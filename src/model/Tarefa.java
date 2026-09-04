package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.DayOfWeek;

public class Tarefa {

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

    public void aumentarSequencia(){

        this.sequencia++;
    }

    public boolean isBonusHabitoRecebido(){

        return bonusHabitoRecebido;
    }

    public void setBonusHabitoRecebido(boolean bonusHabitoRecebido){

        this.bonusHabitoRecebido = bonusHabitoRecebido;
    }


    public void mostrarTarefa(){

        System.out.println("Nome: " + nome);
        System.out.println("Descrição: " + descricao);
        System.out.println("Horário: " + horario);
        System.out.println("Duração: " + duracao + " minutos");
        System.out.println("Prioridade: " + prioridade + " estrelas");
        System.out.println("Progresso: " + calcularProgresso() + "%");
        System.out.println("Concluída: " + concluida);

        if (categoria != null) {

            System.out.println("Categoria: " + categoria.getNome());
        }
    }

    public void concluirTarefa(){
        this.concluida = true;
        this.porcentagem = 100;
        this.dataConclusao = LocalDate.now();
        this.sequencia++;

        for (Etapa etapa : etapas){

            etapa.setConcluida(true);
        }
    }

    public void reiniciarTarefa(){

        this.concluida = false;
        this.porcentagem = 0;

        for (Etapa etapa : etapas){

            etapa.setConcluida(false);
        }
    }

    public void atualizarPorcentagem(double valor) {
        this.porcentagem = valor;
        if (valor == 100) {
            this.concluida = true;
        } else {
            this.concluida = false;
        }
    }

    public void adicionarEtapa(Etapa etapa){

            etapas.add(etapa);
        }

    public void adicionarDia(String dia){

        diasSemana.add(dia);
    }

    public double somarPesos(){

        double total = 0;

        for (Etapa etapa : etapas){

            total += etapa.getPeso();
        }

        return total;
    }

    public boolean pesosValidos(){

        return somarPesos() == 100;
    }

    public void mostrarEtapas(){

        System.out.println("Etapas: ");

        for (Etapa etapa : etapas){
            System.out.println("- " + etapa.getNome() + " | Peso: " + etapa.getPeso() + "%" + " | Concluida: " + etapa.isConcluida());
        }
    }

    public Etapa buscarEtapa(String nome){

        for (Etapa etapa : etapas){

            if (etapa.getNome().equalsIgnoreCase(nome)){

                return etapa;
            }
        }

        return null;
    }

    public double calcularProgresso(){

        if (concluida){

            return 100;
        }

        double total = 0;

        for (Etapa etapa : etapas){

            if (etapa.isConcluida()){

                total += etapa.getPeso();
            }
        }
        return total;
    }

    public boolean aconteceHoje(){

        if (!repeticao){

            return true;
        }

        DayOfWeek hoje = LocalDate.now().getDayOfWeek();

        String dia = "";

        switch (hoje){

            case SUNDAY :
                dia = "Domingo";
                break;

            case MONDAY:
                dia = "Segunda";
                break;

            case TUESDAY:
                dia = "Terça";
                break;

            case WEDNESDAY:
                dia = "Quarta";
                break;

            case THURSDAY:
                dia = "Quinta";
                break;

            case FRIDAY:
                dia = "Sexta";
                break;

            case SATURDAY:
                dia = "Sabado";
                break;
        }

        return diasSemana.contains(dia);

    }



}