package barrioFunde.demo.application.ports.usecases;

import barrioFunde.demo.application.ports.in.ActualizarDonacionUseCase;
import barrioFunde.demo.application.ports.in.ConsultarDonacionUseCase;
import barrioFunde.demo.application.ports.in.CrearDonacionUseCase;
import barrioFunde.demo.application.ports.in.ObtenerRankingDonantesUseCase;
import barrioFunde.demo.application.ports.out.DonacionRepositoryPort;
import barrioFunde.demo.domain.model.Donacion;
import barrioFunde.demo.domain.model.enums.EstadoDonacion;
import barrioFunde.demo.domain.model.enums.TipoDonacion;
import barrioFunde.demo.domain.service.ReglasNegocioDonacion;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class GestionDonacionService implements
        CrearDonacionUseCase,
        ConsultarDonacionUseCase,
        ActualizarDonacionUseCase,
        ObtenerRankingDonantesUseCase {

    private final DonacionRepositoryPort repositoryPort;

    // ========== CREAR ==========

    @Override
    public Donacion crear(Donacion donacion) {
        // 1. Validar modelo de dominio
        donacion.validar();

        // 2. Validar que no tenga donación pendiente
        boolean tieneDonacionPendiente = repositoryPort
                .existeDonacionPendientePorUsuario(donacion.getUsuarioId());
        ReglasNegocioDonacion.validarDonacionNoDuplicada(tieneDonacionPendiente);

        // 3. Establecer valores por defecto
        if (donacion.getEstado() == null) {
            donacion.setEstado(EstadoDonacion.PENDIENTE);
        }

        if (donacion.getFechaDonacion() == null) {
            donacion.setFechaDonacion(LocalDateTime.now());
        }

        // 4. Guardar
        return repositoryPort.guardar(donacion);
    }

    // ========== CONSULTAR ==========

    @Override
    public Optional<Donacion> buscarPorId(Long id) {
        return repositoryPort.buscarPorId(id);
    }

    @Override
    public List<Donacion> listarTodas() {
        return repositoryPort.buscarTodas();
    }

    @Override
    public List<Donacion> buscarPorUsuario(Long usuarioId) {
        return repositoryPort.buscarPorUsuarioId(usuarioId);
    }

    @Override
    public List<Donacion> buscarPorEstado(EstadoDonacion estado) {
        return repositoryPort.buscarPorEstado(estado);
    }

    @Override
    public List<Donacion> buscarPorTipo(TipoDonacion tipo) {
        return repositoryPort.buscarPorTipo(tipo);
    }

    @Override
    public List<Donacion> buscarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return repositoryPort.buscarPorFechas(fechaInicio, fechaFin);
    }

    @Override
    public BigDecimal calcularTotalDonado(Long usuarioId) {
        return repositoryPort.calcularTotalDonadoPorUsuario(usuarioId);
    }

    // ========== ACTUALIZAR ==========
    @CacheEvict(value = "rankingDonantes", allEntries = true)
    @Override
    public void confirmar(Long donacionId, String notasAdmin) {
        Donacion donacion = repositoryPort.buscarPorId(donacionId)
                .orElseThrow(() -> new IllegalArgumentException("Donación no encontrada"));

        // Método de dominio
        donacion.confirmar();

        if (notasAdmin != null && !notasAdmin.trim().isEmpty()) {
            donacion.setNotas(notasAdmin);
        }

        repositoryPort.guardar(donacion);
    }

    @Override
    public void rechazar(Long donacionId, String motivo) {
        Donacion donacion = repositoryPort.buscarPorId(donacionId)
                .orElseThrow(() -> new IllegalArgumentException("Donación no encontrada"));

        // Método de dominio
        donacion.rechazar(motivo);

        repositoryPort.guardar(donacion);
    }

    // ========== RANKING ==========

    // Cachea el ranking por 1 hora
    @Cacheable(value = "rankingDonantes", key = "#limite")
    @Override
    public List<Map<String, Object>> obtenerTopDonantes(int limite) {
        return repositoryPort.obtenerTopDonantesPorMonto(limite);
    }

    @Override
    public Map<String, Object> obtenerEstadisticasDonante(Long usuarioId) {
        BigDecimal totalDonado = repositoryPort.calcularTotalDonadoPorUsuario(usuarioId);
        Long cantidadDonaciones = repositoryPort.contarDonacionesPorUsuario(usuarioId);

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("usuarioId", usuarioId);
        estadisticas.put("totalDonado", totalDonado);
        estadisticas.put("cantidadDonaciones", cantidadDonaciones);

        if (cantidadDonaciones > 0) {
            BigDecimal promedio = totalDonado.divide(
                    new BigDecimal(cantidadDonaciones),
                    2,
                    BigDecimal.ROUND_HALF_UP
            );
            estadisticas.put("promediodonacion", promedio);
        } else {
            estadisticas.put("promedioDonacion", BigDecimal.ZERO);
        }

        return estadisticas;
    }
}
