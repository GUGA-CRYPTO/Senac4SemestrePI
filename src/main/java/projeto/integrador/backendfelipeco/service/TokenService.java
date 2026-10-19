package projeto.integrador.backendfelipeco.service;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    private static final long EXPIRACAO_EM_SEGUNDOS = 7200L; //7200 segundos da 2 horas

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String gerarToken(String email) {
        Instant agora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("aura-finance-api")
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(EXPIRACAO_EM_SEGUNDOS))
                .subject(email)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
