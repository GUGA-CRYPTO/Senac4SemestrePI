package projeto.integrador.backendfelipeco.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.integrador.backendfelipeco.Entidades.Transacao;
import projeto.integrador.backendfelipeco.dtos.transacaoDTO.TransacaoResponseDTO;
import projeto.integrador.backendfelipeco.service.TransacaoService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {


    private TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }


    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> registrar(@RequestBody Transacao dto, Principal principal) {

        String emailLogado = principal.getName();

        TransacaoResponseDTO response = transacaoService.salvar(dto, emailLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarExtrato(Principal principal) {
        String emailLogado= principal.getName();
        List<TransacaoResponseDTO> extrato = transacaoService.buscarTodasDoCliente(emailLogado);
        return ResponseEntity.ok(extrato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> atualizar(@PathVariable Long id, @RequestBody Transacao dto, Principal principal) {
        String emailLogado = principal.getName();
        TransacaoResponseDTO response = transacaoService.atualizar(id, dto, emailLogado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id,Principal principal) {
        String emailLogado = principal.getName();
        transacaoService.deletar(id,emailLogado);
        return ResponseEntity.noContent().build();
    }
}