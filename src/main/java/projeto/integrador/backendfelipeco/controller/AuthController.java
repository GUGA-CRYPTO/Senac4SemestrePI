package projeto.integrador.backendfelipeco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto.integrador.backendfelipeco.Entidades.Cliente;
import projeto.integrador.backendfelipeco.dtos.authDto.LoginRequestDTO;
import projeto.integrador.backendfelipeco.dtos.authDto.LoginResponseDTO;
import projeto.integrador.backendfelipeco.exceptions.CredenciaisInvalidasException;
import projeto.integrador.backendfelipeco.repository.ClienteRepository;
import projeto.integrador.backendfelipeco.service.TokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthController(ClienteRepository clienteRepository,
                          PasswordEncoder passwordEncoder,
                          TokenService tokenService) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        if (dto.getEmail() == null || dto.getSenha() == null) {
            throw new CredenciaisInvalidasException("Usuário ou senha inválidos");
        }

        Cliente cliente = clienteRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CredenciaisInvalidasException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), cliente.getSenha())) {
            throw new CredenciaisInvalidasException("Usuário ou senha inválidos");
        }

        String token = tokenService.gerarToken(cliente.getEmail());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
