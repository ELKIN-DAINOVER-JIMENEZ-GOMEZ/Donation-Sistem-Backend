package barrioFunde.demo.infrastructure.adapters.out.persistence;

import barrioFunde.demo.domain.model.Donacion;

public class DonacionMapper {

    /**
     * Convierte Entity JPA → Modelo de Dominio
     */
    public static Donacion toDomain(DonacionEntity entity) {
        if (entity == null) {
            return null;
        }

        return Donacion.builder()
                .id(entity.getId())
                .usuarioId(entity.getUsuario().getId())
                .tipo(entity.getTipo())
                .monto(entity.getMonto())
                .descripcion(entity.getDescripcion())
                .detalleEspecies(entity.getDetalleEspecies())
                .estado(entity.getEstado())
                .comprobante(entity.getComprobante())
                .fechaDonacion(entity.getFechaDonacion())
                .fechaConfirmacion(entity.getFechaConfirmacion())
                .notas(entity.getNotas())
                .build();
    }

    /**
     * Convierte Modelo de Dominio → Entity JPA
     */
    public static DonacionEntity toEntity(Donacion donacion, UsuarioEntity usuario) {
        if (donacion == null) {
            return null;
        }

        return DonacionEntity.builder()
                .id(donacion.getId())
                .usuario(usuario)
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
     * Actualiza una entidad existente con datos del dominio
     */
    public static void updateEntity(DonacionEntity entity, Donacion donacion) {
        entity.setTipo(donacion.getTipo());
        entity.setMonto(donacion.getMonto());
        entity.setDescripcion(donacion.getDescripcion());
        entity.setDetalleEspecies(donacion.getDetalleEspecies());
        entity.setEstado(donacion.getEstado());
        entity.setComprobante(donacion.getComprobante());
        entity.setFechaDonacion(donacion.getFechaDonacion());
        entity.setFechaConfirmacion(donacion.getFechaConfirmacion());
        entity.setNotas(donacion.getNotas());
    }
}
