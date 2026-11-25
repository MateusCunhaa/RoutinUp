package service;

import model.Usuario;
import model.Tarefa;
import repository.TarefaRepository;
import java.util.ArrayList;
import java.time.LocalDate;

public class GerenciadorTarefas {

    private TarefaRepository tarefaRepository;
    private CalculadoraRecorrencia calculadoraRecorrencia;
    private Usuario usuario;

    // Construtor
    public GerenciadorTarefas(Usuario usuario){

        this.usuario = usuario;
        tarefaRepository = new TarefaRepository();
        calculadoraRecorrencia = new CalculadoraRecorrencia();
    }

    public ArrayList<Tarefa> listarHoje() {

        // Cria lista
        ArrayList<Tarefa> tarefasHoje = new ArrayList<>();

        // Pega todas tarefa do usuario pelo banco de dados
        ArrayList<Tarefa> tarefas = tarefaRepository.listarTodos(usuario.getId());

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
