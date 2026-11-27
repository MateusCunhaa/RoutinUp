package service;

import model.Usuario;
import model.Tarefa;
import repository.TarefaRepository;
import repository.UsuarioRepository;
import java.util.ArrayList;
import java.time.LocalDate;

public class GerenciadorTarefas {

    private TarefaRepository tarefaRepository;
    private CalculadoraRecorrencia calculadoraRecorrencia;


    // Construtor
    public GerenciadorTarefas(){

        tarefaRepository = new TarefaRepository();
        calculadoraRecorrencia = new CalculadoraRecorrencia();
    }

    // Metodos

    public void adicionarTarefa(Tarefa tarefa) {

        // Salva tarefa no banco de dados
        tarefaRepository.salvar(tarefa);
    }

    public ArrayList<Tarefa> listarHoje(int usuarioId) {

        // Cria lista
        ArrayList<Tarefa> tarefasHoje = new ArrayList<>();

        // Pega todas tarefa do usuario pelo banco de dados
        ArrayList<Tarefa> tarefas = tarefaRepository.listarTodos(usuarioId);

        // Obtem data atual
        LocalDate hoje = LocalDate.now();

        // Enquanto houver tarefa na lista tarefas
        for (Tarefa tarefa : tarefas) {

            // Verifica se tarefa acontece na data atual
            boolean acontece = calculadoraRecorrencia.aconteceEm(tarefa.getRecorrencia(),hoje);

            // Se acontece
            if (acontece) {

                // Adiciona tarefa a lista
                tarefasHoje.add(tarefa);
            }
        }

        // Retorna lista
        return tarefasHoje;
    }




}
