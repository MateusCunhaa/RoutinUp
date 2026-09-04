package service;

import model.Usuario;
import model.Tarefa;
import service.GerenciadorTarefas;

public class SistemaXP {




    private int calcularPesoPrioridade(double prioridade){

        if (prioridade <= 2){

            return 1;
        } else if (prioridade <= 4) {

            return 2;
        }else{

            return 3;
        }
    }

    public int bonusTodasTarefasDoDia(GerenciadorTarefas gerenciador){

        if (gerenciador.todasTarefasDoDiaConcluida()){
            return 10;
        }

        return 0;
    }

    public int bonusNovoHabito(Tarefa tarefa){

        if (tarefa.getSequencia() >= 66 && !tarefa.isBonusHabitoRecebido()) {

            tarefa.setBonusHabitoRecebido(true);

            return 50;
        }

        return 0;
    }

    public void adicionarXP(Usuario usuario, int valor){

        usuario.adicionarXP(valor);
    }

    public void calcularNivel(Usuario usuario){

        int xp = usuario.getXp();
        int nivel = 1;
        int xpNecessario = 100;

        while (xp >= xpNecessario){

            nivel++;
            xpNecessario *= 2;
        }

        usuario.setNivel(nivel);
    }

    public int calcularXP(Tarefa tarefa){

        int xp = 0;

        xp += (int) (tarefa.getDuracao() / 20);

        if (tarefa.getPrioridade() <= 2){

            xp += 1;

        }else if(tarefa.getPrioridade() <= 4){

            xp += 2;

        }else {

            xp += 3;

        }

        xp += 2;

        return xp;
    }



}
