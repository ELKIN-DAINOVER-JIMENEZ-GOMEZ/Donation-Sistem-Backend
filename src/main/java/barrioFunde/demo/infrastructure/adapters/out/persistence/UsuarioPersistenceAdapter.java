package barrioFunde.demo.infrastructure.adapters.out.persistence;


import barrioFunde.demo.application.ports.out.UsuarioRepositoryPort;
import barrioFunde.demo.domain.model.Usuario;
import barrioFunde.demo.domain.model.enums.RolUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia - Implementa el puerto UsuarioRepositoryPort
 * INFRAESTRUCTURA - Conecta el dominio con JPA/PostgreSQL
 *
 * Este adaptador traduce entre:
 * - Usuario (modelo de dominio)
 * - UsuarioEntity (entidad JPA)
 */
@Component
@RequiredArgsConstructor
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpaRepository;

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
        UsuarioEntity savedEntity = jpaRepository.save(entity);
        return UsuarioMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpaRepository.findById(id)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(UsuarioMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> obtenerPorRol(RolUsuario rol) {
        return jpaRepository.findByRol(rol)
                .stream()
                .map(UsuarioMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> obtenerPorActivo(Boolean activo) {
        return jpaRepository.findByActivo(activo)
                .stream()
                .map(UsuarioMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> buscarPorNombre(String nombre) {
        return jpaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(UsuarioMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long contarPorRol(RolUsuario rol) {
        return jpaRepository.countByRol(rol);
    }

    @Override
    public long contarActivosPorRol(RolUsuario rol) {
        return jpaRepository.countByRolAndActivoTrue(rol);
    }

    @Override
    public void eliminarPorId(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return jpaRepository.existsById(id);
    }
}