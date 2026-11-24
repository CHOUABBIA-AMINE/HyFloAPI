package dz.sh.hyflo;

import java.time.Instant;

import dz.sh.hyflo.modules.foundation.identity.domain.model.IdentityProvider;
import dz.sh.hyflo.modules.foundation.identity.domain.model.LocalCredential;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserStatus;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HyFloApiTest {

    @Test
    void lombokAndMapStructAnnotationProcessorsWorkTogether() {
        var mapped = Mappers.getMapper(BuildProbeMapper.class)
                .map(new BuildProbeSource("synthetic"));
        assertThat(mapped.getValue()).isEqualTo("synthetic");
    }

    @Test
    void activationRequiresOwnCredentialAndDisabledAccountsCannotBeReactivated() {
        var provider = IdentityProvider.local();
        var pending = new UserStatus(UserStatus.PENDING_CODE, "Pending");
        var active = new UserStatus(UserStatus.ACTIVE_CODE, "Active");
        var suspended = new UserStatus(UserStatus.SUSPENDED_CODE, "Suspended");
        var disabled = new UserStatus(UserStatus.DISABLED_CODE, "Disabled");
        var account = new User(provider, "  Synthetic.User  ", "account@example.invalid", null, pending);
        var other = new User(provider, "other", "other@example.invalid", null, pending);
        String hash = new BCryptPasswordEncoder().encode("synthetic-account-password");
        var changedAt = Instant.parse("2026-11-24T00:00:00Z");
        var foreignCredential = new LocalCredential(other, hash, changedAt);
        var ownCredential = new LocalCredential(account, hash, changedAt);

        assertThat(account.getNormalizedUsername()).isEqualTo("synthetic.user");
        assertThatThrownBy(() -> account.activate(foreignCredential, active))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(account.getStatus().getCode()).isEqualTo(UserStatus.PENDING_CODE);

        account.activate(ownCredential, active);
        account.suspend(suspended);
        assertThat(account.getStatus().getCode()).isEqualTo(UserStatus.SUSPENDED_CODE);
        account.activate(ownCredential, active);
        account.disable(disabled);
        assertThatThrownBy(() -> account.activate(ownCredential, active)).isInstanceOf(IllegalStateException.class);
        assertThat(account.getStatus().getCode()).isEqualTo(UserStatus.DISABLED_CODE);
    }

    @Test
    void jwtSigningAndParsingWorkWithTheRuntimeSerializer() {
        var key = Jwts.SIG.HS256.key().build();
        var token = Jwts.builder().subject("synthetic-user").signWith(key).compact();
        var claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
        assertThat(claims.getSubject()).isEqualTo("synthetic-user");
    }
}

@Getter
class BuildProbeSource {
    private final String value;

    BuildProbeSource(String value) {
        this.value = value;
    }
}

record BuildProbeTarget(String value) {
    public String getValue() {
        return value;
    }
}

@Mapper
interface BuildProbeMapper {
    BuildProbeTarget map(BuildProbeSource source);
}
