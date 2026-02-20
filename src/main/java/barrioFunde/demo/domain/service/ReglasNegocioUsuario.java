package barrioFunde.demo.domain.service;

import barrioFunde.demo.domain.model.Usuario;
import barrioFunde.demo.domain.model.enums.RolUsuario;

/**
        * Servicio de dominio para reglas de negocio de Usuario
 * DOMAIN - Sin dependencias de infraestructura
 * Contiene lógica de negocio pura
 */
public class ReglasNegocioUsuario {

    /**
     * Verifica si un usuario puede crear solicitudes
     * Regla de negocio: Solo líderes sociales y administradores pueden crear solicitudes
     *
     * @param usuario Usuario a verificar
     * @return true si puede crear solicitudes
     */
    public static boolean puedeCrearSolicitudes(Usuario usuario) {
        if (usuario == null || !usuario.getActivo()) {
            return false;
        }

        RolUsuario rol = usuario.getRol();
        return rol == RolUsuario.LIDER_SOCIAL || rol == RolUsuario.ADMINISTRADOR;
    }

    /**
     * Verifica si un usuario puede aprobar solicitudes
     * Regla de negocio: Solo administradores pueden aprobar solicitudes
     *
     * @param usuario Usuario a verificar
     * @return true si puede aprobar solicitudes
     */
    public static boolean puedeAprobarSolicitudes(Usuario usuario) {
        if (usuario == null || !usuario.getActivo()) {
            return false;
        }

        return usuario.getRol() == RolUsuario.ADMINISTRADOR;
    }

    /**
     * Verifica si un usuario puede realizar donaciones
     * Regla de negocio: Todos los usuarios activos pueden donar
     *
     * @param usuario Usuario a verificar
     * @return true si puede donar
     */
    public static boolean puedeDonar(Usuario usuario) {
        return usuario != null && usuario.getActivo();
    }

    /**
     * Verifica si un usuario puede gestionar otros usuarios
     * Regla de negocio: Solo administradores pueden gestionar usuarios
     *
     * @param usuario Usuario a verificar
     * @return true si puede gestionar usuarios
     */
    public static boolean puedeGestionarUsuarios(Usuario usuario) {
        if (usuario == null || !usuario.getActivo()) {
            return false;
        }

        return usuario.getRol() == RolUsuario.ADMINISTRADOR;
    }

    /**
     * Verifica si un usuario puede modificar a otro usuario
     * Regla de negocio: Un usuario puede modificarse a sí mismo o un admin puede modificar a cualquiera
     *
     * @param usuarioActual Usuario que intenta modificar
     * @param usuarioObjetivo Usuario a modificar
     * @return true si puede modificar
     */
    public static boolean puedeModificar(Usuario usuarioActual, Usuario usuarioObjetivo) {
        if (usuarioActual == null || usuarioObjetivo == null) {
            return false;
        }

        if (!usuarioActual.getActivo()) {
            return false;
        }

        // Puede modificarse a sí mismo
        if (usuarioActual.getEmail().equals(usuarioObjetivo.getEmail())) {
            return true;
        }

        // Admin puede modificar a cualquiera
        return usuarioActual.getRol() == RolUsuario.ADMINISTRADOR;
    }

    /**
     * Verifica si un usuario puede cambiar el rol de otro
     * Regla de negocio: Solo admins pueden cambiar roles y no pueden asignar un rol superior al suyo
     *
     * @param usuarioActual Usuario que intenta cambiar el rol
     * @param nuevoRol Nuevo rol a asignar
     * @return true si puede cambiar el rol
     */
    public static boolean puedeCambiarRol(Usuario usuarioActual, RolUsuario nuevoRol) {
        if (usuarioActual == null || nuevoRol == null) {
            return false;
        }

        if (!usuarioActual.getActivo()) {
            return false;
        }

        // Solo admins pueden cambiar roles
        if (usuarioActual.getRol() != RolUsuario.ADMINISTRADOR) {
            return false;
        }

        // No puede asignar un rol de administrador si no lo es
        return nuevoRol != RolUsuario.ADMINISTRADOR ||
                usuarioActual.getRol() == RolUsuario.ADMINISTRADOR;
    }

    /**
     * Valida que un email no esté duplicado (se usa antes de registrar)
     * Regla de negocio: Los emails deben ser únicos en el sistema
     *
     * @param emailExiste Si el email ya existe en el sistema
     * @throws IllegalArgumentException si el email ya existe
     */
    public static void validarEmailUnico(boolean emailExiste) {
        if (emailExiste) {
            throw new IllegalArgumentException("El email ya está registrado en el sistema");
        }
    }

    /**
     * Valida que se pueda cambiar el estado de un usuario
     * Regla de negocio: No se puede desactivar al último administrador del sistema
     *
     * @param usuario Usuario a cambiar estado
     * @param nuevoEstado Nuevo estado
     * @param cantidadAdminsActivos Cantidad de administradores activos
     * @throws IllegalArgumentException si viola reglas de negocio
     */
    public static void validarCambioEstado(Usuario usuario, boolean nuevoEstado,
                                           long cantidadAdminsActivos) {
        // Si se va a desactivar un admin y es el último, no permitir
        if (usuario.getRol() == RolUsuario.ADMINISTRADOR &&
                usuario.getActivo() &&
                !nuevoEstado &&
                cantidadAdminsActivos <= 1) {
            throw new IllegalArgumentException(
                    "No se puede desactivar al último administrador del sistema"
            );
        }
    }

    /**
     * Obtiene el nivel de privilegio de un rol (para comparaciones)
     *
     * @param rol Rol a evaluar
     * @return Nivel de privilegio (mayor = más privilegios)
     */
    public static int obtenerNivelPrivilegio(RolUsuario rol) {
        return switch (rol) {
            case ADMINISTRADOR -> 3;
            case LIDER_SOCIAL -> 2;
            case DONANTE -> 1;
        };
    }

    /**
     * Compara privilegios entre dos roles
     *
     * @param rol1 Primer rol
     * @param rol2 Segundo rol
     * @return true si rol1 tiene más privilegios que rol2
     */
    public static boolean tieneMasPrivilegios(RolUsuario rol1, RolUsuario rol2) {
        return obtenerNivelPrivilegio(rol1) > obtenerNivelPrivilegio(rol2);
    }
}
