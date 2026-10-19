package projeto.integrador.backendfelipeco.dtos.authDto;

public class LoginResponseDTO {
    private String token;
    private String tipo = "Bearer";

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
