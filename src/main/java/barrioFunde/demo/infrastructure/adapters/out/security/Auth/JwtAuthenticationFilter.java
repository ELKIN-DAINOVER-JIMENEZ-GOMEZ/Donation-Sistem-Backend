package barrioFunde.demo.infrastructure.adapters.out.security.Auth;


import barrioFunde.demo.application.ports.out.Auth.JwtTokenPort;
import barrioFunde.demo.application.ports.out.UsuarioRepositoryPort;
import barrioFunde.demo.domain.model.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * Filtro JWT que intercepta cada petición HTTP
 * Valida el token y establece la autenticación en el contexto de Spring Security
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenPort jwtTokenPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. Extraer token del header Authorization
            String token = extractTokenFromRequest(request);

            if (token != null && jwtTokenPort.validateToken(token)) {
                // 2. Extraer email del token
                String email = jwtTokenPort.extractEmail(token);

                // 3. Buscar usuario en la base de datos
                Optional<Usuario> usuarioOpt = usuarioRepositoryPort.buscarPorEmail(email);

                if (usuarioOpt.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Usuario usuario = usuarioOpt.get();

                    // 4. Crear autenticación
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    usuario,
                                    null,
                                    Collections.singletonList(
                                            new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())
                                    )
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 5. Establecer autenticación en el contexto
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            logger.error("Error al procesar el token JWT", e);
        }

        // 6. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token del header Authorization
     * Formato esperado: "Bearer <token>"
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}



