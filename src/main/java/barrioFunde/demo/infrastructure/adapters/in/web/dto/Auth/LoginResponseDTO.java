package barrioFunde.demo.infrastructure.adapters.in.web.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar la respuesta de login al frontend
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String type;
    private String email;
    private String nombre;
    private String rol;
    private Long userId;
}