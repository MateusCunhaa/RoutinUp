// Define estrutura e atributos de tarefa

package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.DayOfWeek;

import repository.EtapaRepository;

public class Tarefa {


    // Variaveis (Atributos)
    private ArrayList<Etapa> etapas; // lista etapas
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

    // Metodos

    public void concluirTarefa(){

        // Adiciona ou aumenta valor ao atributo do objeto
        this.concluida = true;
        this.porcentagem = 100;
        this.dataConclusao = LocalDate.now();
        this.sequencia++;

        // Enquanto tiver objetos "etapa" na listas "etapas"
        for (Etapa etapa : etapas){

            // Altera o atributo "concluida"
            etapa.setConcluida(true);
        }
    }

    public void reiniciarTarefa(){

        // Adiciona valor ao atributo do objeto
        this.concluida = false;
        this.porcentagem = 0;

        // Enquanto tiver objetos "etapa" na listas "etapas"
        for (Etapa etapa : etapas){

            // Altera o atributo "concluida"
            etapa.setConcluida(false);
        }
    }

    public void aumentarSequencia(){

        // Aumentar valor ao atributo do objeto
        this.sequencia++;
    }

    public boolean pesosValidos(){

        // Validacao metodo
        return somarPesos() == 100;
    }

    public double somarPesos(){

        double total = 0;

        // Enquanto tiver objetos "etapa" na listas "etapas"
        for (Etapa etapa : etapas){

            // Pega peso de etapa e soma ao total
            total += etapa.getPeso();
        }

        // Retorna o total
        return total;
    }

    public double calcularProgresso(){

        // Se concluida igual true
        if (concluida){

            // Retorna 100
            return 100;
        }

        // Declara variavel
        double total = 0;

        // Enquanto tiver objetos "etapa" na listas "etapas"
        for (Etapa etapa : etapas){

            // Se atributo "concluida" de etapa igual a true
            if (etapa.isConcluida()){

                // Pega peso de etapa e soma ao total
                total += etapa.getPeso();
            }
        }
        // Retorna total
        return total;
    }

    public void atualizarPorcentagem(double valor) {

        // Adiciona valor ao atributo do objeto
        this.porcentagem = valor;

        // Se valor igual a 100
        if (valor == 100) {

            // Adiciona valor ao atributo do objeto
            this.concluida = true;

          // Se não
        } else {

            // Adiciona valor ao atributo do objeto
            this.concluida = false;
        }
    }

    public boolean aconteceHoje(){

        // Se não repetir
        if (!repeticao){

            // Retorna
            return true;
        }

        // Obtem data atual do sistema (dia/mes/ano) e dia da semana, guarda em "hoje"
        DayOfWeek hoje = LocalDate.now().getDayOfWeek();

        // Declara variavel vazia
        String dia = "";

        // Verifica o valor armazenado em "hoje" e executa o bloco correspondente.
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

        // Verifica se atributo "diasSemana" contem o dia em que estamos (Retorna true ou false)
        return diasSemana.contains(dia);

    }






}