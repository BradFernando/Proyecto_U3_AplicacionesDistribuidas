package ec.edu.utn.turismourcuqui.services;


import ec.edu.utn.turismourcuqui.dto.*;
import ec.edu.utn.turismourcuqui.exceptions.ClientException;
import ec.edu.utn.turismourcuqui.models.*;
import ec.edu.utn.turismourcuqui.repositories.*;
import ec.edu.utn.turismourcuqui.security.Argon2CustomPasswordEncoder;
import ec.edu.utn.turismourcuqui.security.jwt.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenForgetPasswordRepository forgetPasswordRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailSenderService emailSenderService;
    private final SessionAuditService sessionAuditService;
    private final Argon2CustomPasswordEncoder encoder;
    private final JWT jwtutil;


    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public AuthenticationService(UserRepository userRepository, TokenForgetPasswordRepository forgetPasswordRepository, EmailSenderService emailSenderService, JWT jwt, AuthenticationManager authenticationManager, SessionAuditService sessionAuditService, Argon2CustomPasswordEncoder encoder) {

        this.userRepository = userRepository;
        this.forgetPasswordRepository = forgetPasswordRepository;
        this.emailSenderService = emailSenderService;
        this.jwtutil = jwt;
        this.authenticationManager = authenticationManager;
        this.sessionAuditService = sessionAuditService;
        this.encoder = encoder;

        this.forgetPasswordRepository.deleteExpiredToken();


        if (userRepository.existsByUsername("admin")) {
            logger.info("Ya existe un usuario administrador en el sistema, omitiendo creación");
            return;
        }

        var admin = User.builder()
                .username("admin")
                .password(encoder.encode("Qwerty1598."))
                .name("Administrador")
                .lastname("Administrador")
                .email("playerluis159@gmail.com")
                .role("ADMIN")
                .build();

        logger.info("Creando usuario administrador, sus credenciales son: {} - {}", admin.getUsername(), admin.getPassword());
        emailSenderService.sendSimpleMail(admin.getEmail(), "Se acaba de reiniciar el servidor", "Se acaba de reiniciar el servidor, sus credenciales del usuario administrador son: " + admin.getUsername() + " - " + admin.getPassword());
        userRepository.save(admin);
    }

    public String authenticate(Login login) {
        var authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(login.getUsername(), login.getPassword());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var user = (User) authentication.getPrincipal();
        var jwt = jwtutil.create(user.getId().toString(), user.getUsername());
        sessionAuditService.saveActionUser(user, "Inicio de sesión");
        return jwt;
    }


    public void forgotPassword(String email) {
        var userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return;
        }

        var token = generateCode();
        saveTokenForgetPassword(email, token);
        emailSenderService.sendSimpleMail(email, "RECUPERACIÓN DE CUENTA - TURISMO URCIQUÍ", "Su código de recuperación de contraseña es: " + token);
        sessionAuditService.saveActionUser(userOptional.get(), "Solicitud de cambio de contraseña");
    }


    public void resetPassword(RecoveryPasswordData data) {

        var tokenOptional = forgetPasswordRepository.findByToken(data.getToken());

        if (tokenOptional.isEmpty()) {
            throw new ClientException("El código de recuperación de contraseña no es válido");
        }

        var token = tokenOptional.get();

        if (token.isExpired()) {
            forgetPasswordRepository.delete(token);
            throw new ClientException("El código de recuperación ha expirado, por favor solicite uno nuevo");
        }

        var userOptional = userRepository.findByEmail(token.getEmail());

        if (userOptional.isEmpty()) {
            throw new ClientException("No se pudo cambiar la contraseña, por favor intente nuevamente");
        }

        var user = userOptional.get();

        user.setPassword(encoder.encode(data.getPassword()));
        userRepository.save(user);
        forgetPasswordRepository.delete(token);
        sessionAuditService.saveActionUser(user, "Cambio de contraseña mediante código de recuperación");

    }

    private void saveTokenForgetPassword(String email, String token) {

        var tokenForgetPassword = TokenForgetPassword.builder()
                .email(email)
                .token(token)
                .build();

        forgetPasswordRepository.save(tokenForgetPassword);
    }


    private static String generateCode() {
        return String.format("%08d", new Random().nextInt(100000000));
    }

}
