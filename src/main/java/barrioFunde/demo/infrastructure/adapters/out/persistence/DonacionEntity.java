package barrioFunde.demo.infrastructure.adapters.out.persistence;

import barrioFunde.demo.domain.model.enums.EstadoDonacion;
import barrioFunde.demo.domain.model.enums.TipoDonacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "donaciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoDonacion tipo;

    @Column(precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(name = "detalle_especies", length = 1000)
    private String detalleEspecies;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDonacion estado;

    @Column(length = 500)
    private String comprobante;

    @Column(name = "fecha_donacion", nullable = false)
    private LocalDateTime fechaDonacion;

    @Column(name = "fecha_confirmacion")
    private LocalDateTime fechaConfirmacion;

    @Column(length = 1000)
    private String notas;

    @PrePersist
    protected void onCreate() {
        if (fechaDonacion == null) {
            fechaDonacion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoDonacion.PENDIENTE;
        }
    }
}
