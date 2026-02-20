package barrioFunde.demo.domain.service;


import barrioFunde.demo.domain.model.Usuario;
import barrioFunde.demo.domain.model.enums.RolUsuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Reglas de negocio puras del dominio de Donaciones
 */
public class ReglasNegocioDonacion {

    /**
     * Verifica si un usuario puede realizar donaciones
     */
    public static boolean puedeDonar(Usuario usuario) {
        if (usuario == null) {
            return false;
        }

        // El usuario debe estar activo
        if (!usuario.isActivo()) {
            return false;
        }

        // Todos los roles pueden donar excepto tal vez alguno específico
        return true;
    }

    /**
     * Verifica si un usuario puede confirmar/rechazar donaciones
     */
    public static boolean puedeGestionarDonaciones(Usuario usuario) {
        if (usuario == null) {
            return false;
        }

        return usuario.getRol() == RolUsuario.ADMINISTRADOR;
    }

    /**
     * Calcula si una donación califica como "grande"
     */
    public static boolean esDonacionGrande(BigDecimal monto) {
        // Consideramos "grande" si es mayor a $10,000
        return monto != null && monto.compareTo(new BigDecimal("10000.00")) > 0;
    }

    /**
     * Verifica si una donación es reciente (últimas 24 horas)
     */
    public static boolean esDonacionReciente(LocalDateTime fechaDonacion) {
        if (fechaDonacion == null) {
            return false;
        }

        LocalDateTime hace24Horas = LocalDateTime.now().minusHours(24);
        return fechaDonacion.isAfter(hace24Horas);
    }

    /**
     * Valida que un usuario no haga donaciones duplicadas
     */
    public static void validarDonacionNoDuplicada(boolean existeDonacionReciente) {
        if (existeDonacionReciente) {
            throw new IllegalStateException(
                    "Ya existe una donación pendiente. Por favor espere a que sea confirmada."
            );
        }
    }
}