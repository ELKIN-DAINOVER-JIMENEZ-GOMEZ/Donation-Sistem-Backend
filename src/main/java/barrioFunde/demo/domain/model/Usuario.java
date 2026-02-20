package barrioFunde.demo.domain.model;

import barrioFunde.demo.domain.model.enums.RolUsuario;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad de dominio Usuario
 * DOMAIN - Sin dependencias de infraestructura (JPA, Spring, etc.)
 * Modelo puro con solo reglas de negocio
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password")
@EqualsAndHashCode(of = "email")
public class Usuario {

    private Long id;
    private String nombre;
    private String email;
    private String password;

    @Builder.Default
    private RolUsuario rol = RolUsuario.DONANTE;

    @Builder.Default
    private Boolean activo = true;

    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;

    /**
     * Método de dominio: Valida que el usuario sea válido para persistir
     * @throws IllegalArgumentException si hay errores de validación
     */
    public void validar() {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (nombre.length() < 2 || nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre debe tener entre 2 y 100 caracteres");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }

        if (email.length() > 150) {
            throw new IllegalArgumentException("El email no puede exceder 150 caracteres");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
    }

    /**
     * Activa el usuario
     */
    public void activar() {
        this.activo = true;
    }

    /**
     * Desactiva el usuario
     */
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Cambia el rol del usuario
     * @param nuevoRol Nuevo rol a asignar
     */
    public void cambiarRol(RolUsuario nuevoRol) {
        if (nuevoRol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        this.rol = nuevoRol;
    }

    /**
     * Actualiza la contraseña del usuario
     * @param nuevaPassword Nueva contraseña (ya debe venir encriptada)
     */
    public void actualizarPassword(String nuevaPassword) {
        if (nuevaPassword == null || nuevaPassword.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        this.password = nuevaPassword;
    }

    /**
     * Verifica si el usuario es un líder social
     * @return true si es líder social
     */
    public boolean esLiderSocial() {
        return this.rol == RolUsuario.LIDER_SOCIAL;
    }

    /**
     * Verifica si el usuario es un administrador
     * @return true si es administrador
     */
    public boolean esAdministrador() {
        return this.rol == RolUsuario.ADMINISTRADOR;
    }

    /**
     * Verifica si el usuario es un donante
     * @return true si es donante
     */
    public boolean esDonante() {
        return this.rol == RolUsuario.DONANTE;
    }

    /**
     * Marca las fechas de registro (se usa al crear)
     */
    public void marcarFechaRegistro() {
        LocalDateTime ahora = LocalDateTime.now();
        this.fechaRegistro = ahora;
        this.fechaActualizacion = ahora;
    }

    /**
     * Marca la fecha de actualización
     */
    public void marcarFechaActualizacion() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public boolean isActivo() {
        return false;
    }
}