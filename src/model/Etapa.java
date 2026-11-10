package model;

public class Etapa {

    // Variaveis (Atributos)
    private String nome;
    private boolean concluida;
    private double peso;
    private int id;

    // Construtor
    public Etapa(String nome, double peso) {
        this.nome = nome;
        this.peso = peso;
        this.concluida = false;
    }


    // Gets e Sets
    public String getNome(){

        return nome;
    }
    public void setNome(String nome){

        this.nome = nome;
    }

    public int getId(){

        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getPeso(){

        return peso;
    }
    public void setPeso(double peso){

        this.peso = peso;
    }

    public boolean isConcluida(){

        return concluida;
    }
    public void setConcluida(boolean concluida){

        this.concluida = concluida;
    }

}

