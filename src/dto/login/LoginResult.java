package dto.login;

public class LoginResult {

    // Variaveis (Atributos)
    private boolean sucesso;
    private String mensagem;
    private LoginResponse usuario;

    // Construtor
    public LoginResult(boolean sucesso, String mensagem, LoginResponse usuario){

        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.usuario = usuario;

    }

    // Gets
    public boolean isSucesso() {

        return sucesso;
    }

    public String getMensagem() {

        return mensagem;
    }

    public LoginResponse getUsuario() {

        return usuario;
    }
}
