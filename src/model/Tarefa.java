// Define estrutura e atributos de tarefa

package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.DayOfWeek;

public class Tarefa {


    // Variaveis (Atributos)
    private ArrayList<Etapa> etapas;
    private ArrayList<String> diasSemana;
    private Categoria categoria;
    private String nome;
    private String descricao;
    private String horario;
    private double duracao;
    private double prioridade;
    private double porcentagem;
    private boolean concluida;
    private boolean repeticao;
    private boolean bonusHabitoRecebido;
    private LocalDate dataConclusao;
    private int sequencia;
    private int id;
    private int usuarioId;
    private boolean xpRecebido;


    // Construtor
    public Tarefa(String nome, String descricao, String horario, double duracao, double prioridade , Categoria categoria) {

        this.nome = nome;
        this.descricao = descricao;
        this.horario = horario;
        this.duracao = duracao;
        this.prioridade = prioridade;
        this.porcentagem = 0;
        this.concluida = false;
        this.categoria = categoria;
        this.etapas = new ArrayList<>();
        this.repeticao = false;
        this.diasSemana = new ArrayList<>();
        this.dataConclusao = null;
        this.sequencia = 0;
        this.bonusHabitoRecebido = false;
        this.id = 0;
        this.xpRecebido = false;
    }


    // Gets e Sets
    public String getNome(){

        return nome;
    }
    public void setNome(String nome){

        this.nome = nome;
    }


    public String getDescricao(){

        return descricao;
    }
    public void setDescricao(String descricao){

        this.descricao = descricao;
    }


    public String getHorario(){

        return horario;
    }
    public void setHorario(String horario){

        this.horario = horario;
    }

    public int getUsuarioId(){

        return usuarioId;
    }
    public void setUsuarioId(int usuarioId){

        this.usuarioId = usuarioId;
    }


    public double getDuracao(){

        return  duracao;
    }
    public void setDuracao(double duracao){

        this.duracao = duracao;
    }


    public double getPrioridade(){

        return prioridade;
    }
    public void setPrioridade(double prioridade){

        this.prioridade = prioridade;
    }


    public  double getPorcentagem(){

        return  porcentagem;
    }
    public void setPorcentagem(double porcentagem){

        this.porcentagem = porcentagem;
    }


    public boolean isConcluida(){

        return concluida;
    }
    public void setConcluida(boolean concluida){

        this.concluida = concluida;
    }

    public boolean isXpRecebido(){

        return xpRecebido;
    }
    public void setXpRecebido(boolean xpRecebido){

        this.xpRecebido = xpRecebido;
    }


    public boolean isRepeticao(){

        return repeticao;
    }
    public  void setRepeticao(boolean repeticao){

        this.repeticao = repeticao;
    }


    public Categoria getCategoria(){

        return categoria;
    }
    public void setCategoria(Categoria categoria){

        this.categoria = categoria;
    }


    public ArrayList<String> getDiasSemana(){

        return diasSemana;
    }
    public void setDiasSemana(ArrayList<String> diasSemana){

        this.diasSemana = diasSemana;
    }

    public ArrayList<Etapa> getEtapas() {
        return etapas;
    }

    public void setEtapas(ArrayList<Etapa> etapas) {

        this.etapas = etapas;
    }

    public LocalDate getDataConclusao(){

        return dataConclusao;
    }
    public void setDataConclusao(LocalDate dataConclusao){

        this.dataConclusao = dataConclusao;
    }

    public int getId(){

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getSequencia(){

        return sequencia;
    }

    public void setSequencia(int sequencia){

        this.sequencia = sequencia;
    }

    public boolean isBonusHabitoRecebido(){

        return bonusHabitoRecebido;
    }

    public void setBonusHabitoRecebido(boolean bonusHabitoRecebido){

        this.bonusHabitoRecebido = bonusHabitoRecebido;
    }





}