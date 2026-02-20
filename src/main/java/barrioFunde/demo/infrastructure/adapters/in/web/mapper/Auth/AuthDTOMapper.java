package barrioFunde.demo.infrastructure.adapters.in.web.mapper.Auth;


import barrioFunde.demo.domain.model.Auth.AuthCredentials;
import barrioFunde.demo.domain.model.Auth.AuthToken;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.Auth.LoginRequestDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.Auth.LoginResponseDTO;

/**
 * Mapper para convertir entre DTOs web y modelos de dominio de autenticación
 */
public class AuthDTOMapper {

    private AuthDTOMapper() {
        // Constructor privado para clase utilitaria
    }

    /**
     * Convierte LoginRequestDTO → AuthCredentials (dominio)
     */
    public static AuthCredentials toDomain(LoginRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return AuthCredentials.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    /**
     * Convierte AuthToken (dominio) → LoginResponseDTO
     */
    public static LoginResponseDTO toResponseDTO(AuthToken authToken) {
        if (authToken == null) {
            return null;
        }

        return LoginResponseDTO.builder()
                .token(authToken.getToken())
                .type(authToken.getType())
                .email(authToken.getUsuario().getEmail())
                .nombre(authToken.getUsuario().getNombre())
                .rol(authToken.getUsuario().getRol().name())
                .userId(authToken.getUsuario().getId())
                .build();
    }
}