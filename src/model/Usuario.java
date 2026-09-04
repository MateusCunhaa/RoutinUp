package model;

import service.SistemaXP;


public class Usuario {

    private String nome;
    private String gmail;
    private String senha;


    private int xp;
    private int nivel;
    private int habitosCriados;
    private int id;




    public Usuario(String nome, String gmail, String senha){

        this.nome = nome;
        this.gmail = gmail;
        this.senha = senha;

        this.xp = 0;
        this.nivel = 1;
        this.habitosCriados = 0;



    }


    public String getNome(){

        return nome;
    }

    public String getGmail(){

        return gmail;
    }

    public String getSenha(){

        return senha;
    }

    public int getXp(){

        return xp;
    }

    public void setXp(int xp){

        this.xp = xp;
    }

    public  int getNivel(){

        return nivel;
    }

    public void setNivel(int nivel){

        this.nivel = nivel;
    }

    public int getId(){

        return id;
    }

    public void setId(int id){

        this.id = id;
    }

    public int getHabitosCriados(){

        return habitosCriados;
    }


    public void adicionarXP(int valor){

        this.xp += valor;
    }

    public void aumentarHabitosCriados(){

        this.habitosCriados++;
    }





}
