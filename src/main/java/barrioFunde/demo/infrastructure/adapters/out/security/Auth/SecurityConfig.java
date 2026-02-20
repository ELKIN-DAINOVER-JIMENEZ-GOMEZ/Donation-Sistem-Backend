package barrioFunde.demo.infrastructure.adapters.out.security.Auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de Spring Security con JWT
 * Define qué endpoints son públicos y cuáles requieren autenticación
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // ✅ CAMBIO: Reemplaza @EnableGlobalMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/registro").permitAll()
                        .requestMatchers("/api/usuarios/**").authenticated()

                        // Cualquiera puede crear donación (autenticado)
                        .requestMatchers(HttpMethod.POST, "/api/donaciones").authenticated()

                        // Solo admin puede confirmar/rechazar
                        .requestMatchers(HttpMethod.PATCH, "/api/donaciones/*/confirmar").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/donaciones/*/rechazar").hasRole("ADMINISTRADOR")

                        // Solo admin ve todas las donaciones
                        .requestMatchers(HttpMethod.GET, "/api/donaciones").hasRole("ADMINISTRADOR")

                        // Un usuario solo puede ver SUS donaciones
                        .requestMatchers(HttpMethod.GET, "/api/donaciones/usuario/*").authenticated()

                        // El ranking es público
                        .requestMatchers(HttpMethod.GET, "/api/donaciones/ranking/**").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
