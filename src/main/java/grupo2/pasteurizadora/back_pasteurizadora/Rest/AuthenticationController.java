package grupo2.pasteurizadora.back_pasteurizadora.Rest;


import grupo2.pasteurizadora.back_pasteurizadora.dto.Login;
import grupo2.pasteurizadora.back_pasteurizadora.dto.Register;
import grupo2.pasteurizadora.back_pasteurizadora.entity.User;
import grupo2.pasteurizadora.back_pasteurizadora.services.AuthenticationService;
import grupo2.pasteurizadora.back_pasteurizadora.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;


/**
 * Controlador para la autenticación y gestión de usuarios.
 */
@RestController
public class AuthenticationController {
    private final AuthenticationService service;

    private final UserService userService;

    AuthenticationController(AuthenticationService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    /**
     * Autentica un usuario.
     *
     * @param login El objeto LoginDTO que contiene las credenciales del usuario.
     * @return Una ResponseEntity que contiene el token JWT si la autenticación es exitosa, o un mensaje de error si las credenciales son inválidas.
     */
    @PostMapping(value = "/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody Login login) {
        var jwt = service.authenticate(login);
        return ResponseEntity.ok().body(jwt);
    }

    /**
     * Registra un nuevo usuario.
     *
     * @param register El objeto User que representa al nuevo usuario.
     * @return Una ResponseEntity que contiene el token JWT si el registro es exitoso, o un mensaje de error si ocurre algún problema.
     */
    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody Register register) {
        var jwt = userService.register(register);
        return ResponseEntity.status(CREATED).body(jwt);
    }

    /**
     * Actualiza los datos de un usuario.
     *
     * @param register   El objeto User que representa al usuario.
     * @param principals El objeto AuthenticationPrincipal que representa al usuario autenticado.
     * @return Una ResponseEntity que contiene el mensaje "Actualizado" si la actualización es exitosa, o un mensaje de error si ocurre algún problema.
     */
    @PutMapping(value = "/update")
    public ResponseEntity<String> update(@RequestBody Register register, @AuthenticationPrincipal User principals) {
        userService.update(register, principals);
        return ResponseEntity.status(CREATED).body("Actualizado");

    }

    /**
     * Obtiene los detalles de un usuario.
     *
     * @param principals El objeto AuthenticationPrincipal que representa al usuario autenticado.
     * @return Una ResponseEntity que contiene el objeto User si el usuario está autenticado, o un mensaje de error si no es válido.
     */
    @GetMapping(value = "/whoami")
    public ResponseEntity<User> whoami(@AuthenticationPrincipal User principals) {
        principals.hidePassword();
        return ResponseEntity.ok().body(principals);
    }
}