package dto.login;

public class LoginResponse {

    //Variaveis (Atributos)
    private int id;
    private String nome;
    private int xp;
    private int nivel;



    // Construtor
    public LoginResponse(int id,String nome, int xp, int nivel) {

        this.id = id;
        this.nome = nome;
        this.xp = xp;
        this.nivel = nivel;

    }


    //Gets
    public int getId(){
        return id;
    }

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