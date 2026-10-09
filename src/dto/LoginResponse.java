package dto;

public class LoginResponse {

    //Variaveis (Atributos)
    private String nome;
    private int xp;
    private int nivel;


    // Construtor
    public LoginResponse(String nome, int xp, int nivel) {

        this.nome = nome;
        this.xp = xp;
        this.nivel = nivel;

    }


    //Gets
    public String getNome() {
        return nome;
    }

    public int getXp() {
        return xp;
    }

    public int getNivel() {
        return nivel;
    }

}