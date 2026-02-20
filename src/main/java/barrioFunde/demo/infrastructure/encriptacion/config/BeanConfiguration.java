package barrioFunde.demo.infrastructure.encriptacion.config;

import barrioFunde.demo.application.ports.out.Auth.JwtTokenPort;
import barrioFunde.demo.application.ports.out.DonacionRepositoryPort;
import barrioFunde.demo.application.ports.out.PasswordEncoderPort;
import barrioFunde.demo.application.ports.out.UsuarioRepositoryPort;
import barrioFunde.demo.application.ports.usecases.Auth.AutenticacionService;
import barrioFunde.demo.application.ports.usecases.GestionDonacionService;
import barrioFunde.demo.application.ports.usecases.GestionUsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de beans de Spring
 * INFRAESTRUCTURA - Aquí es donde conectamos todo
 *
 * Este es el único lugar donde conocemos las implementaciones concretas
 * El resto del código trabaja con interfaces (puertos)
 */
@Configuration
@EnableWebSecurity
public class BeanConfiguration {

    /**
     * Bean del PasswordEncoder de Spring Security
     * Este bean será inyectado en el PasswordEncoderAdapter
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean del servicio de aplicación (casos de uso)
     * Aquí conectamos los puertos (interfaces) con los adaptadores (implementaciones)
     *
     * @param repositoryPort Puerto de salida para persistencia (implementado por UsuarioPersistenceAdapter)
     * @param passwordEncoderPort Puerto de salida para encriptación (implementado por PasswordEncoderAdapter)
     * @return Servicio de gestión de usuarios
     */
    @Bean
    public GestionUsuarioService gestionUsuarioService(
            UsuarioRepositoryPort repositoryPort,
            PasswordEncoderPort passwordEncoderPort) {

        return new GestionUsuarioService(repositoryPort, passwordEncoderPort);
    }



    /**
     * Bean para el servicio de autenticación
     */
    @Bean
    public AutenticacionService autenticacionService(
            UsuarioRepositoryPort usuarioRepositoryPort,
            PasswordEncoderPort passwordEncoderPort,
            JwtTokenPort jwtTokenPort) {

        return new AutenticacionService(
                usuarioRepositoryPort,
                passwordEncoderPort,
                jwtTokenPort
        );
    }
    //DOaciones
    @Bean
    public GestionDonacionService gestionDonacionService(
            DonacionRepositoryPort donacionRepositoryPort
    ) {
        return new GestionDonacionService(donacionRepositoryPort);
    }


}