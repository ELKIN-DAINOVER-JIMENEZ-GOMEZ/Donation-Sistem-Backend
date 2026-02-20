package barrioFunde.demo.domain.service.Auth;

import barrioFunde.demo.domain.model.Usuario;

/**
 * Reglas de negocio puras para autenticación
 * Sin dependencias de frameworks
 */
public class ReglasNegocioAuth {

    public static void validarUsuarioParaAutenticacion(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        if (!usuario.isActivo()) {
            throw new IllegalStateException("Usuario inactivo. No puede iniciar sesión");
        }
    }

    public static void validarCredenciales(String passwordIngresada, String passwordAlmacenada) {
        if (passwordIngresada == null || passwordIngresada.trim().isEmpty()) {
            throw new IllegalArgumentException("Password es obligatorio");
        }

        if (passwordAlmacenada == null || passwordAlmacenada.trim().isEmpty()) {
            throw new IllegalStateException("Usuario sin password configurado");
        }
    }
}