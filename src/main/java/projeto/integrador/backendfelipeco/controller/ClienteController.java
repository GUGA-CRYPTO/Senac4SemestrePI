package projeto.integrador.backendfelipeco.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.integrador.backendfelipeco.dtos.clienteDto.ClienteRequestDTO;
import projeto.integrador.backendfelipeco.dtos.clienteDto.ClienteResponseDTO;
import projeto.integrador.backendfelipeco.service.ClienteService;

import java.util.List;


@RestController
    @RequestMapping("/api/clientes")
    public class ClienteController {

        private ClienteService clienteService;

        public ClienteController(ClienteService clienteService) {
            this.clienteService = clienteService;
        }

        @PostMapping
        public ResponseEntity<ClienteResponseDTO> cadastrar(@RequestBody ClienteRequestDTO dto) {
            ClienteResponseDTO response = clienteService.salvar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping
        public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
            return ResponseEntity.ok(clienteService.listar());
        }

        @GetMapping("/{id}")
        public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
            return ResponseEntity.ok(clienteService.buscarPorId(id));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @RequestBody ClienteRequestDTO dto) {
            return ResponseEntity.ok(clienteService.atualizar(id, dto));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletar(@PathVariable Long id) {
            clienteService.deletar(id);
            return ResponseEntity.noContent().build();
        }
    }
