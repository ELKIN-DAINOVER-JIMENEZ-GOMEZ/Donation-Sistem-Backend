package barrioFunde.demo.infrastructure.adapters.out.persistence;

import barrioFunde.demo.domain.model.enums.EstadoDonacion;
import barrioFunde.demo.domain.model.enums.TipoDonacion;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface DonacionJpaRepository extends JpaRepository<DonacionEntity, Long> {

    // Buscar por usuario
    List<DonacionEntity> findByUsuarioId(Long usuarioId);

    // Buscar por estado
    List<DonacionEntity> findByEstado(EstadoDonacion estado);

    // Buscar por tipo
    List<DonacionEntity> findByTipo(TipoDonacion tipo);

    // Buscar por rango de fechas
    @Query("SELECT d FROM DonacionEntity d WHERE d.fechaDonacion BETWEEN :inicio AND :fin")
    List<DonacionEntity> findByFechaDonacionBetween(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    // ========== RANKING Y ESTADÍSTICAS ==========

    /**
     * Top donantes por monto total (solo donaciones confirmadas)
     */
    @Query("""
        SELECT 
            d.usuario.id as usuarioId,
            d.usuario.nombre as nombre,
            d.usuario.email as email,
            SUM(d.monto) as totalDonado,
            COUNT(d) as cantidadDonaciones
        FROM DonacionEntity d
        WHERE d.estado = 'CONFIRMADA' 
        AND d.tipo = 'MONETARIA'
        GROUP BY d.usuario.id, d.usuario.nombre, d.usuario.email
        ORDER BY SUM(d.monto) DESC
    """)
    List<Map<String, Object>> findTopDonantesPorMonto(@Param("limite") PageRequest limite);

    /**
     * Calcular total donado por un usuario
     */
    @Query("""
        SELECT COALESCE(SUM(d.monto), 0)
        FROM DonacionEntity d
        WHERE d.usuario.id = :usuarioId
        AND d.estado = 'CONFIRMADA'
        AND d.tipo = 'MONETARIA'
    """)
    BigDecimal calcularTotalDonadoPorUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * Contar donaciones de un usuario
     */
    @Query("SELECT COUNT(d) FROM DonacionEntity d WHERE d.usuario.id = :usuarioId AND d.estado = 'CONFIRMADA'")
    Long contarDonacionesConfirmadasPorUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * Verificar si tiene donación pendiente
     */
    boolean existsByUsuarioIdAndEstado(Long usuarioId, EstadoDonacion estado);
}
