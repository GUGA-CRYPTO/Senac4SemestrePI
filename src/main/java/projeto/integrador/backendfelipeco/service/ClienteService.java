package projeto.integrador.backendfelipeco.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.integrador.backendfelipeco.Entidades.Cliente;
import projeto.integrador.backendfelipeco.dtos.clienteDto.ClienteRequestDTO;
import projeto.integrador.backendfelipeco.dtos.clienteDto.ClienteResponseDTO;
import projeto.integrador.backendfelipeco.exceptions.RecursoNaoEncontradoException;
import projeto.integrador.backendfelipeco.exceptions.RegraNegocioException;
import projeto.integrador.backendfelipeco.repository.ClienteRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ClienteService {

    private ClienteRepository cliRepo;

    public ClienteService(ClienteRepository clienteRepository) {
        this.cliRepo = clienteRepository;
    }

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        if (cliRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new RegraNegocioException("Email já cadastrado");
        }

        Cliente cliente = new Cliente();
        transformarEmEntidade(dto, cliente);
        cliente.setSaldoAtual(BigDecimal.ZERO);
        cliente.setDataCadastro(LocalDateTime.now());

        cliente = cliRepo.save(cliente);
        return transformarEmResponse(cliente);

    }


    public List<ClienteResponseDTO> listar() {
        return cliRepo.findAll().stream()
                .map(this::transformarEmResponse)
                .collect(Collectors.toList());
    }


    public ClienteResponseDTO buscarPorId(Long id){
        Cliente cliente = buscarEntidadePorId(id);
        return transformarEmResponse(cliente);
    }

    @Transactional
    public ClienteResponseDTO atualizar(Long id,ClienteRequestDTO dto) {
        Cliente cliente = buscarEntidadePorId(id);

        if (!cliente.getEmail().equals(dto.getEmail()) && cliRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new RegraNegocioException("Email já está em uso");
        }

        transformarEmEntidade(dto, cliente);
        cliente=cliRepo.save(cliente);
        return transformarEmResponse(cliente);

    }

    @Transactional
    public void deletar(Long id) {
        Cliente cliente = buscarEntidadePorId(id);
        cliRepo.delete(cliente);
    }

    


    public Cliente buscarEntidadePorId(Long id) {
        return cliRepo.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + id));
    }

    public void transformarEmEntidade(ClienteRequestDTO dto, Cliente entidade) {
        entidade.setNome(dto.getNome());
        entidade.setEmail(dto.getEmail());
        entidade.setSenha(dto.getSenha());
        entidade.setSalarioMensal(dto.getSalarioMensal());
        entidade.setPerfilRisco(dto.getPerfilRisco());
    }

    public ClienteResponseDTO transformarEmResponse(Cliente entidade){
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setId(entidade.getId());
        response.setNome(entidade.getNome());
        response.setSaldoAtual(entidade.getSaldoAtual());
        response.setEmail(entidade.getEmail());
        response.setSalarioMensal(entidade.getSalarioMensal());
        response.setPerfilRisco(entidade.getPerfilRisco());
        response.setDataCadastro(entidade.getDataCadastro());
        return response;
    }


}
