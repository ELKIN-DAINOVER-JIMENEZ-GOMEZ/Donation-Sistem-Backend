package barrioFunde.demo.infrastructure.adapters.out.persistence;

import barrioFunde.demo.domain.model.enums.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA de Spring Data
 * INFRAESTRUCTURA - Tecnología específica (Spring Data JPA)
 */
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    List<UsuarioEntity> findByRol(RolUsuario rol);

    List<UsuarioEntity> findByActivo(Boolean activo);

    List<UsuarioEntity> findByNombreContainingIgnoreCase(String nombre);

    long countByRol(RolUsuario rol);

    @Query("SELECT COUNT(u) FROM UsuarioEntity u WHERE u.rol = :rol AND u.activo = true")
    long countByRolAndActivoTrue(@Param("rol") RolUsuario rol);

    List<UsuarioEntity> findByRolAndActivo(RolUsuario rol, Boolean activo);
}