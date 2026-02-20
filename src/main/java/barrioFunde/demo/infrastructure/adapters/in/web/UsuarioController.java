package barrioFunde.demo.infrastructure.adapters.in.web;

import barrioFunde.demo.application.ports.in.ActualizarUsuarioUseCase;
import barrioFunde.demo.application.ports.in.ConsultarUsuarioUseCase;
import barrioFunde.demo.application.ports.in.CrearUsuarioUseCase;
import barrioFunde.demo.domain.model.Usuario;
import barrioFunde.demo.domain.model.enums.RolUsuario;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.UsuarioActualizacionDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.UsuarioRegistroDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.UsuarioResponseDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.mapper.UsuarioDTOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar usuarios
 * INFRAESTRUCTURA - Adaptador de entrada (web)
 * Usa los casos de uso (puertos de entrada) sin conocer la implementación
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UsuarioController {

    // Inyección de PUERTOS, no de implementaciones concretas
    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final ConsultarUsuarioUseCase consultarUsuarioUseCase;
    private final ActualizarUsuarioUseCase actualizarUsuarioUseCase;

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioRegistroDTO dto) {
        try {
            log.info("Recibida solicitud de registro para email: {}", dto.getEmail());

            // Convertir DTO a dominio
            Usuario usuario = UsuarioDTOMapper.toDomain(dto);

            // Ejecutar caso de uso
            Usuario usuarioCreado = crearUsuarioUseCase.crear(usuario);

            // Convertir dominio a DTO de respuesta
            UsuarioResponseDTO response = UsuarioDTOMapper.toResponseDTO(usuarioCreado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            log.error("Error al registrar usuario: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al registrar usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al registrar usuario: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos() {
        log.info("Obteniendo todos los usuarios");

        List<UsuarioResponseDTO> usuarios = consultarUsuarioUseCase.obtenerTodos()
                .stream()
                .map(UsuarioDTOMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        log.info("Obteniendo usuario con ID: {}", id);

        return consultarUsuarioUseCase.buscarPorId(id)
                .map(usuario -> ResponseEntity.ok(UsuarioDTOMapper.toResponseDTO(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> obtenerPorEmail(@PathVariable String email) {
        log.info("Obteniendo usuario con email: {}", email);

        return consultarUsuarioUseCase.buscarPorEmail(email)
                .map(usuario -> ResponseEntity.ok(UsuarioDTOMapper.toResponseDTO(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerPorRol(@PathVariable RolUsuario rol) {
        log.info("Obteniendo usuarios con rol: {}", rol);

        List<UsuarioResponseDTO> usuarios = consultarUsuarioUseCase.obtenerPorRol(rol)
                .stream()
                .map(UsuarioDTOMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerActivos() {
        log.info("Obteniendo usuarios activos");

        List<UsuarioResponseDTO> usuarios = consultarUsuarioUseCase.obtenerActivos()
                .stream()
                .map(UsuarioDTOMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNombre(@RequestParam String nombre) {
        log.info("Buscando usuarios con nombre: {}", nombre);

        List<UsuarioResponseDTO> usuarios = consultarUsuarioUseCase.buscarPorNombre(nombre)
                .stream()
                .map(UsuarioDTOMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioActualizacionDTO dto) {
        try {
            log.info("Actualizando usuario con ID: {}", id);

            Usuario usuario = UsuarioDTOMapper.toDomain(dto);
            Usuario usuarioActualizado = actualizarUsuarioUseCase.actualizar(id, usuario);

            return ResponseEntity.ok(UsuarioDTOMapper.toResponseDTO(usuarioActualizado));

        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar usuario: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al actualizar usuario: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/rol")
    public ResponseEntity<?> cambiarRol(@PathVariable Long id, @RequestParam RolUsuario rol) {
        try {
            log.info("Cambiando rol del usuario {} a {}", id, rol);

            Usuario usuarioActualizado = actualizarUsuarioUseCase.cambiarRol(id, rol);
            return ResponseEntity.ok(UsuarioDTOMapper.toResponseDTO(usuarioActualizado));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam Boolean activo) {
        try {
            log.info("Cambiando estado del usuario {} a {}", id, activo);

            Usuario usuarioActualizado = actualizarUsuarioUseCase.cambiarEstado(id, activo);
            return ResponseEntity.ok(UsuarioDTOMapper.toResponseDTO(usuarioActualizado));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            log.info("Eliminando usuario con ID: {}", id);
            actualizarUsuarioUseCase.eliminar(id);
            return ResponseEntity.ok(new MessageResponse("Usuario eliminado exitosamente"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // Records para respuestas
    private record ErrorResponse(String mensaje) {}
    private record MessageResponse(String mensaje) {}
}