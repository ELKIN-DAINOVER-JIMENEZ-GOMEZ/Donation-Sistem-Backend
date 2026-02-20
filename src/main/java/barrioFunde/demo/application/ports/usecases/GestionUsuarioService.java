package barrioFunde.demo.application.ports.usecases;

import barrioFunde.demo.application.ports.in.ActualizarUsuarioUseCase;
import barrioFunde.demo.application.ports.in.ConsultarUsuarioUseCase;
import barrioFunde.demo.application.ports.in.CrearUsuarioUseCase;
import barrioFunde.demo.application.ports.out.PasswordEncoderPort;
import barrioFunde.demo.application.ports.out.UsuarioRepositoryPort;
import barrioFunde.demo.domain.model.Usuario;
import barrioFunde.demo.domain.model.enums.RolUsuario;
import barrioFunde.demo.domain.service.ReglasNegocioUsuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación - Gestión de Usuarios
 * Implementa todos los casos de uso relacionados con usuarios
 * Orquesta las reglas de negocio del dominio y las llamadas a puertos
 */
@RequiredArgsConstructor
@Slf4j
public class GestionUsuarioService implements
        CrearUsuarioUseCase,
        ConsultarUsuarioUseCase,
        ActualizarUsuarioUseCase {

    private final UsuarioRepositoryPort repositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    // ============================================================
    // IMPLEMENTACIÓN DE CrearUsuarioUseCase
    // ============================================================

    @Override
    public Usuario crear(Usuario usuario) {
        log.info("Creando usuario con email: {}", usuario.getEmail());

        // 1. Validar el usuario (dominio)
        usuario.validar();

        // 2. Verificar que el email no exista (regla de negocio)
        boolean emailExiste = repositoryPort.existePorEmail(usuario.getEmail());
        ReglasNegocioUsuario.validarEmailUnico(emailExiste);

        // 3. Encriptar la contraseña
        String passwordEncriptada = passwordEncoderPort.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);

        // 4. Establecer valores por defecto si no están definidos
        if (usuario.getRol() == null) {
            usuario.setRol(RolUsuario.DONANTE);
        }
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        // 5. Marcar fecha de registro
        usuario.marcarFechaRegistro();

        // 6. Guardar en la base de datos
        Usuario usuarioGuardado = repositoryPort.guardar(usuario);

        log.info("Usuario creado exitosamente con ID: {}", usuarioGuardado.getId());
        return usuarioGuardado;
    }

    // ============================================================
    // IMPLEMENTACIÓN DE ConsultarUsuarioUseCase
    // ============================================================

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        log.debug("Buscando usuario con ID: {}", id);
        return repositoryPort.buscarPorId(id);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        log.debug("Buscando usuario con email: {}", email);
        return repositoryPort.buscarPorEmail(email);
    }

    @Override
    public List<Usuario> obtenerTodos() {
        log.debug("Obteniendo todos los usuarios");
        return repositoryPort.obtenerTodos();
    }

    @Override
    public List<Usuario> obtenerPorRol(RolUsuario rol) {
        log.debug("Obteniendo usuarios con rol: {}", rol);
        return repositoryPort.obtenerPorRol(rol);
    }

    @Override
    public List<Usuario> obtenerActivos() {
        log.debug("Obteniendo usuarios activos");
        return repositoryPort.obtenerPorActivo(true);
    }

    @Override
    public List<Usuario> buscarPorNombre(String nombre) {
        log.debug("Buscando usuarios con nombre: {}", nombre);
        return repositoryPort.buscarPorNombre(nombre);
    }

    // ============================================================
    // IMPLEMENTACIÓN DE ActualizarUsuarioUseCase
    // ============================================================

    @Override
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        log.info("Actualizando usuario con ID: {}", id);

        // 1. Buscar el usuario existente
        Usuario usuarioExistente = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));

        // 2. Si cambió el email, verificar que no exista otro usuario con ese email
        if (!usuarioExistente.getEmail().equals(usuarioActualizado.getEmail())) {
            if (repositoryPort.existePorEmail(usuarioActualizado.getEmail())) {
                throw new IllegalArgumentException("El email ya está registrado");
            }
        }

        // 3. Actualizar campos
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());

        // 4. Solo actualizar password si se proporciona una nueva
        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isBlank()) {
            String passwordEncriptada = passwordEncoderPort.encode(usuarioActualizado.getPassword());
            usuarioExistente.actualizarPassword(passwordEncriptada);
        }

        // 5. Marcar fecha de actualización
        usuarioExistente.marcarFechaActualizacion();

        // 6. Validar y guardar
        usuarioExistente.validar();
        Usuario usuarioGuardado = repositoryPort.guardar(usuarioExistente);

        log.info("Usuario actualizado exitosamente con ID: {}", usuarioGuardado.getId());
        return usuarioGuardado;
    }

    @Override
    public Usuario cambiarRol(Long id, RolUsuario nuevoRol) {
        log.info("Cambiando rol del usuario {} a {}", id, nuevoRol);

        // 1. Buscar usuario
        Usuario usuario = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));

        // 2. Cambiar rol (método del dominio)
        usuario.cambiarRol(nuevoRol);

        // 3. Marcar actualización y guardar
        usuario.marcarFechaActualizacion();
        return repositoryPort.guardar(usuario);
    }

    @Override
    public Usuario cambiarEstado(Long id, Boolean activo) {
        log.info("Cambiando estado del usuario {} a {}", id, activo);

        // 1. Buscar usuario
        Usuario usuario = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));

        // 2. Validar regla de negocio (no desactivar último admin)
        if (usuario.getRol() == RolUsuario.ADMINISTRADOR) {
            long cantidadAdminsActivos = repositoryPort.contarActivosPorRol(RolUsuario.ADMINISTRADOR);
            ReglasNegocioUsuario.validarCambioEstado(usuario, activo, cantidadAdminsActivos);
        }

        // 3. Cambiar estado
        if (activo) {
            usuario.activar();
        } else {
            usuario.desactivar();
        }

        // 4. Marcar actualización y guardar
        usuario.marcarFechaActualizacion();
        return repositoryPort.guardar(usuario);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando (desactivando) usuario con ID: {}", id);
        cambiarEstado(id, false);
    }

    // ============================================================
    // MÉTODOS ADICIONALES ÚTILES
    // ============================================================

    /**
     * Valida las credenciales de un usuario
     * @param email Email del usuario
     * @param password Contraseña sin encriptar
     * @return Optional con el usuario si las credenciales son válidas
     */
    public Optional<Usuario> validarCredenciales(String email, String password) {
        log.debug("Validando credenciales para email: {}", email);

        Optional<Usuario> usuarioOpt = repositoryPort.buscarPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            log.warn("Usuario no encontrado con email: {}", email);
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getActivo()) {
            log.warn("Usuario inactivo: {}", email);
            return Optional.empty();
        }

        if (passwordEncoderPort.matches(password, usuario.getPassword())) {
            log.info("Credenciales válidas para usuario: {}", email);
            return Optional.of(usuario);
        }

        log.warn("Credenciales inválidas para email: {}", email);
        return Optional.empty();
    }

    /**
     * Verifica si un email ya está registrado
     * @param email Email a verificar
     * @return true si existe
     */
    public boolean emailExiste(String email) {
        return repositoryPort.existePorEmail(email);
    }

    /**
     * Cuenta usuarios por rol
     * @param rol Rol a contar
     * @return Cantidad de usuarios
     */
    public long contarPorRol(RolUsuario rol) {
        return repositoryPort.contarPorRol(rol);
    }
}