package dto.cadastro;

public class CadastroResult {

    // Variaveis (Atributos)
    private boolean sucesso;
    private String mensagem;

    // Construtor
    public CadastroResult(boolean sucesso, String mensagem) {

        this.sucesso = sucesso;
        this.mensagem = mensagem;

    }

    //Gets
    public boolean isSucesso() {
        return sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

}