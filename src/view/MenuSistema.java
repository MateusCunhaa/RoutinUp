package view;

import java.util.ArrayList;
import java.util.Scanner;

import model.Etapa;
import model.Tarefa;
import model.Categoria;
import model.Usuario;
import service.GerenciadorTarefas;
import service.GerenciadorCategorias;
import service.SistemaXP;
import repository.UsuarioRepository;

public class MenuSistema {


    private Scanner scan;
    private GerenciadorTarefas gerenciador;
    private GerenciadorCategorias gerenciadorCategorias;
    private Usuario usuario;
    private SistemaXP sistemaXP;
    private UsuarioRepository usuarioRepository;



    public MenuSistema(Usuario usuario, Scanner scan){

        this.scan = scan;
        this.usuario = usuario;

        gerenciador = new GerenciadorTarefas(usuario);
        gerenciadorCategorias = new GerenciadorCategorias(usuario);

        sistemaXP = new SistemaXP();

        usuarioRepository = new UsuarioRepository();
    }

    public void iniciar(){

        int opcao;

        do {
            System.out.println("====== RoutinUp ======");
            System.out.println("1 - Criar Tarefa");
            System.out.println("2 - Listar Tarefas");
            System.out.println("3 - Buscar Tarefa");
            System.out.println("4 - Editar Tarefa");
            System.out.println("5 - Remover Tarefa");
            System.out.println("6 - Adicionar Etapa");
            System.out.println("7 - Ver Etapas");
            System.out.println("8 - Concluir Etapa");
            System.out.println("9 - Concluir Tarefa");
            System.out.println("10 - Ver Perfil");
            System.out.println("11 - Ver Hoje");
            System.out.println("12 - Sair");

            System.out.println("Escolha uma opção:");

            opcao = Integer.parseInt( scan.nextLine());

            switch (opcao){

                case 1:

                    criarTarefa();

                break;

                case 2:

                    gerenciador.listarTarefas();

                break;

                case 3:

                    buscarTarefa();

                break;

                case 4:

                    editarTarefa();

                break;

                case 5:

                    removerTarefa();

                break;

                case 6:

                    adicionarEtapa();

                break;

                case 7:

                    verEtapas();

                    break;

                case 8:

                    concluirEtapa();

                    break;

                case  9:

                    concluirTarefa();

                    break;

                case 10:

                    verPerfil();

                    break;

                case 11:

                    verHoje();

                    break;

                case 12:

                    System.out.println("Saindo do RoutinUp...");

                    break;

                default:

                    System.out.println("Opção invalida");
            }

        }while (opcao != 12);
    }

    private void criarTarefa(){

        System.out.println("Nome da Tarefa : ");
        String nome = scan.nextLine();

        System.out.println("Descrição : ");
        String descricao = scan.nextLine();

        System.out.println("Horario : ");
        String horario = scan.nextLine();

        System.out.println("Duração em Minutos : ");
        double duracao = Double.parseDouble( scan.nextLine());

        System.out.println("Prioridade (0 a 5) : ");
        double prioridade = Double.parseDouble( scan.nextLine());

        System.out.println("Escolha uma Categoria : ");

        gerenciadorCategorias.listarCategorias();
        System.out.println((gerenciadorCategorias.quantidadeCategoria() + 1 ) + " - Criar Nova Categoria ");

        int escolhaCategoria = Integer.parseInt( scan.nextLine());

        Categoria categoriaEscolhida;

        if (escolhaCategoria == gerenciadorCategorias.quantidadeCategoria() + 1){

            criarNovaCategoria();
            System.out.println("Escolha novamente a categoria: ");
            gerenciadorCategorias.listarCategorias();
            escolhaCategoria = Integer.parseInt( scan.nextLine());

        }

        categoriaEscolhida = gerenciadorCategorias.buscarCategoria(escolhaCategoria);

        if (categoriaEscolhida == null){

            System.out.println("Categoria Invalida!");

            return;
        }

        System.out.println("A tarefa se repete?");

        System.out.println("1 - Sim");
        System.out.println("2 - Não");

        int escolhaRepeticao = Integer.parseInt( scan.nextLine());

        boolean repeticao = false;

        if (escolhaRepeticao == 1){

            repeticao = true;
        }


        Tarefa novaTarefa = new Tarefa(
                nome,
                descricao,
                horario,
                duracao,
                prioridade,
                categoriaEscolhida
        );

        novaTarefa.setUsuarioId(usuario.getId());
        novaTarefa.setRepeticao(repeticao);

        if (repeticao){

            System.out.println("Quantos dias deseja adicionar: ");
            int quantidadeDias = Integer.parseInt( scan.nextLine());

            System.out.println("1 - Domingo");
            System.out.println("2 - Segunda");
            System.out.println("3 - Terça");
            System.out.println("4 - Quarta");
            System.out.println("5 - Quintq");
            System.out.println("6 - Sexta");
            System.out.println("7 - Sabado");

            for (int i = 0; i < quantidadeDias; i++){

                System.out.println("Escolha o dia:");
                int dia = Integer.parseInt( scan.nextLine());

                switch (dia){

                    case 1:
                        novaTarefa.adicionarDia("Domingo");
                        break;

                    case 2:
                        novaTarefa.adicionarDia("Segunda");
                        break;

                    case 3:
                        novaTarefa.adicionarDia("Terça");
                        break;

                    case 4:
                        novaTarefa.adicionarDia("Quarta");
                        break;

                    case 5:
                        novaTarefa.adicionarDia("Quinta");
                        break;

                    case 6:
                        novaTarefa.adicionarDia("Sexta");
                        break;

                    case 7:
                        novaTarefa.adicionarDia("Sabado");
                        break;

                }
            }
        }

        gerenciador.adicionarTarefa(novaTarefa);

        System.out.println("Tarefa criada com sucesso!");
    }

    private void buscarTarefa(){

        System.out.println("Digite o nome da tarefa: ");

        String nome = scan.nextLine();

        Tarefa tarefa = gerenciador.buscarTarefa(nome);

        if (tarefa != null) {

            tarefa.mostrarTarefa();

        }else {

            System.out.println("Tarefa não encontrada");
        }
    }

    private void editarTarefa(){

        System.out.println("Digite o nome da tarefa que deseja editar: ");

        String nome = scan.nextLine();

        Tarefa tarefa = gerenciador.buscarTarefa(nome);

        if (tarefa == null){

            System.out.println("Tarefa não encontrada");

            return;
        }

        System.out.println("Qual informação deseja editar?");

        System.out.println("1 - Nome");
        System.out.println("2 - Descrição");
        System.out.println("3 - Horario");
        System.out.println("4 - Duração");
        System.out.println("5 - Prioridada");

        int escolha = Integer.parseInt( scan.nextLine());

        switch (escolha){

            case 1:

                System.out.println("Novo nome: ");
                String novoNome = scan.nextLine();
                gerenciador.editarNome(tarefa, novoNome);

                break;

            case 2:

                System.out.println("Nova Descrição: ");
                String novoDescricao = scan.nextLine();
                gerenciador.editarDescricao(tarefa, novoDescricao);

                break;

            case 3:

                System.out.println("Novo horario: ");
                String novoHorario = scan.nextLine();
                gerenciador.editarHorario(tarefa, novoHorario);

                break;

            case 4:

                System.out.println("Nova duração: ");
                double novoDuracao = Double.parseDouble( scan.nextLine());
                gerenciador.editarDuracao(tarefa, novoDuracao);

                break;

            case 5:

                System.out.println("Nova Prioridade: ");
                double novoPrioridade = Double.parseDouble( scan.nextLine());
                gerenciador.editarPrioridade(tarefa, novoPrioridade);

                break;

            default:

                System.out.println("Opção invalida");

        }

        System.out.println("Tarefa editada com sucesso!");
    }

    public void removerTarefa(){

        System.out.println("Digite o nome da tarefa que seseja remover: ");

        String nome = scan.nextLine();

        Tarefa tarefa = gerenciador.buscarTarefa(nome);

        if (tarefa != null){

            gerenciador.removerTarefa(tarefa);
            System.out.println("Tarefa removida com sucesso!");

        }else {

            System.out.println("Tarefa não encontrada");
        }
    }

    private void concluirTarefa(){

        System.out.println("Digite o nome da tarefa: ");
        String nome = scan.nextLine();

        Tarefa tarefa = gerenciador.buscarTarefa(nome);

        if (tarefa == null){

            System.out.println("Tarefa não encontrada");

            return;
        }

        if (tarefa.isConcluida()){

            System.out.println("Essa tarefa ja foi concluida.");

            return;
        }

        if (tarefa.isXpRecebido()){

            System.out.println("Essa tarefa ja recebeu xp");

            return;
        }

        gerenciador.finalizarTarefa(tarefa);
        int xpGanho = darXPDaTarefa(tarefa);

        System.out.println("Tarefa Concluida!");
        System.out.println("XP ganho: " + xpGanho);
        System.out.println("Nivel Atual: " + usuario.getNivel());

    }

    private void criarNovaCategoria(){

        System.out.println("Nome da Categoria : ");
        String nome = scan.nextLine();

        System.out.println("Cor da Categoria : ");
        String cor = scan.nextLine();

        gerenciadorCategorias.criarCategoria(nome, cor);

        System.out.println("Categoria Criada com Sucesso");
    }

    private void adicionarEtapa(){

        System.out.println("Digite o nome da Tarefa");

        String nomeTarefa = scan.nextLine();

        Tarefa tarefa = gerenciador.buscarTarefa(nomeTarefa);

        if (tarefa == null){

            System.out.println("Tarefa não encontrada");

            return;
        }

        System.out.println("Nome da Etapa: ");
        String nomeEtapa = scan.nextLine();

        System.out.println("Peso da etapa (%): ");
        double peso = Double.parseDouble( scan.nextLine());

        Etapa etapa = new Etapa(nomeEtapa, peso);

        gerenciador.adicionarEtapa(tarefa, etapa);

        System.out.println("Etapa adicionada com sucesso!");
    }

    private void verEtapas(){

        System.out.println("Digite o nome da tarefa: ");

        String nome = scan.nextLine();

        Tarefa tarefa = gerenciador.buscarTarefa(nome);

        if (tarefa != null){

            tarefa.mostrarEtapas();

        }else {

            System.out.println("Tarefa não encontrada");
        }
    }

    private void concluirEtapa(){

        System.out.println("Digite o nome da tarefa: ");

        String nomeTarefa = scan.nextLine();

        Tarefa tarefa = gerenciador.buscarTarefa(nomeTarefa);

        if (tarefa == null){

            System.out.println("Tarefa não encontrada");

            return;
        }

        System.out.println("Digite o nome da etapa: ");
        String nomeEtapa = scan.nextLine();

        Etapa etapa = tarefa.buscarEtapa(nomeEtapa);

        if (etapa != null){

            gerenciador.concluirEtapa(tarefa, etapa);
            System.out.println("Etapa concluida!");

            if (tarefa.isConcluida()){

                darXPDaTarefa(tarefa);

            }

        }else {

            System.out.println("Etapa não encontrada");
        }
    }

    private void verPerfil(){

        System.out.println("===== Ver Perfil =====");
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("XP: " + usuario.getXp());
        System.out.println("Nivel: " + usuario.getNivel());

        gerenciador.listarHabitos();

    }

    private int darXPDaTarefa(Tarefa tarefa) {

        if (tarefa.isXpRecebido()) {

            return 0;
        }

        int xpGanho = sistemaXP.calcularXP(tarefa);
        xpGanho += sistemaXP.bonusTodasTarefasDoDia(gerenciador);

        sistemaXP.adicionarXP(usuario, xpGanho);
        sistemaXP.calcularNivel(usuario);

        tarefa.setXpRecebido(true);
        gerenciador.atualizarXPRecebido(tarefa);
        usuarioRepository.atualizarXP(usuario);

        return xpGanho;
    }

    private void verHoje(){

        System.out.println("===== Ver Hoje =====");

        ArrayList<Tarefa> tarefas = gerenciador.listarHoje();

        if (tarefas.isEmpty()){

            System.out.println("Nenhuma tarefa pra hoje!");

            return;
        }

        double progressoTotal = 0;
        int contador = 1;

        for (Tarefa tarefa : tarefas){

            String status ;

            if (tarefa.isConcluida()){

                status = "✓";

            }else {

                status = " ";
            }

            System.out.println("[" + status + "] " + tarefa.getNome());
            System.out.println(" Categoria: " + tarefa.getCategoria().getNome());
            System.out.println(" Progresso: " + tarefa.calcularProgresso() + "%");
            System.out.println(" Prioridade: " + tarefa.getPrioridade());
            System.out.println("------------------");

            progressoTotal += tarefa.calcularProgresso();

            contador++;
        }

        double progressoDia = progressoTotal / tarefas.size();

        System.out.println("Progresso do dia: " + progressoDia + "%");

        System.out.println();
        System.out.println("Digite 0 para voltar");
        System.out.println("Escolha uma tarefa para concluir");

        int escolha = Integer.parseInt( scan.nextLine());

        if (escolha == 0) {

            return;
        }


        if (escolha > 0 && escolha <= tarefas.size()){

                Tarefa tarefaEscolhida = tarefas.get(escolha - 1);

                if (tarefaEscolhida.isConcluida()){

                    System.out.println("Essa tarefa ja foi concluida!");

                    return;
                }

                gerenciador.finalizarTarefa(tarefaEscolhida);
                int xpGanho = darXPDaTarefa(tarefaEscolhida);

                System.out.println("Tarefa Concluida!");
                System.out.println("XP ganho: " + xpGanho);

            }else {

                System.out.println("Opção Invalida!");
            }
        }





}





