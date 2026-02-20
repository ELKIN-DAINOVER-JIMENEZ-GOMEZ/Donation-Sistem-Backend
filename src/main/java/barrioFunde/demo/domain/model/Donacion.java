package barrioFunde.demo.domain.model;

import barrioFunde.demo.domain.model.enums.EstadoDonacion;
import barrioFunde.demo.domain.model.enums.TipoDonacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo de dominio PURO - Sin anotaciones de JPA
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Donacion {

    private Long id;
    private Long usuarioId;                  // Relación con Usuario
    private TipoDonacion tipo;
    private BigDecimal monto;                // Para donaciones monetarias
    private String descripcion;              // Descripción de la donación
    private String detalleEspecies;          // Para donaciones en especies
    private EstadoDonacion estado;
    private String comprobante;              // URL o referencia del comprobante
    private LocalDateTime fechaDonacion;
    private LocalDateTime fechaConfirmacion;
    private String notas;                    // Notas del administrador

    // ========== MÉTODOS DE DOMINIO ==========

    /**
     * Valida que la donación tenga datos correctos
     */
    public void validar() {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de donación es obligatorio");
        }

        if (tipo == TipoDonacion.MONETARIA) {
            validarDonacionMonetaria();
        }

        if (tipo == TipoDonacion.ESPECIES) {
            validarDonacionEspecies();
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
    }

    private void validarDonacionMonetaria() {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        // Validar monto mínimo (ejemplo: $1.00)
        if (monto.compareTo(new BigDecimal("1.00")) < 0) {
            throw new IllegalArgumentException("El monto mínimo es $1.00");
        }

        // Validar monto máximo (ejemplo: $1,000,000)
        if (monto.compareTo(new BigDecimal("1000000.00")) > 0) {
            throw new IllegalArgumentException("El monto máximo es $1,000,000.00");
        }
    }

    private void validarDonacionEspecies() {
        if (detalleEspecies == null || detalleEspecies.trim().isEmpty()) {
            throw new IllegalArgumentException("El detalle de especies es obligatorio");
        }
    }

    /**
     * Confirma la donación (solo administrador)
     */
    public void confirmar() {
        if (estado == EstadoDonacion.CONFIRMADA) {
            throw new IllegalStateException("La donación ya está confirmada");
        }

        if (estado == EstadoDonacion.RECHAZADA) {
            throw new IllegalStateException("No se puede confirmar una donación rechazada");
        }

        this.estado = EstadoDonacion.CONFIRMADA;
        this.fechaConfirmacion = LocalDateTime.now();
    }

    /**
     * Rechaza la donación (solo administrador)
     */
    public void rechazar(String motivo) {
        if (estado == EstadoDonacion.CONFIRMADA) {
            throw new IllegalStateException("No se puede rechazar una donación confirmada");
        }

        this.estado = EstadoDonacion.RECHAZADA;
        this.notas = motivo;
    }

    /**
     * Verifica si la donación es monetaria
     */
    public boolean esMonetaria() {
        return tipo == TipoDonacion.MONETARIA;
    }

    /**
     * Verifica si la donación está confirmada
     */
    public boolean estaConfirmada() {
        return estado == EstadoDonacion.CONFIRMADA;
    }
}