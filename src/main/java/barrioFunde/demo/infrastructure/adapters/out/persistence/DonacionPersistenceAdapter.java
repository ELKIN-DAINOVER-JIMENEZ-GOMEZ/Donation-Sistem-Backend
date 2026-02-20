package barrioFunde.demo.infrastructure.adapters.out.persistence;

import barrioFunde.demo.application.ports.out.DonacionRepositoryPort;
import barrioFunde.demo.domain.model.Donacion;
import barrioFunde.demo.domain.model.enums.EstadoDonacion;
import barrioFunde.demo.domain.model.enums.TipoDonacion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DonacionPersistenceAdapter implements DonacionRepositoryPort {

    private final DonacionJpaRepository jpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    @Override
    public Donacion guardar(Donacion donacion) {
        UsuarioEntity usuario = usuarioJpaRepository.findById(donacion.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        DonacionEntity entity;

        if (donacion.getId() != null) {
            // Actualizar existente
            entity = jpaRepository.findById(donacion.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Donación no encontrada"));
            DonacionMapper.updateEntity(entity, donacion);
        } else {
            // Crear nueva
            entity = DonacionMapper.toEntity(donacion, usuario);
        }

        DonacionEntity saved = jpaRepository.save(entity);
        return DonacionMapper.toDomain(saved);
    }

    @Override
    public Optional<Donacion> buscarPorId(Long id) {
        return jpaRepository.findById(id)
                .map(DonacionMapper::toDomain);
    }

    @Override
    public List<Donacion> buscarTodas() {
        return jpaRepository.findAll().stream()
                .map(DonacionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Donacion> buscarPorUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream()
                .map(DonacionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Donacion> buscarPorEstado(EstadoDonacion estado) {
        return jpaRepository.findByEstado(estado).stream()
                .map(DonacionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Donacion> buscarPorTipo(TipoDonacion tipo) {
        return jpaRepository.findByTipo(tipo).stream()
                .map(DonacionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Donacion> buscarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return jpaRepository.findByFechaDonacionBetween(fechaInicio, fechaFin).stream()
                .map(DonacionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> obtenerTopDonantesPorMonto(int limite) {
        return jpaRepository.findTopDonantesPorMonto(
                PageRequest.of(0, limite)
        );
    }

    @Override
    public BigDecimal calcularTotalDonadoPorUsuario(Long usuarioId) {
        return jpaRepository.calcularTotalDonadoPorUsuario(usuarioId);
    }

    @Override
    public Long contarDonacionesPorUsuario(Long usuarioId) {
        return jpaRepository.contarDonacionesConfirmadasPorUsuario(usuarioId);
    }

    @Override
    public boolean existeDonacionPendientePorUsuario(Long usuarioId) {
        return jpaRepository.existsByUsuarioIdAndEstado(usuarioId, EstadoDonacion.PENDIENTE);
    }
}