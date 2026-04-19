package dz.sh.trc.hyflo.core.system.security.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;

import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.system.security.dto.request.CreateUserRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateUserRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.UserResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.UserSummary;
import dz.sh.trc.hyflo.core.system.security.mapper.UserMapper;
import dz.sh.trc.hyflo.core.system.security.model.Group;
import dz.sh.trc.hyflo.core.system.security.model.Role;
import dz.sh.trc.hyflo.core.system.security.model.User;
import dz.sh.trc.hyflo.core.system.security.repository.UserRepository;
import dz.sh.trc.hyflo.core.system.security.service.UserService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;
import dz.sh.trc.hyflo.exception.base.ErrorCode;
import dz.sh.trc.hyflo.exception.business.BusinessException;

@Service
public class UserServiceImpl extends AbstractCrudService<CreateUserRequest, UpdateUserRequest, UserResponse, UserSummary, User, Long> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository repository, UserMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher, PasswordEncoder passwordEncoder) {
        super(repository, mapper, referenceResolver, eventPublisher);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = repository;
        this.userMapper = mapper;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected void beforeCreate(CreateUserRequest request, User entity) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException(ErrorCode.AUTH_USERNAME_ALREADY_EXISTS, "Username already in use", HttpStatus.CONFLICT);
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.AUTH_EMAIL_ALREADY_EXISTS, "Email already in use", HttpStatus.CONFLICT);
        }

        entity.setPassword(passwordEncoder.encode(request.password()));
        
        if (request.employeeId() != null) {
            entity.setEmployee(referenceResolver.resolve(request.employeeId(), Employee.class));
        }

        if (request.roleIds() != null) {
            request.roleIds().forEach(roleId -> entity.addRole(referenceResolver.resolve(roleId, Role.class)));
        }

        if (request.groupIds() != null) {
            request.groupIds().forEach(groupId -> entity.addGroup(referenceResolver.resolve(groupId, Group.class)));
        }
    }

    @Override
    protected void beforeUpdate(UpdateUserRequest request, User entity) {
        if (request.email() != null && !request.email().equals(entity.getEmail()) && userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.AUTH_EMAIL_ALREADY_EXISTS, "Email already in use", HttpStatus.CONFLICT);
        }

        if (request.roleIds() != null) {
            entity.getRoles().clear();
            request.roleIds().forEach(roleId -> entity.addRole(referenceResolver.resolve(roleId, Role.class)));
        }

        if (request.groupIds() != null) {
            entity.getGroups().clear();
            request.groupIds().forEach(groupId -> entity.addGroup(referenceResolver.resolve(groupId, Group.class)));
        }
    }

    @Override
    @Transactional
    public void assignRole(Long userId, Long roleId) {
        User user = findEntityById(userId);
        user.addRole(referenceResolver.resolve(roleId, Role.class));
        repository.save(user);
    }

    @Override
    @Transactional
    public void removeRole(Long userId, Long roleId) {
        User user = findEntityById(userId);
        user.removeRole(referenceResolver.resolve(roleId, Role.class));
        repository.save(user);
    }

    @Override
    @Transactional
    public void assignGroup(Long userId, Long groupId) {
        User user = findEntityById(userId);
        user.addGroup(referenceResolver.resolve(groupId, Group.class));
        repository.save(user);
    }

    @Override
    @Transactional
    public void removeGroup(Long userId, Long groupId) {
        User user = findEntityById(userId);
        user.removeGroup(referenceResolver.resolve(groupId, Group.class));
        repository.save(user);
    }

    @Override
    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTH_USER_NOT_FOUND, "User not found"));
        return userMapper.toResponse(user);
    }
}
