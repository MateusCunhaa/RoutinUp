package service;

import model.Etapa;
import model.Categoria;
import model.Tarefa;
import repository.TarefaRepository;
import repository.EtapaRepository;
import java.util.ArrayList;
import java.time.LocalDate;


public class GerenciadorTarefas {

    private ArrayList<Tarefa> tarefas;
    private TarefaRepository tarefaRepository;
    private EtapaRepository etapaRepository;

    public GerenciadorTarefas(){

        tarefaRepository = new TarefaRepository();
        etapaRepository = new EtapaRepository();
        tarefas = tarefaRepository.listarTodos();
    }



    public void adicionarTarefa(Tarefa tarefa){

        tarefas.add(tarefa);
        tarefaRepository.salvar(tarefa);
    }

    public void listarTarefas(){
        for(Tarefa tarefa : tarefas){
            tarefa.mostrarTarefa();

            System.out.println("----------------------");
        }
    }

    public Tarefa buscarTarefa(String nome){
        for (Tarefa tarefa : tarefas){
            if (tarefa.getNome().equalsIgnoreCase(nome)){
                return tarefa;
            }
        }
        return null;

    }

    public void listarHabitos(){

        System.out.println("===== Habitos =====");

        for (Tarefa tarefa : tarefas){

            System.out.println(tarefa.getNome() + "- Sequencia: " + tarefa.getSequencia() + " dias");

            if (tarefa.getSequencia() >= 66){

                System.out.println("⭐ Hábito criado!");
            }
        }
    }

    public boolean todasTarefasDoDiaConcluida(){

        for (Tarefa tarefa : tarefas){

            if (!tarefa.isConcluida())

                return false;
        }

        return true;
    }

    public void editarNome (Tarefa tarefa, String novoNome){

        tarefa.setNome(novoNome);
    }

    public void adicionarEtapa(Tarefa tarefa, Etapa etapa){

        tarefa.adicionarEtapa(etapa);

        etapaRepository.salvar(
                etapa,
                tarefa.getId()
        );
    }

    public void concluirEtapa(Tarefa tarefa, Etapa etapa){

        etapa.setConcluida(true);

        etapaRepository.atualizarConclusao(etapa);

        if (tarefa.calcularProgresso() == 100){

            tarefa.concluirTarefa();

            tarefaRepository.atualizarConclusao(tarefa);
        }

    }

    public void concluirTarefa(Tarefa tarefa){

        tarefa.concluirTarefa();

        tarefaRepository.atualizarConclusao(tarefa);
    }


    public void editarHorario (Tarefa tarefa, String novoHorario){
        tarefa.setHorario(novoHorario);
    }

    public void editarDescricao (Tarefa tarefa, String novoDescricao){
        tarefa.setDescricao(novoDescricao);
    }

    public void editarDuracao (Tarefa tarefa, double novoDuracao){
        tarefa.setDuracao(novoDuracao);
    }

    public void editarPrioridade (Tarefa tarefa, double novoPrioridade){
        tarefa.setPrioridade(novoPrioridade);
    }

    public void editarCategoria (Tarefa tarefa, Categoria novoCategoria){
        tarefa.setCategoria(novoCategoria);
    }

    public void removerTarefa(Tarefa tarefa){
        tarefas.remove(tarefa);
    }








}
