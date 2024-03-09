package grupo2.pasteurizadora.back_pasteurizadora.services;


import grupo2.pasteurizadora.back_pasteurizadora.dto.Login;
import grupo2.pasteurizadora.back_pasteurizadora.entity.User;
import grupo2.pasteurizadora.back_pasteurizadora.repository.UserRepository;
import grupo2.pasteurizadora.back_pasteurizadora.security.Argon2CustomPasswordEncoder;
import grupo2.pasteurizadora.back_pasteurizadora.security.jwt.JWT;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Log4j2
@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final Argon2CustomPasswordEncoder encoder;
    private final JWT jwtutil;

    @Autowired
    public AuthenticationService(UserRepository userRepository, JWT jwt, AuthenticationManager authenticationManager, Argon2CustomPasswordEncoder encoder) {

        this.userRepository = userRepository;
        this.jwtutil = jwt;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;


        if (userRepository.existsByUsername("admin")) {
            log.info("Ya existe un usuario administrador en el sistema, omitiendo creación");
            return;
        }

        var admin = User.builder()
                .username("admin")
                .password(encoder.encode("Qwerty1598."))
                .name("Administrador")
                .lastname("Administrador")
                .email("jhonfecrho@gmail.com")
                .role("ADMIN")
                .enabled(true)
                .build();

        log.info("Creando usuario administrador, sus credenciales son: {} - {}", admin.getUsername(), admin.getPassword());
        userRepository.save(admin);
    }

    public String authenticate(Login login) {
        var authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(login.getUsername(), login.getPassword());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var user = (User) authentication.getPrincipal();
        return jwtutil.create(user.getId().toString(), user.getUsername());
    }

}
