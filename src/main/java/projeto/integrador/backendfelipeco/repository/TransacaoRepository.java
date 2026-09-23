package projeto.integrador.backendfelipeco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projeto.integrador.backendfelipeco.Entidades.Transacao;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByClienteIdOrderByDataDesc(Long clienteId);


    @Query("SELECT t FROM Transacao t WHERE t.cliente.id = :clienteId AND MONTH(t.data) = :mes AND YEAR(t.data) = :ano")
    List<Transacao> buscarPorMesEAno(@Param("clienteId") Long clienteId, @Param("mes") int mes, @Param("ano") int ano);
}