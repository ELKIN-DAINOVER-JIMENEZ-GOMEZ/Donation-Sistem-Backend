package barrioFunde.demo.domain.model.enums;

public enum TipoDonacion {
    MONETARIA("Donación Monetaria"),
    ESPECIES("Donación en Especies"),
    SERVICIOS("Donación de Servicios");

    private final String descripcion;

    TipoDonacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}