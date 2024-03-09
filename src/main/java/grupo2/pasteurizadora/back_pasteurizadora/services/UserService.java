package grupo2.pasteurizadora.back_pasteurizadora.services;

import grupo2.pasteurizadora.back_pasteurizadora.dto.Register;
import grupo2.pasteurizadora.back_pasteurizadora.entity.User;
import grupo2.pasteurizadora.back_pasteurizadora.exception.ClientException;
import grupo2.pasteurizadora.back_pasteurizadora.repository.UserRepository;
import grupo2.pasteurizadora.back_pasteurizadora.security.Argon2CustomPasswordEncoder;
import grupo2.pasteurizadora.back_pasteurizadora.security.jwt.JWT;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Transactional
@Log4j2
@Validated
public class UserService implements UserDetailsService {

    static final String[] ROLES = {"ADMIN", "USER"};

    private final UserRepository repository;
    private final Argon2CustomPasswordEncoder encoder;
    private final JWT jwtutil;

    @PersistenceContext
    private EntityManager entityManager;

    UserService(UserRepository repository, Argon2CustomPasswordEncoder encoder, JWT jwtUtil) {
        this.repository = repository;
        this.encoder = encoder;
        jwtutil = jwtUtil;
    }

    public Iterable<User> findAll() {

        return repository
                .findAll()
                .stream()
                .map(this::hidePassword)
                .toList();
    }

    public User find(Long id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            return hidePassword(user.get());
        }
        throw new ClientException("El usuario no existe");
    }

    private User hidePassword(User user) {
        entityManager.detach(user);
        user.setPassword(null);
        return user;
    }


    public String register(@Valid Register register) {

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
                .enabled(true)
                .build();


        var usersaved = repository.save(user);

        return jwtutil.create(usersaved.getId().toString(), usersaved.getUsername());
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
