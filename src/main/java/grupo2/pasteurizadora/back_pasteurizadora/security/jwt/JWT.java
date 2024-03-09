package grupo2.pasteurizadora.back_pasteurizadora.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Componente para la generación y validación de tokens JWT.
 */
@Log4j2
@Component
public class JWT {

    /**
     * Clave secreta utilizada para firmar los tokens JWT.
     */
    private static final String KEY = "grupo";

    /**
     * Emisor del token JWT.
     */
    private static final String ISSUER = "espe";

    /**
     * Tiempo de vida (en milisegundos) del token JWT.
     */
    private static final long TTL_MILLIS = 86400000;


    /**
     * Crea un token JWT con el nombre de usuario y el correo electrónico proporcionados.
     *
     * @param id      Nombre de usuario para incluir en el token.
     * @param subject Correo electrónico para incluir en el token.
     * @return El token JWT generado.
     */
    public String create(String id, String subject) {

        log.info("Creating JWT for user id {} and subject {}", id, subject);

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // Algoritmo de firma

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(KEY); // Clave secreta en bytes

        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName()); // Clave secreta para firmar el token

        JwtBuilder builder = Jwts.builder()
                .setId(id) // ID del token
                .setIssuedAt(now) // Fecha de creación del token
                .setSubject(subject) // Asunto del token
                .setIssuer(ISSUER) // Emisor del token
                .signWith(signatureAlgorithm, signingKey);  // Firma del token

        if (TTL_MILLIS >= 0) {
            long expMillis = nowMillis + TTL_MILLIS;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }


    /**
     * Obtiene el nombre de usuario de un token JWT.
     *
     * @param jwt Token JWT del cual se obtiene el nombre de usuario.
     * @return El nombre de usuario del token.
     */
    public String getID(String jwt) {
        log.debug("getting id from jwt '{}'", jwt);
        return getClaim(jwt, Claims::getId);
    }

    /**
     * Obtiene el correo electrónico de un token JWT.
     *
     * @param jwt Token JWT del cual se obtiene el correo electrónico.
     * @return El correo electrónico del token.
     */
    public String getSubject(String jwt) {
        log.debug("getting subject from jwt '{}'", jwt);
        return getClaim(jwt, Claims::getSubject);
    }

    /**
     * Obtiene un reclamo específico del token JWT utilizando un resolvedor de reclamos dado.
     *
     * @param jwt            Token JWT del cual se obtendrá el reclamo.
     * @param claimsResolver Función que resuelve el reclamo deseado a partir de los Claims del token.
     * @param <T>            Tipo de dato del reclamo que se desea obtener.
     * @return El reclamo específico del token JWT.
     */
    private <T> T getClaim(String jwt, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    /**
     * Obtiene todos los reclamos (Claims) contenidos en el token JWT.
     *
     * @param jwt Token JWT del cual se obtendrán los reclamos.
     * @return Objeto Claims que representa todos los reclamos del token JWT.
     * @throws SignatureException Si ocurre un error al verificar la firma del token JWT.
     */
    private Claims getAllClaims(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(KEY))
                .parseClaimsJws(jwt)
                .getBody();
    }
}
