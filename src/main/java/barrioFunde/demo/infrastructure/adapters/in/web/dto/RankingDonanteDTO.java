package barrioFunde.demo.infrastructure.adapters.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingDonanteDTO {
    private Integer posicion;
    private Long usuarioId;
    private String nombre;
    private String email;
    private BigDecimal totalDonado;
    private Long cantidadDonaciones;
    private BigDecimal promedioDonacion;
}