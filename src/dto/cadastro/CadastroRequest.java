package dto.cadastro;

public class CadastroRequest {

    // Variaveis (Atributos)
    private String nome;
    private String gmail;
    private String senha;

    // Construtor
    public CadastroRequest(String nome, String gmail, String senha) {

        this.nome = nome;
        this.gmail = gmail;
        this.senha = senha;
    }

    // Gets
    public String getNome() {
        return nome;
    }

    public String getGmail() {
        return gmail;
    }

    public String getSenha() {
        return senha;
    }

}