package projeto.integrador.backendfelipeco.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.integrador.backendfelipeco.Entidades.Cliente;

import projeto.integrador.backendfelipeco.dtos.clienteDto.ClienteResponseDTO;
import projeto.integrador.backendfelipeco.dtos.clienteDto.FormularioRiscoDTO;
import projeto.integrador.backendfelipeco.service.ClienteService;

import java.security.Principal;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(@RequestBody Cliente cliente) {
        ClienteResponseDTO response = clienteService.salvar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/meu-perfil")
    public ResponseEntity<ClienteResponseDTO> buscarPerfil(Principal principal) {
        String emailLogado = principal.getName();
        ClienteResponseDTO perfil = clienteService.buscarPorEmail(emailLogado);
        return ResponseEntity.ok(perfil);
    }


    @PutMapping("/meu-perfil")
    public ResponseEntity<ClienteResponseDTO> atualizarPerfil(@RequestBody Cliente dadosAtualizados, Principal principal) {
        String emailLogado = principal.getName();
        ClienteResponseDTO perfilAtualizado = clienteService.atualizar(emailLogado, dadosAtualizados);
        return ResponseEntity.ok(perfilAtualizado);
    }


    @DeleteMapping("/meu-perfil")
    public ResponseEntity<Void> deletarConta(Principal principal) {
        String emailLogado = principal.getName();
        clienteService.deletar(emailLogado);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/meu-perfil/risco")
    public ResponseEntity<ClienteResponseDTO> atualizarPerfilRisco(@RequestBody FormularioRiscoDTO formulario, Principal principal) {
        String emailLogado = principal.getName();
        ClienteResponseDTO perfilAtualizado = clienteService.definirPerfilRisco(emailLogado, formulario);
        return ResponseEntity.ok(perfilAtualizado);
    }
}