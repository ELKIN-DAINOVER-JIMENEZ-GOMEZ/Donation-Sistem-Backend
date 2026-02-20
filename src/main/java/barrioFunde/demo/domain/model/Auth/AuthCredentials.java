package barrioFunde.demo.domain.model.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de dominio puro para las credenciales de autenticación
 * Sin anotaciones de framework
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthCredentials {
    private String email;
    private String password;

    public void validar() {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("El password es obligatorio");
        }
    }
}