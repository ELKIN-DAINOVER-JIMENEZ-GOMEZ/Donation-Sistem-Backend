package barrioFunde.demo.domain.model.enums;

public enum EstadoDonacion {
    PENDIENTE("Pendiente de confirmación"),
    CONFIRMADA("Confirmada"),
    RECHAZADA("Rechazada");

    private final String descripcion;

    EstadoDonacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}