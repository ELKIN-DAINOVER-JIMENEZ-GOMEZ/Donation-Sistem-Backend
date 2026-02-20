package barrioFunde.demo.domain.model.enums;

/**
 * Enumeración de roles de usuario en el sistema de donaciones
 * DOMAIN - Sin dependencias externas
 */
public enum RolUsuario {
    /**
     * Usuario que realiza donaciones
     */
    DONANTE,

    /**
     * Usuario que crea y gestiona solicitudes de mejoras
     */
    LIDER_SOCIAL,

    /**
     * Usuario con privilegios administrativos del sistema
     */
    ADMINISTRADOR
}