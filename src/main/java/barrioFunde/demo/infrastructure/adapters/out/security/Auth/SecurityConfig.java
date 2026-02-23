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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración de Spring Security con JWT
 * Define qué endpoints son públicos y cuáles requieren autenticación
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ── CORS debe ir ANTES que CSRF y que el resto de reglas ──────────
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        // Preflight OPTIONS: siempre libre, sin token
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/registro").permitAll()
                        .requestMatchers("/api/usuarios/**").authenticated()

                        // Cualquiera autenticado puede crear donación
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

    /**
     * Configuración CORS centralizada.
     * Se aplica a todos los endpoints antes de que Security valide el token,
     * lo que permite que los preflight OPTIONS pasen sin autenticación.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Orígenes permitidos — ajusta si tu frontend corre en otro puerto
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://localhost:3000"
        ));

        // Métodos HTTP permitidos (incluye OPTIONS para preflight)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Headers que puede enviar el cliente (incluye Authorization para JWT)
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "X-Skip-Loading",
                "Accept"
        ));

        // Permite enviar cookies / credenciales (necesario para que el navegador
        // incluya el header Authorization en las peticiones cross-origin)
        config.setAllowCredentials(true);

        // El navegador cachea la respuesta del preflight por 1 hora
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}