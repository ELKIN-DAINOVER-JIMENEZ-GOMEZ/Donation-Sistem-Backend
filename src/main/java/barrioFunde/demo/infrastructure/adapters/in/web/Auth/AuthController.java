package barrioFunde.demo.infrastructure.adapters.in.web.Auth;


import barrioFunde.demo.application.ports.in.Auth.AutenticarUsuarioUseCase;
import barrioFunde.demo.domain.model.Auth.AuthCredentials;
import barrioFunde.demo.domain.model.Auth.AuthToken;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.Auth.LoginRequestDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.Auth.LoginResponseDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.mapper.Auth.AuthDTOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación
 * Adaptador de entrada que expone los endpoints de login
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Configurar según tus necesidades de CORS
public class AuthController {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    /**
     * Endpoint para login
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            // 1. Convertir DTO → Dominio
            AuthCredentials credentials = AuthDTOMapper.toDomain(loginRequest);

            // 2. Ejecutar caso de uso
            AuthToken authToken = autenticarUsuarioUseCase.autenticar(credentials);

            // 3. Convertir Dominio → DTO
            LoginResponseDTO response = AuthDTOMapper.toResponseDTO(authToken);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciales inválidas", e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("Usuario inactivo", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor", e.getMessage()));
        }
    }

    /**
     * Clase interna para respuestas de error
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    private static class ErrorResponse {
        private String error;
        private String message;
    }
}



