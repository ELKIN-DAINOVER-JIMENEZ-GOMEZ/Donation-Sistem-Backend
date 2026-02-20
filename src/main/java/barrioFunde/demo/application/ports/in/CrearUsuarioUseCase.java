package barrioFunde.demo.application.ports.in;

import barrioFunde.demo.domain.model.Usuario;

/**
 * Puerto de entrada - Caso de uso: Crear Usuario
 * Define QUÉ se puede hacer, no CÓMO se hace
 */
public interface CrearUsuarioUseCase {

    /**
     * Crea un nuevo usuario en el sistema
     *
     * @param usuario Usuario a crear (con password sin encriptar)
     * @return Usuario creado con ID asignado
     * @throws IllegalArgumentException si hay errores de validación o el email ya existe
     */
    Usuario crear(Usuario usuario);
}