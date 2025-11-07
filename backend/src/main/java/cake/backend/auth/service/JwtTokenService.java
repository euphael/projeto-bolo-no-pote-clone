package cake.backend.auth.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import cake.backend.auth.model.UserDetailsImpl;

@Service
public class JwtTokenService {
    private static final String SECRET_KEY = "+Zf+52ufnK06ZakahLaAAw==";
    private static final String ISSUER = "cake-api";

    /**
     * Gera um token JWT.
     * 
     * @param user Detalhes do usuário.
     * @return Token JWT.
     */
    public String generateToken(UserDetailsImpl user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(creationDate())
                    .withExpiresAt(expirationDate())
                    .withSubject(user.getUsername())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new JWTCreationException("Erro ao gerar token", e);
        }
    }

    /**
     * Obtendo o assunto do token.
     * 
     * @param token Token JWT.
     * @return Assunto do token.
     */
    public String getSubjectFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token inválido ou expirado.");
        }
    }

    /**
     * Obtendo a data de criação do token.
     * 
     * @return Data de criação do token.
     */
    private Instant creationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Recife")).toInstant();
    }

    /**
     * Obtendo a data de expiração do token.
     * 
     * @return Data de expiração do token.
     */
    private Instant expirationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Recife")).plusHours(4).toInstant();
    }
}
