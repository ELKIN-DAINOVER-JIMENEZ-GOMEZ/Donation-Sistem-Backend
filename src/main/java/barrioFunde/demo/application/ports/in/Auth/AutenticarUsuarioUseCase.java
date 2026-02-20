package barrioFunde.demo.application.ports.in.Auth;

import barrioFunde.demo.domain.model.Auth.AuthCredentials;
import barrioFunde.demo.domain.model.Auth.AuthToken;

/**
 * Puerto de entrada: Define el caso de uso de autenticación
 * Interface que define QUÉ puede hacer la aplicación
 */
public interface AutenticarUsuarioUseCase {

    /**
     * Autentica un usuario con sus credenciales
     * @param credentials Credenciales del usuario (email y password)
     * @return Token de autenticación con información del usuario
     * @throws IllegalArgumentException si las credenciales son inválidas
     */
    AuthToken autenticar(AuthCredentials credentials);
}