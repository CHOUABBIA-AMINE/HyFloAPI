# Generic Notification System

## Overview

The HyFlo API implements a **generic, reusable notification system** that works for **any workflow** in the application. This system automatically handles:

- Event publishing from any service
- Notification creation and persistence
- Real-time WebSocket delivery
- Unread count tracking
- Multiple recipient targeting strategies

---

## Architecture

```
┌──────────────────────────────────────────────────┐
│              Any Service Layer                    │
│   (FlowReadingService, OrderService, etc.)       │
│                                                    │
│   publishEvent(BaseNotificationEvent)             │
└────────────────┬─────────────────────────────────┘
                 │
                 ▼
┌──────────────────────────────────────────────────┐
│      GenericNotificationEventListener            │
│                                                   │
│  1. Resolve NotificationType from DB             │
│  2. Resolve Recipients (username/role/custom)    │
│  3. Create Notifications in DB                   │
│  4. Send via WebSocket                           │
│  5. Update unread counts                         │
└──────────────────────────────────────────────────┘
```

---

## Core Components

### 1. **BaseNotificationEvent** (Base Class)

All notification events extend this abstract class.

**Location**: `dz.sh.trc.hyflo.configuration.event.BaseNotificationEvent`

**Key Fields**:
- `notificationTypeCode` - Database code for notification type
- `notificationTitle` - Notification title
- `notificationMessage` - Notification body/message
- `relatedEntityId` - ID of the related entity
- `relatedEntityType` - Type of entity (e.g., "READING", "ORDER")
- `recipientUsernames` - List of specific usernames to notify
- `recipientRole` - Role for role-based targeting (e.g., "ROLE_VALIDATOR")
- `priority` - Notification priority (LOW, NORMAL, HIGH, URGENT)
- `metadata` - Optional additional data

---

### 2. **GenericNotificationEventListener** (Event Handler)

Handles all events that extend `BaseNotificationEvent`.

**Location**: `dz.sh.trc.hyflo.configuration.event.GenericNotificationEventListener`

**Features**:
- **Automatic recipient resolution** (username, role, or custom logic)
- **Asynchronous processing** (@Async)
- **Independent transactions** (REQUIRES_NEW)
- **Error isolation** (failures don't break business logic)
- **WebSocket integration** (real-time notifications)

---

### 3. **GenericService** (Base Service)

Provides event publishing capability to all services.

**Location**: `dz.sh.trc.hyflo.configuration.template.GenericService`

**Key Methods**:
- `publishEvent(Object event)` - Publish any event
- `afterCreate(E entity)` - Lifecycle hook after entity creation
- `afterUpdate(E entity)` - Lifecycle hook after entity update
- `beforeDelete(E entity)` - Lifecycle hook before entity deletion
- `afterDelete(ID id)` - Lifecycle hook after entity deletion

---

## How to Use This System

### **Step 1: Create Your Event Class**

Extend `BaseNotificationEvent` and add workflow-specific fields.

**Example: Order Placed Event**

```java
package dz.sh.trc.hyflo.order.event;

import dz.sh.trc.hyflo.configuration.event.BaseNotificationEvent;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Getter
@SuperBuilder
@ToString(callSuper = true)
public class OrderPlacedEvent extends BaseNotificationEvent {

    private final Long orderId;
    private final String customerName;
    private final BigDecimal totalAmount;
    private final String orderNumber;

    /**
     * Factory method to create OrderPlacedEvent
     */
    public static OrderPlacedEvent create(
            Long orderId,
            String customerName,
            String customerUsername,
            BigDecimal totalAmount,
            String orderNumber) {
        
        return OrderPlacedEvent.builder()
                // Event-specific fields
                .orderId(orderId)
                .customerName(customerName)
                .totalAmount(totalAmount)
                .orderNumber(orderNumber)
                // Base notification fields
                .notificationTypeCode("ORDER_PLACED")
                .notificationTitle("Order Confirmed")
                .notificationMessage(String.format(
                        "Your order %s for %.2f has been placed successfully.",
                        orderNumber,
                        totalAmount
                ))
                .relatedEntityId(orderId.toString())
                .relatedEntityType("ORDER")
                .recipientUsernames(List.of(customerUsername)) // Specific user
                .occurredAt(LocalDateTime.now().toString())
                .priority(BaseNotificationEvent.NotificationPriority.NORMAL)
                .build();
    }
}
```

---

### **Step 2: Publish Event from Your Service**

Use the `publishEvent()` method from `GenericService`.

**Option A: Using Lifecycle Hooks**

```java
@Service
public class OrderService extends GenericService<Order, OrderDTO, Long> {
    
    @Override
    protected void afterCreate(Order order) {
        // Automatically called after order creation
        publishEvent(OrderPlacedEvent.create(
            order.getId(),
            order.getCustomer().getName(),
            order.getCustomer().getUsername(),
            order.getTotalAmount(),
            order.getOrderNumber()
        ));
    }
}
```

**Option B: Direct Publishing**

```java
@Service
public class OrderService extends GenericService<Order, OrderDTO, Long> {
    
    @Transactional
    public OrderDTO shipOrder(Long orderId, String trackingNumber) {
        // ... shipping logic ...
        
        // Publish event directly
        publishEvent(OrderShippedEvent.create(
            orderId,
            trackingNumber,
            order.getCustomer().getUsername()
        ));
        
        return dto;
    }
}
```

---

### **Step 3: Configure Notification Type in Database**

Ensure the notification type code exists in the `notification_type` table.

```sql
INSERT INTO notification_type (code, name_en, name_fr, name_ar, description, icon, color)
VALUES (
    'ORDER_PLACED',
    'Order Placed',
    'Commande Passée',
    'تم تقديم الطلب',
    'Notification sent when customer places an order',
    'shopping_cart',
    '#4CAF50'
);
```

---

## Recipient Targeting Strategies

The system supports **three recipient targeting strategies**:

### **Strategy 1: Specific Users** (Username-based)

```java
MyEvent.builder()
    .recipientUsernames(List.of("john.doe", "jane.smith"))
    .build();
```

Notification sent to specific users by username.

---

### **Strategy 2: Role-based**

```java
MyEvent.builder()
    .recipientRole("ROLE_VALIDATOR")
    .build();
```

Notification sent to **all users** with the specified role.

---

### **Strategy 3: Custom Logic**

Extend `GenericNotificationEventListener` and override `resolveCustomRecipients()`:

```java
@Component
public class CustomNotificationEventListener 
        extends GenericNotificationEventListener {
    
    @Override
    protected List<User> resolveCustomRecipients(BaseNotificationEvent event) {
        // Custom logic based on event type or metadata
        if (event instanceof OrderPlacedEvent) {
            OrderPlacedEvent orderEvent = (OrderPlacedEvent) event;
            // Return users who have access to this order
            return findUsersWithOrderAccess(orderEvent.getOrderId());
        }
        
        return super.resolveCustomRecipients(event);
    }
}
```

---

## Real-World Examples

### **Example 1: Document Approval Workflow**

```java
// 1. Create events
public class DocumentSubmittedEvent extends BaseNotificationEvent {
    private final Long documentId;
    private final String documentTitle;
    private final String submittedBy;
}

public class DocumentApprovedEvent extends BaseNotificationEvent {
    private final Long documentId;
    private final String approvedBy;
    private final String originalSubmitter;
}

// 2. Publish from service
@Service
public class DocumentService extends GenericService<Document, DocumentDTO, Long> {
    
    @Override
    protected void afterCreate(Document document) {
        publishEvent(DocumentSubmittedEvent.create(
            document.getId(),
            document.getTitle(),
            document.getSubmittedBy().getUsername()
        ));
    }
    
    @Transactional
    public DocumentDTO approve(Long id, Long approverId) {
        // ... approval logic ...
        
        publishEvent(DocumentApprovedEvent.create(
            document.getId(),
            approver.getUsername(),
            document.getSubmittedBy().getUsername()
        ));
        
        return dto;
    }
}
```

---

### **Example 2: User Registration & Activation**

```java
public class UserRegisteredEvent extends BaseNotificationEvent {
    private final Long userId;
    private final String username;
    private final String email;
}

@Service
public class UserService extends GenericService<User, UserDTO, Long> {
    
    @Override
    protected void afterCreate(User user) {
        publishEvent(UserRegisteredEvent.create(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        ).toBuilder()
            .recipientRole("ROLE_ADMIN") // Notify all admins
            .build());
    }
}
```

---

## Notification Priorities

Set priority based on urgency:

```java
.priority(BaseNotificationEvent.NotificationPriority.LOW)      // Info messages
.priority(BaseNotificationEvent.NotificationPriority.NORMAL)   // Standard notifications
.priority(BaseNotificationEvent.NotificationPriority.HIGH)     // Important actions
.priority(BaseNotificationEvent.NotificationPriority.URGENT)   // Critical alerts
```

---

## Metadata Support

Store additional data for custom notification behavior:

```java
MyEvent.builder()
    .metadata(Map.of(
        "sendEmail", true,
        "sendSMS", false,
        "category", "financial",
        "amount", 1500.00
    ))
    .build();
```

---

## Benefits of This System

### ✅ **Reusability**
- Write notification logic once
- Works for **any workflow** (orders, documents, users, etc.)
- No code duplication

### ✅ **Consistency**
- Same notification pattern across all features
- Predictable behavior
- Easy to maintain

### ✅ **Flexibility**
- Multiple recipient targeting strategies
- Custom recipient resolution
- Optional metadata for extensions

### ✅ **Performance**
- Asynchronous processing
- Independent transactions
- Non-blocking WebSocket delivery

### ✅ **Reliability**
- Error isolation (notification failures don't break business logic)
- Transaction safety
- Automatic error logging

---

## Migration Guide

### Migrating Existing Specific Event Listeners

If you have existing specific event listeners (like `NotificationEventListener` for Reading events):

1. **Keep both** during transition period
2. **Refactor events** to extend `BaseNotificationEvent`
3. **Test with generic listener**
4. **Remove specific listener** once validated

**Note**: Both listeners can coexist. Spring will invoke both if they listen to the same event type.

---

## Testing

### Unit Test Example

```java
@Test
void shouldPublishOrderPlacedEvent() {
    // Given
    OrderDTO orderDto = createSampleOrderDTO();
    
    // When
    OrderDTO created = orderService.create(orderDto);
    
    // Then
    verify(eventPublisher).publishEvent(any(OrderPlacedEvent.class));
}
```

---

## Troubleshooting

### Event Not Being Handled

1. **Check event inheritance**: Event must extend `BaseNotificationEvent`
2. **Check notification type**: Code must exist in database
3. **Check Spring context**: `@Component` on listener
4. **Check async configuration**: `@EnableAsync` on main application

### Recipients Not Receiving Notifications

1. **Check recipient resolution**: Log recipients in listener
2. **Check usernames**: Ensure usernames are correct
3. **Check roles**: Verify users have the specified role
4. **Check WebSocket connection**: Ensure users are connected

### Performance Issues

1. **Check notification volume**: Too many recipients?
2. **Consider batching**: For bulk notifications
3. **Check async configuration**: Thread pool sizing

---

## Best Practices

1. **Use factory methods** in event classes for clean construction
2. **Set appropriate priorities** based on urgency
3. **Keep messages concise** and user-friendly
4. **Use metadata** for extensibility, not core data
5. **Test notification flows** in staging before production
6. **Monitor notification delivery** rates and errors

---

## Future Enhancements

- **Email integration**: Auto-send emails for HIGH/URGENT notifications
- **SMS integration**: Critical alerts via SMS
- **Notification templates**: Templating system for messages
- **Notification preferences**: User-configurable notification settings
- **Delivery receipts**: Track notification delivery status
- **Retry mechanism**: Auto-retry failed WebSocket deliveries

---

## Summary

The generic notification system provides a **powerful, flexible foundation** for event-driven notifications across your entire application. By extending `BaseNotificationEvent` and using the `publishEvent()` method, you can add notifications to **any workflow** with minimal code.

**Next Steps**:
1. Review the `BaseNotificationEvent` class
2. Look at `ReadingSubmittedEvent` as a reference
3. Create your first custom event
4. Publish it from your service
5. Test end-to-end notification flow

For questions or issues, contact the development team.
