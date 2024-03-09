package ec.edu.utn.turismourcuqui.security.jwt;


import ec.edu.utn.turismourcuqui.dto.ErrorResponse;
import ec.edu.utn.turismourcuqui.security.UserDetailsAuthenticaction;
import ec.edu.utn.turismourcuqui.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que se ejecuta una vez por cada solicitud HTTP para validar y procesar el token JWT en el encabezado de autorización.
 * Si el token es válido, establece la autenticación en el contexto de seguridad de Spring Security.
 */
@Component
@Log4j2
public class JWTOncePerRequestFilter extends OncePerRequestFilter {
    private final JWT jwtutil;
    private final UserService userDetailsService;


    /**
     * Instantiates a new Jwt once per request filter.
     *
     * @param jwtutil            the jwt util
     * @param userDetailsService the user details service
     */
    public JWTOncePerRequestFilter(JWT jwtutil, UserService userDetailsService) {
        this.jwtutil = jwtutil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Método que se ejecuta para cada solicitud HTTP y realiza la validación y procesamiento del token JWT.
     *
     * @param request     Objeto HttpServletRequest que representa la solicitud HTTP actual.
     * @param response    Objeto HttpServletResponse que representa la respuesta HTTP actual.
     * @param filterChain Objeto FilterChain utilizado para invocar el siguiente filtro en la cadena de filtros.
     * @throws ServletException Si ocurre una excepción relacionada con el servlet.
     * @throws IOException      Si ocurre una excepción de E/S.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        final String authenticationHeader = request.getHeader("Authorization");

        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authenticationHeader.replace("Bearer ", "");

        try {

            String username = jwtutil.getSubject(jwtToken);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            var authentication = new UserDetailsAuthenticaction(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);


        } catch (UnsupportedJwtException err) {
            writeError(HttpServletResponse.SC_UNAUTHORIZED, "Token de autenticación no soportado, inicie sesion nuevamente", response);
        } catch (MalformedJwtException err) {
            writeError(HttpServletResponse.SC_UNAUTHORIZED, "Token de autenticación inválido, inicie sesion nuevamente", response);
        } catch (ExpiredJwtException err) {
            writeError(HttpServletResponse.SC_CONFLICT, "Sesión expirada. Inicie sesión nuevamente", response);
        } catch (UsernameNotFoundException err) {
            writeError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado", response);
        } catch (SignatureException err) {
            writeError(HttpServletResponse.SC_UNAUTHORIZED, "Firma del token de autenticación inválida", response);
        } catch (IllegalArgumentException | DataAccessResourceFailureException err) {
            writeError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en el servidor: " + err.getMessage(), response);
        }
    }


    private void writeError(Integer code, String message, HttpServletResponse response) throws IOException {
        String json = ErrorResponse.jsonOf(message);
        log.info("Error de autenticación: {}", json);
        response.setStatus(code);
        response.getWriter().write(json);
    }
}
