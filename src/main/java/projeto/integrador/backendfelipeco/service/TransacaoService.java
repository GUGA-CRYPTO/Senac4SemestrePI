package projeto.integrador.backendfelipeco.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projeto.integrador.backendfelipeco.Entidades.Cliente;
import projeto.integrador.backendfelipeco.Entidades.Transacao;
import projeto.integrador.backendfelipeco.Entidades.enums.TipoTransacao;
import projeto.integrador.backendfelipeco.dtos.transacaoDTO.TransacaoResponseDTO;
import projeto.integrador.backendfelipeco.exceptions.RecursoNaoEncontradoException;
import projeto.integrador.backendfelipeco.repository.ClienteRepository;
import projeto.integrador.backendfelipeco.repository.TransacaoRepository;

    @Service
    public class TransacaoService {


        private TransacaoRepository transacaoRepository;


        private ClienteRepository clienteRepository;

        public TransacaoService(TransacaoRepository transacaoRepository, ClienteRepository clienteRepository) {
            this.transacaoRepository = transacaoRepository;
            this.clienteRepository = clienteRepository;
        }

        @Transactional
        public TransacaoResponseDTO salvar(Transacao transacao, String emailClienteLogado) {
            Cliente cliente = clienteRepository.findByEmail(emailClienteLogado)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente com email " +  emailClienteLogado + " não encontrado"));

           cliente.adicionarTransacao(transacao);

            aplicarImpactoNoSaldo(cliente, transacao.getValor(), transacao.getTipo(), true);

            clienteRepository.save(cliente);
            transacao = transacaoRepository.save(transacao);

            return converterParaResponse(transacao);
        }

        public List<TransacaoResponseDTO> buscarTodasDoCliente(String emailClienteLogado) {
           Cliente cliente = clienteRepository.findByEmail(emailClienteLogado)
                   .orElseThrow(() -> new RecursoNaoEncontradoException("CLiente não encontrado"));
           return transacaoRepository.findByClienteIdOrderByDataCriacaoDesc(cliente.getId())
                   .stream()
                   .map(this::converterParaResponse)
                   .collect(Collectors.toList());
        }



        @Transactional
        public TransacaoResponseDTO atualizar(Long id, Transacao transacaoAtualizada, String emailClienteLogado) {
            Transacao transacaoAntiga = transacaoRepository.findById(id)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Transação não encontrada"));

            Cliente cliente = transacaoAntiga.getCliente();

            if(!cliente.getEmail().equals(emailClienteLogado)){
                throw new RuntimeException("Você não tem permissão para editar essa transação");
            }

            aplicarImpactoNoSaldo(cliente, transacaoAntiga.getValor(), transacaoAntiga.getTipo(), false);

transacaoAntiga.setDescricao(transacaoAtualizada.getDescricao());
transacaoAntiga.setValor(transacaoAtualizada.getValor());
transacaoAntiga.setDataCriacao(transacaoAtualizada.getDataCriacao());
transacaoAntiga.setTipo(transacaoAtualizada.getTipo());
transacaoAntiga.setCategoria(transacaoAtualizada.getCategoria());

             aplicarImpactoNoSaldo(cliente,transacaoAntiga.getValor(), transacaoAntiga.getTipo(), true);


            clienteRepository.save(cliente);
            transacaoAntiga = transacaoRepository.save(transacaoAntiga);

            return converterParaResponse(transacaoAntiga);
        }

        @Transactional
        public void deletar(Long trasacaoId, String emailClienteLogado ) {
            Transacao transacao = transacaoRepository.findById(trasacaoId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Transação não encontrada"));

            Cliente cliente = transacao.getCliente();

            if (!cliente.getEmail().equals(emailClienteLogado)) {
                throw new RuntimeException("Você não tem permissão para deletar esta transação.");
            }

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

