package projeto.integrador.backendfelipeco.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.integrador.backendfelipeco.Entidades.Cliente;
import projeto.integrador.backendfelipeco.Entidades.enums.PerfilRisco;

import projeto.integrador.backendfelipeco.dtos.clienteDto.ClienteResponseDTO;
import projeto.integrador.backendfelipeco.dtos.clienteDto.FormularioRiscoDTO;
import projeto.integrador.backendfelipeco.exceptions.RegraNegocioException;
import projeto.integrador.backendfelipeco.exceptions.RecursoNaoEncontradoException;
import projeto.integrador.backendfelipeco.repository.ClienteRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public ClienteResponseDTO salvar(Cliente cliente) {
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new RegraNegocioException("E-mail já cadastrado no sistema.");
        }

        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));

        cliente.setSaldoAtual(BigDecimal.ZERO);
        cliente.setDataCadastro(LocalDateTime.now());

        if (cliente.getSalarioMensal() == null) {
            cliente.setSalarioMensal(BigDecimal.ZERO);
        }

        cliente = clienteRepository.save(cliente);
        return converterParaResponse(cliente);
    }


    public ClienteResponseDTO buscarPorEmail(String emailLogado) {
        Cliente cliente = clienteRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
        return converterParaResponse(cliente);
    }


    @Transactional
    public ClienteResponseDTO atualizar(String emailLogado, Cliente dadosAtualizados) {
        Cliente clienteExistente = clienteRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));


        if (dadosAtualizados.getNome() != null && !dadosAtualizados.getNome().isEmpty()) {
            clienteExistente.setNome(dadosAtualizados.getNome());
        }

        if (dadosAtualizados.getSalarioMensal() != null) {
            clienteExistente.setSalarioMensal(dadosAtualizados.getSalarioMensal());
        }


        if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().isEmpty()) {
            clienteExistente.setSenha(passwordEncoder.encode(dadosAtualizados.getSenha()));
        }

        clienteExistente = clienteRepository.save(clienteExistente);
        return converterParaResponse(clienteExistente);
    }


    @Transactional
    public void deletar(String emailLogado) {
        Cliente cliente = clienteRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
        clienteRepository.delete(cliente);
    }


    @Transactional
    public ClienteResponseDTO definirPerfilRisco(String emailLogado, FormularioRiscoDTO formulario) {
        Cliente cliente = clienteRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));

        int pontos = formulario.getPontuacaoTotal();

        if (pontos <= 4) {
            cliente.setPerfilRisco(PerfilRisco.CONSERVADOR);
        } else if (pontos <= 7) {
            cliente.setPerfilRisco(PerfilRisco.MODERADO);
        } else {
            cliente.setPerfilRisco(PerfilRisco.ARROJADO);
        }

        cliente = clienteRepository.save(cliente);
        return converterParaResponse(cliente);
    }

    private ClienteResponseDTO converterParaResponse(Cliente entidade) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(entidade.getId());
        dto.setNome(entidade.getNome());
        dto.setEmail(entidade.getEmail());
        dto.setSaldoAtual(entidade.getSaldoAtual());
        dto.setSalarioMensal(entidade.getSalarioMensal());
        dto.setPerfilRisco(entidade.getPerfilRisco());
        dto.setDataCadastro(entidade.getDataCadastro());
        return dto;
    }
}