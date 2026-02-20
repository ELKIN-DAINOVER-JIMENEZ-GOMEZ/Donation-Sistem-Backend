package barrioFunde.demo.application.ports.in;


import barrioFunde.demo.domain.model.Donacion;
import barrioFunde.demo.domain.model.enums.EstadoDonacion;
import barrioFunde.demo.domain.model.enums.TipoDonacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsultarDonacionUseCase {
    Optional<Donacion> buscarPorId(Long id);
    List<Donacion> listarTodas();
    List<Donacion> buscarPorUsuario(Long usuarioId);
    List<Donacion> buscarPorEstado(EstadoDonacion estado);
    List<Donacion> buscarPorTipo(TipoDonacion tipo);
    List<Donacion> buscarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    BigDecimal calcularTotalDonado(Long usuarioId);
}
