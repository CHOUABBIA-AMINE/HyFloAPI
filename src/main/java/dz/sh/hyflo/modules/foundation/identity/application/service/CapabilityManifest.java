/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : CapabilityManifest
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : CapabilityManifest for controlled capability reconciliation.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

/** Reviewed executable capabilities, not customer role configuration. */
public final class CapabilityManifest {
    public static final String ID = "hyflo-identity-capabilities-v2";
    private static final List<Entry> ENTRIES = List.of(
            entry("user.read"), entry("user-employee.read"), entry("user-employee.associate"),
            entry("user.create"), entry("user.activate"), entry("user.suspend"), entry("user.reactivate"),
            entry("user.disable"), entry("credential.initialize"), entry("credential.reset"),
            entry("login-session.revoke"), entry("role.read"), entry("permission.read"),
            entry("user-role-grant.read"), entry("user-role-grant.assign"), entry("user-role-grant.revoke"),
            entry("identity-catalog.read"), entry(PlatformAuthorizationService.ROLE_READ),
            entry(PlatformAuthorizationService.ROLE_CREATE), entry(PlatformAuthorizationService.CAPABILITY_RECONCILE),
            entry(PlatformAuthorizationService.PERMISSION_READ), entry(PlatformAuthorizationService.PERMISSION_ASSOCIATE),
            entry(PlatformAuthorizationService.PERMISSION_REMOVE))
            .stream().sorted(Comparator.comparing(Entry::permissionCode)).toList();
    private static final String DIGEST = calculateDigest();

    private CapabilityManifest() {}

    public static List<Entry> entries() { return ENTRIES; }
    public static String digest() { return DIGEST; }

    public static boolean supports(dz.sh.hyflo.modules.foundation.identity.domain.model.Permission permission) {
        return ENTRIES.stream().anyMatch(e -> e.permissionCode().equals(permission.getCode())
                && e.permissionLabel().equals(permission.getDisplayName())
                && e.resourceCode().equals(permission.getResourceType().getCode())
                && e.resourceLabel().equals(permission.getResourceType().getDisplayName())
                && e.actionCode().equals(permission.getAction().getCode())
                && e.actionLabel().equals(permission.getAction().getDisplayName()));
    }

    public record Entry(String resourceCode, String actionCode, String permissionCode,
            String resourceLabel, String actionLabel, String permissionLabel) {
        public Entry {
            if (!validPart(resourceCode) || !validPart(actionCode)
                    || !Objects.equals(resourceCode + "." + actionCode, permissionCode)
                    || permissionCode.length() > 120
                    || !validLabel(resourceLabel) || !validLabel(actionLabel) || !validLabel(permissionLabel))
                throw new IllegalArgumentException("Invalid capability manifest definition");
        }

        private String canonical() {
            return String.join("\t", resourceCode, actionCode, permissionCode,
                    resourceLabel, actionLabel, permissionLabel) + "\n";
        }
    }

    private static Entry entry(String code) {
        String[] parts = code.split("\\.", -1);
        if (parts.length != 2) throw new IllegalArgumentException("Invalid capability code");
        return new Entry(parts[0], parts[1], code, label(parts[0]), label(parts[1]),
                label(parts[0]) + " / " + label(parts[1]));
    }

    private static String label(String code) {
        String words = code.replace('-', ' ');
        return Character.toUpperCase(words.charAt(0)) + words.substring(1);
    }

    private static boolean validPart(String value) {
        return value != null && value.matches("[a-z0-9_-]{1,64}");
    }

    private static boolean validLabel(String value) {
        return value != null && !value.isBlank() && value.length() <= 120
                && value.chars().noneMatch(Character::isISOControl);
    }

    private static String calculateDigest() {
        if (ENTRIES.stream().map(Entry::permissionCode).distinct().count() != ENTRIES.size())
            throw new IllegalStateException("Duplicate manifest capability");
        // Code is the exact resource/action pair, so uniqueness covers both.
        var resources = new java.util.HashMap<String, String>();
        var actions = new java.util.HashMap<String, String>();
        for (Entry e : ENTRIES) {
            if (!resources.computeIfAbsent(e.resourceCode(), key -> e.resourceLabel()).equals(e.resourceLabel())
                    || !actions.computeIfAbsent(e.actionCode(), key -> e.actionLabel()).equals(e.actionLabel()))
                throw new IllegalStateException("Inconsistent manifest labels");
        }
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            for (Entry e : ENTRIES) digest.update(e.canonical().getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest.digest());
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException("SHA-256 unavailable", impossible);
        }
    }
}
