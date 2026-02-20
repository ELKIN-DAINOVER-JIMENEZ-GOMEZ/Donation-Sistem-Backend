package barrioFunde.demo.application.ports.in;


import barrioFunde.demo.domain.model.Usuario;
import barrioFunde.demo.domain.model.enums.RolUsuario;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada - Caso de uso: Consultar Usuarios
 */
public interface ConsultarUsuarioUseCase {

    /**
     * Busca un usuario por su ID
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> buscarPorId(Long id);

    /**
     * Busca un usuario por su email
     * @param email Email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> buscarPorEmail(String email);

    /**
     * Obtiene todos los usuarios
     * @return Lista de todos los usuarios
     */
    List<Usuario> obtenerTodos();

    /**
     * Obtiene usuarios por rol
     * @param rol Rol a buscar
     * @return Lista de usuarios con ese rol
     */
    List<Usuario> obtenerPorRol(RolUsuario rol);

    /**
     * Obtiene usuarios activos
     * @return Lista de usuarios activos
     */
    List<Usuario> obtenerActivos();

    /**
     * Busca usuarios por nombre (búsqueda parcial)
     * @param nombre Nombre a buscar
     * @return Lista de usuarios que coincidan
     */
    List<Usuario> buscarPorNombre(String nombre);
}