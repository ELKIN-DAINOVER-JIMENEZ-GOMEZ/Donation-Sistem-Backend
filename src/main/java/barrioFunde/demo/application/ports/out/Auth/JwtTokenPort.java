package barrioFunde.demo.application.ports.out.Auth;


import barrioFunde.demo.domain.model.Usuario;

/**
 * Puerto de salida: Define lo que la aplicación NECESITA para JWT
 * Interface que será implementada por el adaptador de infraestructura
 */
public interface JwtTokenPort {

    /**
     * Genera un token JWT para el usuario
     * @param usuario Usuario para el cual generar el token
     * @return Token JWT como String
     */
    String generateToken(Usuario usuario);

    /**
     * Valida un token JWT
     * @param token Token a validar
     * @return true si el token es válido, false en caso contrario
     */
    boolean validateToken(String token);

    /**
     * Extrae el email del usuario del token
     * @param token Token JWT
     * @return Email del usuario
     */
    String extractEmail(String token);

    /**
     * Extrae el ID del usuario del token
     * @param token Token JWT
     * @return ID del usuario
     */
    Long extractUserId(String token);
}