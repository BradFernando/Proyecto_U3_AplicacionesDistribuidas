package ec.edu.utn.turismourcuqui.services;

import ec.edu.utn.turismourcuqui.dto.Register;
import ec.edu.utn.turismourcuqui.dto.UpdateUserDetails;
import ec.edu.utn.turismourcuqui.exceptions.ClientException;
import ec.edu.utn.turismourcuqui.models.User;
import ec.edu.utn.turismourcuqui.repositories.UserRepository;
import ec.edu.utn.turismourcuqui.security.Argon2CustomPasswordEncoder;
import ec.edu.utn.turismourcuqui.security.jwt.JWT;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Validated
@Log4j2
public class UserService implements UserDetailsService {

    static final String[] ROLES = {"ADMIN", "USER"};

    private final UserRepository repository;
    private final EmailSenderService emailSenderService;
    private final Argon2CustomPasswordEncoder encoder;
    private final SessionAuditService sessionAuditService;
    private final JWT jwtutil;

    @PersistenceContext
    private EntityManager entityManager;

    UserService(UserRepository repository, EmailSenderService emailSenderService, Argon2CustomPasswordEncoder encoder, SessionAuditService sessionAuditService, JWT jwtUtil) {
        this.repository = repository;
        this.emailSenderService = emailSenderService;
        this.encoder = encoder;
        this.sessionAuditService = sessionAuditService;
        jwtutil = jwtUtil;
    }

    public Iterable<User> findAll() {

        return repository
                .findAll()
                .stream()
                .map(this::hidePasswordAndGallery)
                .collect(Collectors.toList());
    }

    public User find(Long id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            return hidePasswordAndGallery(user.get());
        }
        throw new ClientException("El usuario no existe");
    }

    private User hidePasswordAndGallery(User user) {
        entityManager.detach(user);
        user.setPassword(null);
        for (var target : user.getTargets()) {
            target.setGallery(null);
        }
        return user;
    }

    public void updateDetails(Long id, UpdateUserDetails detailsUser) {

        var role = detailsUser.getRole();
        var enabled = detailsUser.getEnabled();
        var password = detailsUser.getPassword();

        if (role == null && enabled == null && password == null) {
            throw new ClientException("No se ha enviado ningún dato para actualizar");
        }

        log.info(detailsUser);
        if (role != null && !Arrays.asList(ROLES).contains(role)) {
            throw new ClientException("El rol que se trata de asignar no existe en el sistema");
        }


        var user = repository.findById(id).orElseThrow(() -> new ClientException("El usuario no existe"));

        if (enabled != null) user.setEnabled(enabled);
        if (role != null) user.setRole(role);
        if (password != null && !password.isBlank()) user.setPassword(encoder.encode(password));


        repository.save(user);

        sessionAuditService.saveActionUser(user, "Actualización de usuario");
        emailSenderService.sendSimpleMail(user.getEmail(), "ACTUALIZACIÓN DE DATOS", "Se han actualizado sus datos de usuario desde el sistema de administración de Turismo Urcuquí, si usted no ha solicitado esta acción, por favor contacte con el administrador del sistema. Sus datos de acceso son: \n" +
                "Usuario: " + user.getUsername() + "\n" +
                "Contraseña: " + (password != null && !password.isBlank() ? password : "No se ha actualizado") + "\n" +
                "Rol: " + user.getRole() + "\n" +
                "Estado: " + (user.isEnabled() ? "Habilitado" : "Deshabilitado") + "\n" +
                "Cuenta no expirada: " + (user.isAccountNonExpired() ? "Si" : "No") + "\n" +
                "Cuenta no bloqueada: " + (user.isAccountNonLocked() ? "Si" : "No") + "\n"
        );
    }


    public String register(Register register) {

        if (repository.existsByUsername(register.getUsername())) {
            throw new ClientException("Nombre de usuario no disponible");
        }

        if (repository.existsByEmail(register.getEmail())) {
            throw new ClientException("El email ingresado ya está registrado");
        }

        var user = User.builder()
                .username(register.getUsername())
                .password(encoder.encode(register.getPassword()))
                .name(register.getName())
                .lastname(register.getLastname())
                .email(register.getEmail().trim())
                .role("USER")
                .build();


        var usersaved = repository.save(user);

        var jwt = jwtutil.create(usersaved.getId().toString(), usersaved.getUsername());

        sessionAuditService.saveActionUser(usersaved, "Registro de usuario");
        emailSenderService.sendSimpleMail(register.getEmail(), "BIENVENIDO A TURISMO URCUQUÍ", "Gracias por registrarse en nuestra aplicacion de realidad aumentada, esperamos que disfrute de su experiencia");

        return jwt;
    }

    public void update(Register register, User user) {

        var username = user.getUsername();
        var email = user.getEmail();

        if (!username.equals(register.getUsername()) && repository.existsByUsername(username)) {
            throw new ClientException("Nombre de usuario no disponible");
        }

        if (!email.equals(register.getEmail()) && repository.existsByEmail(email)) {
            throw new ClientException("El email ingresado ya está registrado");
        }


        user.setUsername(register.getUsername());
        user.setPassword(encoder.encode(register.getPassword()));
        user.setName(register.getName());
        user.setLastname(register.getLastname());
        user.setEmail(register.getEmail());

        repository.save(user);

        sessionAuditService.saveActionUser(user, "Actualización de usuario");
    }


    /**
     * Carga los detalles de un usuario por su nombre de usuario.
     * <p>
     * El método loadUserByUsername() se utiliza para cargar los detalles del usuario basados en el nombre de usuario.
     * Luego, estos detalles se utilizan para autenticar al usuario en el contexto de seguridad
     * de Spring y permitir el acceso a los recursos protegidos.
     *
     * @param username Nombre de usuario del usuario.
     * @return Los detalles del usuario como UserDetails.
     * @throws UsernameNotFoundException          Si no se encuentra ningún usuario con el nombre de usuario especificado.
     * @throws DataAccessResourceFailureException Si ocurre un error al acceder a la fuente de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        var principal = username.trim();
        Optional<User> userOptional = repository.findByUsernameOrEmail(principal, principal);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new UsernameNotFoundException("El usuario con el nombre" + principal + " no existe");
    }
}
