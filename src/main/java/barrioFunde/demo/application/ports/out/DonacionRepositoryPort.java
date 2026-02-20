package barrioFunde.demo.application.ports.out;

import barrioFunde.demo.domain.model.Donacion;
import barrioFunde.demo.domain.model.enums.EstadoDonacion;
import barrioFunde.demo.domain.model.enums.TipoDonacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DonacionRepositoryPort {

    // CRUD Básico
    Donacion guardar(Donacion donacion);
    Optional<Donacion> buscarPorId(Long id);
    List<Donacion> buscarTodas();
    void eliminar(Long id);

    // Consultas específicas
    List<Donacion> buscarPorUsuarioId(Long usuarioId);
    List<Donacion> buscarPorEstado(EstadoDonacion estado);
    List<Donacion> buscarPorTipo(TipoDonacion tipo);
    List<Donacion> buscarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Para ranking
    List<Map<String, Object>> obtenerTopDonantesPorMonto(int limite);
    BigDecimal calcularTotalDonadoPorUsuario(Long usuarioId);
    Long contarDonacionesPorUsuario(Long usuarioId);

    // Validaciones
    boolean existeDonacionPendientePorUsuario(Long usuarioId);
}
