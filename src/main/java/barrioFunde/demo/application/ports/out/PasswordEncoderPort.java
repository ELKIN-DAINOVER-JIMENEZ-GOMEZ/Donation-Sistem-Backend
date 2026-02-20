package barrioFunde.demo.application.ports.out;

/**
 * Puerto de salida - Interfaz para encriptación de contraseñas
 * La infraestructura implementará este puerto con BCrypt o similar
 */
public interface PasswordEncoderPort {

    /**
     * Encripta una contraseña
     * @param rawPassword Contraseña sin encriptar
     * @return Contraseña encriptada
     */
    String encode(String rawPassword);

    /**
     * Verifica si una contraseña coincide con el hash
     * @param rawPassword Contraseña sin encriptar
     * @param encodedPassword Contraseña encriptada
     * @return true si coinciden
     */
    boolean matches(String rawPassword, String encodedPassword);
}