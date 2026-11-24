package dz.sh.hyflo;

import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class HyFloApiTest {

    @Test
    void lombokAndMapStructAnnotationProcessorsWorkTogether() {
        var mapped = Mappers.getMapper(BuildProbeMapper.class)
                .map(new BuildProbeSource("synthetic"));
        assertThat(mapped.getValue()).isEqualTo("synthetic");
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
