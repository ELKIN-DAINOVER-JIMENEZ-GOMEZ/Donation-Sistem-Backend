package barrioFunde.demo.infrastructure.adapters.in.web;

import barrioFunde.demo.application.ports.in.*;
import barrioFunde.demo.domain.model.Donacion;
import barrioFunde.demo.domain.model.Usuario;
import barrioFunde.demo.domain.model.enums.EstadoDonacion;
import barrioFunde.demo.domain.model.enums.TipoDonacion;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.DonacionRequestDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.DonacionResponseDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.RankingDonanteDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.mapper.DonacionDTOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/donaciones")
@RequiredArgsConstructor
public class DonacionController {

    private final CrearDonacionUseCase crearDonacionUseCase;
    private final ConsultarDonacionUseCase consultarDonacionUseCase;
    private final ActualizarDonacionUseCase actualizarDonacionUseCase;
    private final ObtenerRankingDonantesUseCase rankingDonantesUseCase;
    private final ConsultarUsuarioUseCase consultarUsuarioUseCase;

    /**
     * Obtiene el objeto Usuario directamente desde el SecurityContext.
     *
     * El JwtAuthenticationFilter establece el principal como un objeto Usuario:
     *   new UsernamePasswordAuthenticationToken(usuario, null, authorities)
     *
     * Por eso casteamos el principal a Usuario en lugar de llamar
     * authentication.getName() que devolvería usuario.toString() y no el email.
     */
    private Usuario getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (principal instanceof Usuario usuario) {
            // ✅ El filtro JWT guardó el objeto Usuario completo como principal
            return usuario;
        }

        // Fallback: si por alguna razón el principal es un String (email),
        // buscamos en la BD igual que antes
        String email = authentication.getName();
        return consultarUsuarioUseCase.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuario autenticado no encontrado: " + email));
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<DonacionResponseDTO> crear(@Valid @RequestBody DonacionRequestDTO dto) {
        Usuario usuarioAutenticado = getAuthenticatedUser();

        Donacion donacion = DonacionDTOMapper.toDomain(dto);
        donacion.setUsuarioId(usuarioAutenticado.getId());

        Donacion creada = crearDonacionUseCase.crear(donacion);

        DonacionResponseDTO response = DonacionDTOMapper.toResponseDTO(
                creada,
                usuarioAutenticado.getNombre(),
                usuarioAutenticado.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<DonacionResponseDTO> buscarPorId(@PathVariable Long id) {
        return consultarDonacionUseCase.buscarPorId(id)
                .map(donacion -> {
                    Usuario usuario = consultarUsuarioUseCase.buscarPorId(donacion.getUsuarioId())
                            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

                    return ResponseEntity.ok(DonacionDTOMapper.toResponseDTO(
                            donacion,
                            usuario.getNombre(),
                            usuario.getEmail()
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DonacionResponseDTO>> listarTodas() {
        List<Donacion> donaciones = consultarDonacionUseCase.listarTodas();

        List<DonacionResponseDTO> response = donaciones.stream()
                .map(donacion -> {
                    Usuario usuario = consultarUsuarioUseCase.buscarPorId(donacion.getUsuarioId())
                            .orElse(null);

                    return DonacionDTOMapper.toResponseDTO(
                            donacion,
                            usuario != null ? usuario.getNombre() : "Desconocido",
                            usuario != null ? usuario.getEmail() : ""
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Mis donaciones — usa el usuario del SecurityContext directamente,
     * sin necesidad de pasar el ID por la URL.
     */
    @GetMapping("/mis-donaciones")
    public ResponseEntity<List<DonacionResponseDTO>> listarMisDonaciones() {
        Usuario usuarioAutenticado = getAuthenticatedUser();

        List<Donacion> donaciones = consultarDonacionUseCase.buscarPorUsuario(usuarioAutenticado.getId());

        List<DonacionResponseDTO> response = donaciones.stream()
                .map(donacion -> DonacionDTOMapper.toResponseDTO(
                        donacion,
                        usuarioAutenticado.getNombre(),
                        usuarioAutenticado.getEmail()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DonacionResponseDTO>> buscarPorUsuario(@PathVariable Long usuarioId) {
        List<Donacion> donaciones = consultarDonacionUseCase.buscarPorUsuario(usuarioId);

        Usuario usuario = consultarUsuarioUseCase.buscarPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<DonacionResponseDTO> response = donaciones.stream()
                .map(donacion -> DonacionDTOMapper.toResponseDTO(
                        donacion,
                        usuario.getNombre(),
                        usuario.getEmail()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<DonacionResponseDTO>> buscarPorEstado(@PathVariable EstadoDonacion estado) {
        List<Donacion> donaciones = consultarDonacionUseCase.buscarPorEstado(estado);

        List<DonacionResponseDTO> response = donaciones.stream()
                .map(donacion -> {
                    Usuario usuario = consultarUsuarioUseCase.buscarPorId(donacion.getUsuarioId())
                            .orElse(null);

                    return DonacionDTOMapper.toResponseDTO(
                            donacion,
                            usuario != null ? usuario.getNombre() : "Desconocido",
                            usuario != null ? usuario.getEmail() : ""
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<DonacionResponseDTO>> buscarPorTipo(@PathVariable TipoDonacion tipo) {
        List<Donacion> donaciones = consultarDonacionUseCase.buscarPorTipo(tipo);

        List<DonacionResponseDTO> response = donaciones.stream()
                .map(donacion -> {
                    Usuario usuario = consultarUsuarioUseCase.buscarPorId(donacion.getUsuarioId())
                            .orElse(null);

                    return DonacionDTOMapper.toResponseDTO(
                            donacion,
                            usuario != null ? usuario.getNombre() : "Desconocido",
                            usuario != null ? usuario.getEmail() : ""
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<DonacionResponseDTO>> buscarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        List<Donacion> donaciones = consultarDonacionUseCase.buscarPorFechas(inicio, fin);

        List<DonacionResponseDTO> response = donaciones.stream()
                .map(donacion -> {
                    Usuario usuario = consultarUsuarioUseCase.buscarPorId(donacion.getUsuarioId())
                            .orElse(null);

                    return DonacionDTOMapper.toResponseDTO(
                            donacion,
                            usuario != null ? usuario.getNombre() : "Desconocido",
                            usuario != null ? usuario.getEmail() : ""
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{usuarioId}/total")
    public ResponseEntity<Map<String, BigDecimal>> calcularTotalDonado(@PathVariable Long usuarioId) {
        BigDecimal total = consultarDonacionUseCase.calcularTotalDonado(usuarioId);
        return ResponseEntity.ok(Map.of("totalDonado", total));
    }

    // ── ADMIN ─────────────────────────────────────────────────────────────────

    @PatchMapping("/{id}/confirmar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> confirmar(
            @PathVariable Long id,
            @RequestParam(required = false) String notas
    ) {
        actualizarDonacionUseCase.confirmar(id, notas);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> rechazar(
            @PathVariable Long id,
            @RequestParam String motivo
    ) {
        actualizarDonacionUseCase.rechazar(id, motivo);
        return ResponseEntity.ok().build();
    }

    // ── RANKING ───────────────────────────────────────────────────────────────

    @GetMapping("/ranking/top")
    public ResponseEntity<List<RankingDonanteDTO>> obtenerTopDonantes(
            @RequestParam(defaultValue = "10") int limite
    ) {
        List<Map<String, Object>> ranking = rankingDonantesUseCase.obtenerTopDonantes(limite);

        List<RankingDonanteDTO> response = IntStream.range(0, ranking.size())
                .mapToObj(i -> DonacionDTOMapper.toRankingDTO(ranking.get(i), i + 1))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranking/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasDonante(@PathVariable Long usuarioId) {
        Map<String, Object> estadisticas = rankingDonantesUseCase.obtenerEstadisticasDonante(usuarioId);
        return ResponseEntity.ok(estadisticas);
    }
}