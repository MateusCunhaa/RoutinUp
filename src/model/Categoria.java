package model;

public class Categoria {

    // Variaveis (Atributos)
    private String nome;
    private String cor;
    private int id;
    private int usuarioId;


    // Construtor
    public Categoria(String nome, String cor){
        this.nome = nome;
        this.cor = cor;
    }



    //Gets e Sets
    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }


    public String getCor(){
        return cor;
    }
    public void setCor(String cor){
        this.cor = cor;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId(){

        return usuarioId;
    }
    public void setUsuarioId(int usuarioId){

        this.usuarioId = usuarioId;
    }
}

