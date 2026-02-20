package barrioFunde.demo.application.ports.in;

import barrioFunde.demo.domain.model.Usuario;
import barrioFunde.demo.domain.model.enums.RolUsuario;

/**
 * Puerto de entrada - Caso de uso: Actualizar Usuario
 */
public interface ActualizarUsuarioUseCase {

    /**
     * Actualiza la información de un usuario
     * @param id ID del usuario a actualizar
     * @param usuarioActualizado Datos actualizados
     * @return Usuario actualizado
     */
    Usuario actualizar(Long id, Usuario usuarioActualizado);

    /**
     * Cambia el rol de un usuario
     * @param id ID del usuario
     * @param nuevoRol Nuevo rol a asignar
     * @return Usuario con rol actualizado
     */
    Usuario cambiarRol(Long id, RolUsuario nuevoRol);

    /**
     * Cambia el estado activo de un usuario
     * @param id ID del usuario
     * @param activo Nuevo estado
     * @return Usuario actualizado
     */
    Usuario cambiarEstado(Long id, Boolean activo);

    /**
     * Elimina un usuario (desactivación lógica)
     * @param id ID del usuario
     */
    void eliminar(Long id);
}

