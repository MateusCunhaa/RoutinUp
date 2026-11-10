package service;

import model.Etapa;
import model.Categoria;
import model.Tarefa;
import model.Usuario;
import repository.UsuarioRepository;
import service.SistemaXP;
import repository.TarefaRepository;
import repository.EtapaRepository;
import repository.UsuarioRepository;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.time.LocalDate;


public class GerenciadorTarefas {


    // Variaveis (Atributos)
    private TarefaRepository tarefaRepository;
    private EtapaRepository etapaRepository;
    private Usuario usuario;
    private SistemaXP sistemaXP;
    private UsuarioRepository usuarioRepository;


    // Construtor
    public GerenciadorTarefas(Usuario usuario){

        this.usuario = usuario;
        tarefaRepository = new TarefaRepository();
        etapaRepository = new EtapaRepository();
        sistemaXP = new SistemaXP();
        usuarioRepository = new UsuarioRepository();
    }


    // Metodos
    public void adicionarTarefa(Tarefa tarefa){

        tarefaRepository.salvar(tarefa);
    }

    public void editarNome (Tarefa tarefa, String novoNome){

        tarefa.setNome(novoNome);
        tarefaRepository.atualizar(tarefa);
    }

    public void editarHorario (Tarefa tarefa, String novoHorario){

        tarefa.setHorario(novoHorario);
        tarefaRepository.atualizar(tarefa);
    }

    public void editarDescricao (Tarefa tarefa, String novoDescricao){

        tarefa.setDescricao(novoDescricao);
        tarefaRepository.atualizar(tarefa);
    }

    public void editarDuracao (Tarefa tarefa, double novoDuracao){

        tarefa.setDuracao(novoDuracao);
        tarefaRepository.atualizar(tarefa);
    }

    public void editarPrioridade (Tarefa tarefa, double novoPrioridade){

        tarefa.setPrioridade(novoPrioridade);
        tarefaRepository.atualizar(tarefa);
    }

    public void editarCategoria (Tarefa tarefa, Categoria novoCategoria){

        tarefa.setCategoria(novoCategoria);
        tarefaRepository.atualizar(tarefa);
    }

    public void listarTarefas(){

        ArrayList<Tarefa> tarefas = tarefaRepository.listarTodos(usuario.getId());

        for(Tarefa tarefa : tarefas){
            mostrarTarefa(tarefa);

            System.out.println("----------------------");
        }
    }

    public Tarefa buscarTarefa(String nome){

        ArrayList<Tarefa> tarefas = tarefaRepository.listarTodos(usuario.getId());

        for (Tarefa tarefa : tarefas){
            if (tarefa.getNome().equalsIgnoreCase(nome)){
                return tarefa;
            }
        }
        return null;

    }

    public void removerTarefa(Tarefa tarefa){

        etapaRepository.removerPorTarefa(tarefa.getId());
        tarefaRepository.remover(tarefa);
    }

    public int finalizarTarefa(Tarefa tarefa){

        if (tarefa.isXpRecebido()){

            System.out.println("Essa tarefa já recebeu XP!");

            return 0;
        }

        concluirTarefa(tarefa);

        int xp = sistemaXP.calcularXP(tarefa);

        sistemaXP.adicionarXP(usuario, xp);

        sistemaXP.calcularNivel(usuario);

        tarefa.setXpRecebido(true);

        if (tarefa.isRepeticao()){

            reiniciarTarefa(tarefa);
        }

        tarefaRepository.atualizarConclusao(tarefa);

        usuarioRepository.atualizarXP(usuario);

        System.out.println("Tarefa concluída!");
        System.out.println("XP ganho: " + xp);

        return xp;
    }

    public void atualizarXPRecebido(Tarefa tarefa){

        tarefaRepository.atualizarConclusao(tarefa);
    }

    public ArrayList<Tarefa> listarHoje(){

        return tarefaRepository.listarHoje(usuario.getId());

    }

    public void listarHabitos(){

        System.out.println("===== Habitos =====");

        ArrayList<Tarefa> tarefas = tarefaRepository.listarTodos(usuario.getId());

        for (Tarefa tarefa : tarefas){

            System.out.println(tarefa.getNome() + "- Sequencia: " + tarefa.getSequencia() + " dias");

            if (tarefa.getSequencia() >= 66){

                System.out.println("⭐ Hábito criado!");
            }
        }
    }

    public boolean todasTarefasDoDiaConcluida(){

        ArrayList<Tarefa> tarefas = tarefaRepository.listarTodos((usuario.getId()));

        for (Tarefa tarefa : tarefas){

            if (!tarefa.isConcluida())

                return false;
        }

        return true;
    }

    public void concluirEtapa(Tarefa tarefa, Etapa etapa){

        etapa.setConcluida(true);

        etapaRepository.atualizarConclusao(etapa);

        double progresso = calcularProgresso(tarefa);

        atualizarPorcentagem(tarefa, progresso);

        tarefaRepository.atualizar(tarefa);

        if (progresso >= 100){

            concluirTarefa(tarefa);

            tarefaRepository.atualizarConclusao(tarefa);
        }
    }

    public void mostrarTarefa(Tarefa tarefa){

        System.out.println("Nome: " + tarefa.getNome());
        System.out.println("Descrição: " + tarefa.getDescricao());
        System.out.println("Horário: " + tarefa.getHorario());
        System.out.println("Duração: " + tarefa.getDuracao() + " minutos");
        System.out.println("Prioridade: " + tarefa.getPrioridade() + " estrelas");
        System.out.println("Progresso: " + calcularProgresso(tarefa) + "%");
        System.out.println("Concluída: " + tarefa.isConcluida());

        if (tarefa.getCategoria() != null) {

            System.out.println("Categoria: " + tarefa.getCategoria().getNome());
        }
    }

    public void concluirTarefa(Tarefa tarefa){
        tarefa.setConcluida(true);
        tarefa.setPorcentagem(100);
        tarefa.setDataConclusao(LocalDate.now());
        tarefa.setSequencia(tarefa.getSequencia() + 1);

        for (Etapa etapa : tarefa.getEtapas()){

            etapa.setConcluida(true);
        }
    }

    public void reiniciarTarefa(Tarefa tarefa){

        tarefa.setConcluida(false);
        tarefa.setPorcentagem(0);

        for (Etapa etapa : tarefa.getEtapas()){

            etapa.setConcluida(false);
        }
    }

    public void adicionarEtapa(Tarefa tarefa, Etapa etapa) {

        tarefa.getEtapas().add(etapa);

        etapaRepository.salvar(etapa, tarefa.getId());
    }

    public void mostrarEtapas(Tarefa tarefa){

        System.out.println("Etapas: ");

        for (Etapa etapa : tarefa.getEtapas()){
            System.out.println("- " + etapa.getNome() + " | Peso: " + etapa.getPeso() + "%" + " | Concluida: " + etapa.isConcluida());
        }
    }

    public Etapa buscarEtapa(Tarefa tarefa, String nome){

        for (Etapa etapa : tarefa.getEtapas()){

            if (etapa.getNome().equalsIgnoreCase(nome)){

                return etapa;
            }
        }

        return null;
    }

    public void aumentarSequencia(Tarefa tarefa){

        tarefa.setSequencia(tarefa.getSequencia() + 1);
    }

    public boolean pesosValidos(Tarefa tarefa){

        return somarPesos(tarefa) == 100;
    }

    public double somarPesos(Tarefa tarefa){

        double total = 0;

        for (Etapa etapa : tarefa.getEtapas()){

            total += etapa.getPeso();
        }

        return total;
    }

    public double calcularProgresso(Tarefa tarefa){

        if (tarefa.isConcluida()){

            return 100;
        }

        double total = 0;

        for (Etapa etapa : tarefa.getEtapas()){

            if (etapa.isConcluida()){

                total += etapa.getPeso();
            }
        }
        return total;
    }

    public void atualizarPorcentagem(Tarefa tarefa, double valor) {
        tarefa.setPorcentagem(valor);

        if (valor == 100) {
            tarefa.setConcluida(true);
        } else {
            tarefa.setConcluida(false);
        }
    }

    public void adicionarDia(Tarefa tarefa, String dia){

        tarefa.getDiasSemana().add(dia);
    }

    public boolean aconteceHoje(Tarefa tarefa){

        if (!tarefa.isRepeticao()){

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

        return tarefa.getDiasSemana().contains(dia);

    }

















}
