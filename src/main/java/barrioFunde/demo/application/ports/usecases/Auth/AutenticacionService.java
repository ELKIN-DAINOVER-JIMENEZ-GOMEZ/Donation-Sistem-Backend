package barrioFunde.demo.application.ports.usecases.Auth;

import barrioFunde.demo.application.ports.in.Auth.AutenticarUsuarioUseCase;
import barrioFunde.demo.application.ports.out.Auth.JwtTokenPort;
import barrioFunde.demo.application.ports.out.PasswordEncoderPort;
import barrioFunde.demo.application.ports.out.UsuarioRepositoryPort;
import barrioFunde.demo.domain.model.Auth.AuthCredentials;
import barrioFunde.demo.domain.model.Auth.AuthToken;
import barrioFunde.demo.domain.model.Usuario;
import barrioFunde.demo.domain.service.Auth.ReglasNegocioAuth;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Servicio que implementa el caso de uso de autenticación
 * Orquesta la lógica de aplicación usando los puertos
 */
@RequiredArgsConstructor
public class AutenticacionService implements AutenticarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtTokenPort jwtTokenPort;

    @Override
    public AuthToken autenticar(AuthCredentials credentials) {
        // 1. Validar credenciales (dominio)
        credentials.validar();

        // 2. Buscar usuario por email (puerto)
        Usuario usuario = usuarioRepositoryPort.buscarPorEmail(credentials.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        // 3. Validar usuario según reglas de negocio (dominio)
        ReglasNegocioAuth.validarUsuarioParaAutenticacion(usuario);

        // 4. Validar password (puerto)
        ReglasNegocioAuth.validarCredenciales(credentials.getPassword(), usuario.getPassword());

        boolean passwordValida = passwordEncoderPort.matches(
                credentials.getPassword(),
                usuario.getPassword()
        );

        if (!passwordValida) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // 5. Generar token JWT (puerto)
        String token = jwtTokenPort.generateToken(usuario);

        // 6. Construir respuesta con token
        return AuthToken.builder()
                .token(token)
                .type("Bearer")
                .expiresAt(LocalDateTime.now().plusHours(24)) // 24 horas de expiración
                .usuario(usuario)
                .build();
    }
}



