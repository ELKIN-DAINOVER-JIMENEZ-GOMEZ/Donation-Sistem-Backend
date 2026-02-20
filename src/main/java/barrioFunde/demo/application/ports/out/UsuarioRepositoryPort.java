package barrioFunde.demo.application.ports.out;

import barrioFunde.demo.domain.model.Usuario;
import barrioFunde.demo.domain.model.enums.RolUsuario;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida - Interfaz para persistencia de Usuario
 * La infraestructura implementará este puerto
 * NO depende de JPA ni de ninguna tecnología específica
 */
public interface UsuarioRepositoryPort {

    /**
     * Guarda un usuario (crear o actualizar)
     * @param usuario Usuario a guardar
     * @return Usuario guardado con ID asignado
     */
    Usuario guardar(Usuario usuario);

    /**
     * Busca un usuario por ID
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> buscarPorId(Long id);

    /**
     * Busca un usuario por email
     * @param email Email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> buscarPorEmail(String email);

    /**
     * Verifica si existe un usuario con el email dado
     * @param email Email a verificar
     * @return true si existe
     */
    boolean existePorEmail(String email);

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
     * Obtiene usuarios por estado activo
     * @param activo Estado a buscar
     * @return Lista de usuarios con ese estado
     */
    List<Usuario> obtenerPorActivo(Boolean activo);

    /**
     * Busca usuarios por nombre (búsqueda parcial, case insensitive)
     * @param nombre Nombre a buscar
     * @return Lista de usuarios que coincidan
     */
    List<Usuario> buscarPorNombre(String nombre);

    /**
     * Cuenta usuarios por rol
     * @param rol Rol a contar
     * @return Cantidad de usuarios con ese rol
     */
    long contarPorRol(RolUsuario rol);

    /**
     * Cuenta usuarios activos por rol
     * @param rol Rol a contar
     * @return Cantidad de usuarios activos con ese rol
     */
    long contarActivosPorRol(RolUsuario rol);

    /**
     * Elimina un usuario por ID
     * @param id ID del usuario a eliminar
     */
    void eliminarPorId(Long id);

    /**
     * Verifica si existe un usuario por ID
     * @param id ID a verificar
     * @return true si existe
     */
    boolean existePorId(Long id);
}
