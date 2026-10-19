package projeto.integrador.backendfelipeco.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projeto.integrador.backendfelipeco.Entidades.Cliente;
import projeto.integrador.backendfelipeco.Entidades.Transacao;
import projeto.integrador.backendfelipeco.Entidades.enums.TipoTransacao;
import projeto.integrador.backendfelipeco.dtos.transacaoDTO.TransacaoRequestDTO;
import projeto.integrador.backendfelipeco.dtos.transacaoDTO.TransacaoResponseDTO;
import projeto.integrador.backendfelipeco.exceptions.RecursoNaoEncontradoException;
import projeto.integrador.backendfelipeco.repository.ClienteRepository;
import projeto.integrador.backendfelipeco.repository.TransacaoRepository;

    @Service
    public class TransacaoService {

        private TransacaoRepository transacaoRepository;

        private ClienteService clienteService;

        private ClienteRepository clienteRepository;

        public TransacaoService(TransacaoRepository transacaoRepository, ClienteService clienteService, ClienteRepository clienteRepository) {
            this.transacaoRepository = transacaoRepository;
            this.clienteService = clienteService;
            this.clienteRepository = clienteRepository;
        }

        @Transactional
        public TransacaoResponseDTO salvar(TransacaoRequestDTO dto) {
            Cliente cliente = clienteService.buscarEntidadePorId(dto.getClienteId());

            Transacao transacao = new Transacao();
            copiarDtoParaEntidade(dto, transacao);
            transacao.setCliente(cliente);

            aplicarImpactoNoSaldo(cliente, transacao.getValor(), transacao.getTipo(), true);

            clienteRepository.save(cliente);
            transacao = transacaoRepository.save(transacao);

            return converterParaResponse(transacao);
        }

        public List<TransacaoResponseDTO> buscarTodasDoCliente(Long clienteId) {
            return transacaoRepository.findByClienteIdOrderByDataCriacaoDesc(clienteId).stream()
                    .map(this::converterParaResponse)
                    .collect(Collectors.toList());
        }

        public TransacaoResponseDTO buscarPorId(Long id) {
            Transacao transacao = transacaoRepository.findById(id)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Transação não encontrada"));
            return converterParaResponse(transacao);
        }

        @Transactional
        public TransacaoResponseDTO atualizar(Long id, TransacaoRequestDTO dto) {
            Transacao transacaoExistente = transacaoRepository.findById(id)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Transação não encontrada"));

            Cliente cliente = transacaoExistente.getCliente();

            aplicarImpactoNoSaldo(cliente, transacaoExistente.getValor(), transacaoExistente.getTipo(), false);

            copiarDtoParaEntidade(dto, transacaoExistente);

            aplicarImpactoNoSaldo(cliente, transacaoExistente.getValor(), transacaoExistente.getTipo(), true);

            clienteRepository.save(cliente);
            transacaoExistente = transacaoRepository.save(transacaoExistente);

            return converterParaResponse(transacaoExistente);
        }

        @Transactional
        public void deletar(Long id) {
            Transacao transacao = transacaoRepository.findById(id)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Transação não encontrada"));

            Cliente cliente = transacao.getCliente();

            aplicarImpactoNoSaldo(cliente, transacao.getValor(), transacao.getTipo(), false);

            clienteRepository.save(cliente);
            transacaoRepository.delete(transacao);
        }

        private void aplicarImpactoNoSaldo(Cliente cliente, java.math.BigDecimal valor, TipoTransacao tipo, boolean isAdicao) {
            if ((tipo == TipoTransacao.RECEITA && isAdicao) || (tipo == TipoTransacao.DESPESA && !isAdicao)) {
                cliente.setSaldoAtual(cliente.getSaldoAtual().add(valor));
            } else {
                cliente.setSaldoAtual(cliente.getSaldoAtual().subtract(valor));
            }
        }

        private void copiarDtoParaEntidade(TransacaoRequestDTO dto, Transacao entidade) {
            entidade.setDescricao(dto.getDescricao());
            entidade.setValor(dto.getValor());
            entidade.setDataCriacao(dto.getData());
            entidade.setTipo(dto.getTipo());
            entidade.setCategoria(dto.getCategoria());
        }

        private TransacaoResponseDTO converterParaResponse(Transacao entidade) {
            TransacaoResponseDTO dto = new TransacaoResponseDTO();
            dto.setId(entidade.getId());
            dto.setDescricao(entidade.getDescricao());
            dto.setValor(entidade.getValor());
            dto.setData(entidade.getDataCriacao());
            dto.setTipo(entidade.getTipo());
            dto.setCategoria(entidade.getCategoria());
            return dto;
        }
    }

