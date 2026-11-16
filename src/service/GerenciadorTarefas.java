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

        // Guarda tarefa no banco de dados
        tarefaRepository.salvar(tarefa);
    }

    public void editarNome (Tarefa tarefa, String novoNome){

        // Troca o "nome" para valor guardado dentro da variavel "novoNome"
        tarefa.setNome(novoNome);

        // Atualiza tarefa no banco de dados
        tarefaRepository.atualizar(tarefa);
    }

    public void editarHorario (Tarefa tarefa, String novoHorario){

        // Troca o "horario" para valor guardado dentro da variavel "novoHorario"
        tarefa.setHorario(novoHorario);

        // Atualiza tarefa no banco de dados
        tarefaRepository.atualizar(tarefa);
    }

    public void editarDescricao (Tarefa tarefa, String novoDescricao){

        // Troca a "descricao" para valor guardado dentro da variavel "novoDescricao"
        tarefa.setDescricao(novoDescricao);

        // Atualiza tarefa no banco de dados
        tarefaRepository.atualizar(tarefa);
    }

    public void editarDuracao (Tarefa tarefa, double novoDuracao){

        // Troca a "duracao" para valor guardado dentro da variavel "novoDuracao"
        tarefa.setDuracao(novoDuracao);

        // Atualiza tarefa no banco de dados
        tarefaRepository.atualizar(tarefa);
    }

    public void editarPrioridade (Tarefa tarefa, double novoPrioridade){

        // Troca a "prioridade" para valor guardado dentro da variavel "novoPrioridade"
        tarefa.setPrioridade(novoPrioridade);

        // Atualiza tarefa no banco de dados
        tarefaRepository.atualizar(tarefa);
    }

    public void editarCategoria (Tarefa tarefa, Categoria novoCategoria){

        // Troca a "categoria" para valor guardado dentro da variavel "novoCategoria"
        tarefa.setCategoria(novoCategoria);

        // Atualiza tarefa no banco de dados
        tarefaRepository.atualizar(tarefa);
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

    public int finalizarTarefa(Tarefa tarefa, Usuario usuario){

        if (tarefa.isXpRecebido()){

            System.out.println("Essa tarefa já recebeu XP!");

            return 0;
        }

        tarefa.concluirTarefa();

        int xp = sistemaXP.calcularXP(tarefa);

        usuario.adicionarXP(xp);

        sistemaXP.calcularNivel(usuario);

        tarefa.setXpRecebido(true);

        if (tarefa.isRepeticao()){

            tarefa.reiniciarTarefa();
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

        return tarefaRepository.listarHoje(usuario.getId()); // Retorna resposta comando SQL

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

        ArrayList<Tarefa> tarefas = tarefaRepository.listarTodos(usuario.getId());

        for (Tarefa tarefa : tarefas){

            if (!tarefa.isConcluida())

                return false;
        }

        return true;
    }

    public void concluirEtapa(Tarefa tarefa, Etapa etapa){

        etapa.setConcluida(true);

        etapaRepository.atualizarConclusao(etapa);

        double progresso = tarefa.calcularProgresso();

        tarefa.atualizarPorcentagem(progresso);

        tarefaRepository.atualizar(tarefa);

        if (progresso >= 100){

            tarefa.concluirTarefa();

            tarefaRepository.atualizarConclusao(tarefa);
        }
    }



















}
