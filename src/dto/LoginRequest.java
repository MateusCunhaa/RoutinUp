package dto;

public class LoginRequest {

    // Variaveis(Atributos)
    private String gmail;
    private String senha;

    //Construtor
    public LoginRequest(String gmail, String senha) {
        this.gmail = gmail;
        this.senha = senha;
    }

    // Gets
    public String getGmail() {
        return gmail;
    }

    public String getSenha() {
        return senha;
    }

}