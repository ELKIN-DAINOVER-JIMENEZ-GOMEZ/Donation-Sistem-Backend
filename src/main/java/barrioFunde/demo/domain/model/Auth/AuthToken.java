package barrioFunde.demo.domain.model.Auth;

import barrioFunde.demo.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Modelo de dominio puro para el token de autenticación
 * Sin anotaciones de framework
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {
    private String token;
    private String type;
    private LocalDateTime expiresAt;
    private Usuario usuario;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}