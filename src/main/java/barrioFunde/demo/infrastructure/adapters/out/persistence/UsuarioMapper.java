package barrioFunde.demo.infrastructure.adapters.out.persistence;

import barrioFunde.demo.domain.model.Usuario;

/**
 * Mapper para convertir entre Usuario (dominio) y UsuarioEntity (JPA)
 * INFRAESTRUCTURA - Convierte entre capas
 */
public class UsuarioMapper {

    /**
     * Convierte de modelo de dominio a entidad JPA
     * @param usuario Modelo de dominio
     * @return Entidad JPA
     */
    public static UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioEntity.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .password(usuario.getPassword())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .fechaRegistro(usuario.getFechaRegistro())
                .fechaActualizacion(usuario.getFechaActualizacion())
                .build();
    }

    /**
     * Convierte de entidad JPA a modelo de dominio
     * @param entity Entidad JPA
     * @return Modelo de dominio
     */
    public static Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        return Usuario.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .rol(entity.getRol())
                .activo(entity.getActivo())
                .fechaRegistro(entity.getFechaRegistro())
                .fechaActualizacion(entity.getFechaActualizacion())
                .build();
    }
}
