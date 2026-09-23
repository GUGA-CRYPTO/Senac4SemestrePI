package projeto.integrador.backendfelipeco.dtos.clienteDto;

import projeto.integrador.backendfelipeco.Entidades.enums.PerfilRisco;

import java.math.BigDecimal;

public class ClienteRequestDTO {
    private String nome;
    private String email;
    private String senha;
    private BigDecimal salarioMensal;
    private PerfilRisco perfilRisco;

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


}
