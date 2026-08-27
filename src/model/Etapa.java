package model;

public class Etapa {

    private String nome;
    private boolean concluida;
    private double peso;
    private int id;

    public Etapa(String nome, double peso) {
        this.nome = nome;

        if (peso < 0 || peso > 100) {

            System.out.println("Peso inválido. Deve estar entre 0 e 100.");
        }else{

            this.peso = peso;
        }

        this.concluida = false;
    }


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



    public void concluir(){

        this.concluida = true;
    }

    public void reabrir(){

        this.concluida = false;
    }



}

