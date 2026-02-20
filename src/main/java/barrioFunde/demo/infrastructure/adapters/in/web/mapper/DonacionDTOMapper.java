package barrioFunde.demo.infrastructure.adapters.in.web.mapper;


import barrioFunde.demo.domain.model.Donacion;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.DonacionRequestDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.DonacionResponseDTO;
import barrioFunde.demo.infrastructure.adapters.in.web.dto.RankingDonanteDTO;

import java.math.BigDecimal;
import java.util.Map;

public class DonacionDTOMapper {

    /**
     * Convierte DTO Request → Dominio
     */
    public static Donacion toDomain(DonacionRequestDTO dto) {
        return Donacion.builder()
                .usuarioId(dto.getUsuarioId())
                .tipo(dto.getTipo())
                .monto(dto.getMonto())
                .descripcion(dto.getDescripcion())
                .detalleEspecies(dto.getDetalleEspecies())
                .comprobante(dto.getComprobante())
                .build();
    }

    /**
     * Convierte Dominio → DTO Response
     */
    public static DonacionResponseDTO toResponseDTO(Donacion donacion, String nombreUsuario, String emailUsuario) {
        return DonacionResponseDTO.builder()
                .id(donacion.getId())
                .usuarioId(donacion.getUsuarioId())
                .nombreUsuario(nombreUsuario)
                .emailUsuario(emailUsuario)
                .tipo(donacion.getTipo())
                .monto(donacion.getMonto())
                .descripcion(donacion.getDescripcion())
                .detalleEspecies(donacion.getDetalleEspecies())
                .estado(donacion.getEstado())
                .comprobante(donacion.getComprobante())
                .fechaDonacion(donacion.getFechaDonacion())
                .fechaConfirmacion(donacion.getFechaConfirmacion())
                .notas(donacion.getNotas())
                .build();
    }

    /**
     * Convierte Map → Ranking DTO
     */
    public static RankingDonanteDTO toRankingDTO(Map<String, Object> data, int posicion) {
        BigDecimal totalDonado = (BigDecimal) data.get("totalDonado");
        Long cantidadDonaciones = ((Number) data.get("cantidadDonaciones")).longValue();

        BigDecimal promedio = BigDecimal.ZERO;
        if (cantidadDonaciones > 0) {
            promedio = totalDonado.divide(
                    new BigDecimal(cantidadDonaciones),
                    2,
                    BigDecimal.ROUND_HALF_UP
            );
        }

        return RankingDonanteDTO.builder()
                .posicion(posicion)
                .usuarioId(((Number) data.get("usuarioId")).longValue())
                .nombre((String) data.get("nombre"))
                .email((String) data.get("email"))
                .totalDonado(totalDonado)
                .cantidadDonaciones(cantidadDonaciones)
                .promedioDonacion(promedio)
                .build();
    }
}
