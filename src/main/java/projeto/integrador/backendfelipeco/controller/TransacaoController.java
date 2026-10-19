package projeto.integrador.backendfelipeco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.integrador.backendfelipeco.dtos.transacaoDTO.TransacaoRequestDTO;
import projeto.integrador.backendfelipeco.dtos.transacaoDTO.TransacaoResponseDTO;
import projeto.integrador.backendfelipeco.service.TransacaoService;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {


    private TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }


    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> registrar(@RequestBody TransacaoRequestDTO dto) {
        TransacaoResponseDTO response = transacaoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarDoCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(transacaoService.buscarTodasDoCliente(clienteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transacaoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> atualizar(@PathVariable Long id, @RequestBody TransacaoRequestDTO dto) {
        return ResponseEntity.ok(transacaoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}