package grupo2.pasteurizadora.back_pasteurizadora.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import grupo2.pasteurizadora.back_pasteurizadora.dto.ErrorResponse;
import grupo2.pasteurizadora.back_pasteurizadora.security.jwt.JWTOncePerRequestFilter;
import grupo2.pasteurizadora.back_pasteurizadora.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Log4j2
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    UserDetailsService securityUserDetailsService;
    JWTOncePerRequestFilter jwtAuthenticationFilter;
    Argon2CustomPasswordEncoder argon2CustomPasswordEncoder;

    WebSecurityConfig(UserService securityUserDetailsService, JWTOncePerRequestFilter jwtAuthenticationFilter, Argon2CustomPasswordEncoder argon2CustomPasswordEncoder) {
        this.securityUserDetailsService = securityUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.argon2CustomPasswordEncoder = argon2CustomPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("Configurando CSRF");
        http.csrf(CsrfConfigurer::disable);

        log.info("Configurando CORS");
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        log.info("Configurando autorización de solicitudes HTTP");
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/",
                        "/index.html",
                        "/static/**",
                        "/assets/**",
                        "/login",
                        "/logout",
                        "/authenticate",
                        "/register",
                        "/login",
                        "/loteProductos",
                        "/loteProductos/**"
                ).permitAll()
                .requestMatchers(
                        "/recepcionLeche",
                        "/recepcionLeche/**",
                        "/procesoVerificacion",
                        "/procesoVerificacion/**"
                ).hasRole("ADMIN")
                .requestMatchers(
                        "/haciendaLecha",
                        "/haciendaLecha/**",
                        "/lecheroIndependiente",
                        "/lecheroIndependiente/**",
                        "/procesoPasteurizacion",
                        "/procesoPasteurizacion/**"
                ).hasAnyRole("USER", "ADMIN")
                .requestMatchers(
                        antMatcher(HttpMethod.GET, "/api/v1/**")
                ).hasRole("USER")
                .requestMatchers(
                        "/whoami"
                ).authenticated()
                .anyRequest()
                .authenticated()

        );

        log.info("Configurando autenticación");
        http.sessionManagement(sesion -> sesion
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        log.info("Configurando filtro de autenticación JWT");
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("Configurando proveedor de autenticación");
        http.userDetailsService(securityUserDetailsService);


        log.info("Configurando filtro de autenticación básica");
        http.httpBasic(basic -> basic.authenticationEntryPoint((request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse("No autorizado", HttpStatus.UNAUTHORIZED.value())));
        }));

        log.info("Configurando autenticación");
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        log.warn("Configurando CORS para desarrollo, asegurarse de deshabilitar cuando despliegue la aplicación en producción");

        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Permitir desde cualquier origen
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(securityUserDetailsService);
        authenticationProvider.setPasswordEncoder(argon2CustomPasswordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenAticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}