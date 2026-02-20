package barrioFunde.demo.infrastructure.adapters.in.web.dto;

import barrioFunde.demo.domain.model.enums.EstadoDonacion;
import barrioFunde.demo.domain.model.enums.TipoDonacion;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class DonacionResponseDTO {
    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private String emailUsuario;
    private TipoDonacion tipo;
    private BigDecimal monto;
    private String descripcion;
    private String detalleEspecies;
    private EstadoDonacion estado;
    private String comprobante;
    private LocalDateTime fechaDonacion;
    private LocalDateTime fechaConfirmacion;
    private String notas;
}
