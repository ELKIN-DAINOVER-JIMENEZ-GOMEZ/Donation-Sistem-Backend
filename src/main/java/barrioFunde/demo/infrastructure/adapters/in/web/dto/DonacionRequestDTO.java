package barrioFunde.demo.infrastructure.adapters.in.web.dto;

import barrioFunde.demo.domain.model.enums.TipoDonacion;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DonacionRequestDTO {

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El tipo de donación es obligatorio")
    private TipoDonacion tipo;

    @DecimalMin(value = "1.00", message = "El monto mínimo es $1.00")
    @DecimalMax(value = "1000000.00", message = "El monto máximo es $1,000,000.00")
    private BigDecimal monto;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Size(max = 1000, message = "El detalle de especies no puede exceder 1000 caracteres")
    private String detalleEspecies;

    @Size(max = 500, message = "El comprobante no puede exceder 500 caracteres")
    private String comprobante;
}
