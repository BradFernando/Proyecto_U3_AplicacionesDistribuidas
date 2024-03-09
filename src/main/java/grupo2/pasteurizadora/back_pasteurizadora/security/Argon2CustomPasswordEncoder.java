package grupo2.pasteurizadora.back_pasteurizadora.security;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Implementación personalizada de PasswordEncoder que utiliza el algoritmo Argon2 para el hash y la verificación de contraseñas.
 */

@Log4j2
@Component
public class Argon2CustomPasswordEncoder implements PasswordEncoder {
    /**
     * Codifica la contraseña en bruto utilizando el algoritmo Argon2.
     *
     * @param rawPassword Contraseña en bruto a codificar.
     * @return Contraseña codificada.
     */
    @Override
    public String encode(CharSequence rawPassword) {
        log.info("Codificando contraseña");
        return Argon2Encoder.encode(rawPassword.toString());
    }

    /**
     * Verifica si la contraseña en bruto coincide con la contraseña codificada utilizando el algoritmo Argon2.
     *
     * @param rawPassword     Contraseña en bruto a verificar.
     * @param encodedPassword Contraseña codificada.
     * @return true si la contraseña coincide, false en caso contrario.
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        log.info("Verificando contraseña");
        return Argon2Encoder.verify(encodedPassword, rawPassword.toString());
    }

    /**
     * Verifica si la contraseña codificada necesita una actualización o mejora en su algoritmo de codificación.
     * Este método se hereda de la interfaz PasswordEncoder y devuelve false por defecto, lo que indica que no se requiere actualización.
     *
     * @param encodedPassword Contraseña codificada a verificar.
     * @return true si la contraseña necesita actualización, false en caso contrario.
     */
    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        log.info("Verificando si la contraseña necesita actualización");
        return PasswordEncoder.super.upgradeEncoding(encodedPassword);
    }
}
