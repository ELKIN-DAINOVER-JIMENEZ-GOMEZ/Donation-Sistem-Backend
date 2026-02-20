package barrioFunde.demo.infrastructure.adapters.in.web.mapper;


import barrioFunde.demo.domain.model.Usuario;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.UsuarioActualizacionDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.UsuarioRegistroDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.UsuarioResponseDTO;

/**
 * Mapper entre modelos de dominio y DTOs de la web
 * INFRAESTRUCTURA - Capa web
 */
public class UsuarioDTOMapper {

    /**
     * Convierte DTO de registro a modelo de dominio
     */
    public static Usuario toDomain(UsuarioRegistroDTO dto) {
        if (dto == null) {
            return null;
        }

        return Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .rol(dto.getRol())
                .build();
    }

    /**
     * Convierte DTO de actualización a modelo de dominio
     */
    public static Usuario toDomain(UsuarioActualizacionDTO dto) {
        if (dto == null) {
            return null;
        }

        return Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    /**
     * Convierte modelo de dominio a DTO de respuesta
     */
    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .fechaRegistro(usuario.getFechaRegistro())
                .fechaActualizacion(usuario.getFechaActualizacion())
                .build();
    }
}
