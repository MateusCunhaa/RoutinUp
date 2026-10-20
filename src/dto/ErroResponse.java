package dto;

public class ErroResponse {

    // Variaveis (Atributos)
    private String mensagem;

    // Construtor
    public ErroResponse(String mensagem) {

        this.mensagem = mensagem;
    }

    // GET
    public String getMensagem() {

        return mensagem;
    }
}
