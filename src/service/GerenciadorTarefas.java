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

    public void adicionarEtapa(Tarefa tarefa, Etapa etapa){

        tarefa.adicionarEtapa(etapa);

        etapaRepository.salvar(
                etapa,
                tarefa.getId()
        );
    }

    public void listarTarefas(){

        ArrayList<Tarefa> tarefas = tarefaRepository.listarTodos(usuario.getId());

        for(Tarefa tarefa : tarefas){
            tarefa.mostrarTarefa();

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

        tarefa.concluirTarefa();

        int xp = sistemaXP.calcularXP(tarefa);

        sistemaXP.adicionarXP(usuario, xp);

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

        double progresso = tarefa.calcularProgresso();

        tarefa.atualizarPorcentagem(progresso);

        tarefaRepository.atualizar(tarefa);

        if (progresso >= 100){

            tarefa.concluirTarefa();

            tarefaRepository.atualizarConclusao(tarefa);
        }
    }

















}
