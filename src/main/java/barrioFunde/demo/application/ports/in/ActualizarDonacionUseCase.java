package barrioFunde.demo.application.ports.in;

public interface ActualizarDonacionUseCase {
    void confirmar(Long donacionId, String notasAdmin);
    void rechazar(Long donacionId, String motivo);
}
