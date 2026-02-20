package barrioFunde.demo.application.ports.in;

import java.util.List;
import java.util.Map;

public interface ObtenerRankingDonantesUseCase {

    /**
     * Obtiene el top N de donantes por monto total
     * @param limite Número de donantes a retornar
     * @return Map con usuarioId y total donado
     */
    List<Map<String, Object>> obtenerTopDonantes(int limite);

    /**
     * Obtiene estadísticas de un donante específico
     */
    Map<String, Object> obtenerEstadisticasDonante(Long usuarioId);
}