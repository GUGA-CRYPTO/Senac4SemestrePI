package projeto.integrador.backendfelipeco.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import projeto.integrador.backendfelipeco.Entidades.enums.PerfilRisco;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(nullable = false,length = 100)
    private String nome;

@Column(nullable = false,unique = true,length = 100)
    private String email;

@Column(nullable = false)
    private String senha;

@Column(nullable= false)
    private BigDecimal saldoAtual = BigDecimal.ZERO;

@Column(nullable = false)
    private BigDecimal salarioMensal = BigDecimal.ZERO;

@Enumerated(EnumType.STRING)
@Column(nullable = false)
    private PerfilRisco perfilRisco = PerfilRisco.MODERADO;

private LocalDateTime dataCadastro = LocalDateTime.now();

@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonIgnore
private List<Transacao> transacoes = new ArrayList<>();


    public void adicionarTransacao(Transacao transacao) {
        this.transacoes.add(transacao);
        transacao.setCliente(this);
    }



    public List<Transacao> getTransacoes() { return transacoes; }
    public void setTransacoes(List<Transacao> transacoes) { this.transacoes = transacoes; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public BigDecimal getSalarioMensal() {
        return salarioMensal;
    }

    public void setSalarioMensal(BigDecimal salarioMensal) {
        this.salarioMensal = salarioMensal;
    }

    public PerfilRisco getPerfilRisco() {
        return perfilRisco;
    }

    public void setPerfilRisco(PerfilRisco perfilRisco) {
        this.perfilRisco = perfilRisco;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
