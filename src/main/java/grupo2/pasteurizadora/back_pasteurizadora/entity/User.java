package grupo2.pasteurizadora.back_pasteurizadora.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "my_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role", nullable = false, length = 100)
    @NotBlank(message = "El rol es obligatorio")
    @Size(max = 100, message = "El rol no puede tener más de 100 caracteres")
    @Pattern(regexp = "^(ADMIN|USER)$", message = "Los unicos roles válidos son ADMIN y USER")
    private String role;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String name;

    @Column(name = "lastname", nullable = false, length = 100)
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede tener más de 100 caracteres")
    private String lastname;

    @Column(name = "username", nullable = false, length = 100, unique = true)
    @NotBlank(message = "El username es obligatorio")
    @Size(max = 100, message = "El username no puede tener más de 100 caracteres")
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    @NotBlank(message = "El password es obligatorio")
    @NotNull(message = "El password es obligatorio")
    private String password;

    @Column(name = "email", nullable = false, length = 100)
    @NotBlank(message = "El email es obligatorio")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    @Email(message = "El email no es válido")
    private String email;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    public void hidePassword() {
        password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_" + role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
