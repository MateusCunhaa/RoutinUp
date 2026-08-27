package view;

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



    public MenuSistema(){
        scan = new Scanner(System.in);

        gerenciador = new GerenciadorTarefas();
        gerenciadorCategorias = new GerenciadorCategorias();
        usuarioRepository = new UsuarioRepository();
        usuario = usuarioRepository.buscarPorId(2);

        usuario = new Usuario(
                "usuario",
                "nome@gmail.com",
                "123"
        );

        sistemaXP = new SistemaXP();
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
            System.out.println("11 - Sair");

            System.out.println("Escolha uma opção:");

            opcao = scan.nextInt();

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

                case 9:

                    concluirTarefa();

                    break;

                case 10:

                    verPerfil();

                    break;

                case 11:

                    System.out.println("Saindo do RoutinUp...");

                break;


                default:

                    System.out.println("Opção invalida");
            }

        }while (opcao != 11);
    }

    private void criarTarefa(){

        scan.nextLine();

        System.out.println("Nome da Tarefa : ");
        String nome = scan.nextLine();

        System.out.println("Descrição : ");
        String descricao = scan.nextLine();

        System.out.println("Horario : ");
        String horario = scan.nextLine();

        System.out.println("Duração em Minutos : ");
        double duracao = scan.nextDouble();

        System.out.println("Prioridade (0 a 5) : ");
        double prioridade = scan.nextDouble();

        System.out.println("Escolha uma Categoria : ");
        gerenciadorCategorias.listarCategorias();
        System.out.println("7 - Criar Nova Categoria" );

        int escolhaCategoria = scan.nextInt();

        Categoria categoriaEscolhida;

        if (escolhaCategoria == 7){

            criarNovaCategoria();
            System.out.println("Escolha novamente a categoria: ");
            gerenciadorCategorias.listarCategorias();
            escolhaCategoria = scan.nextInt();

        }

        categoriaEscolhida = gerenciadorCategorias.buscarCategoria(escolhaCategoria);

        System.out.println("A tarefa se repete?");

        System.out.println("1 - Sim");
        System.out.println("2 - Não");

        int escolhaRepeticao = scan.nextInt();

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

        novaTarefa.setRepeticao(repeticao);

        if (repeticao){

            System.out.println("Quantos dias deseja adicionar: ");
            int quantidadeDias = scan.nextInt();

            System.out.println("1 - Domingo");
            System.out.println("2 - Segunda");
            System.out.println("3 - Terça");
            System.out.println("4 - Quarta");
            System.out.println("5 - Quintq");
            System.out.println("6 - Sexta");
            System.out.println("7 - Sabado");

            for (int i = 0; i < quantidadeDias; i++){

                System.out.println("Escolha o dia:");
                int dia = scan.nextInt();

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

        scan.nextLine();

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

        scan.nextLine();

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

        int escolha = scan.nextInt();

        switch (escolha){

            case 1:

                scan.nextLine();
                System.out.println("Novo nome: ");
                String novoNome = scan.nextLine();
                gerenciador.editarNome(tarefa, novoNome);

                break;

            case 2:

                scan.nextLine();
                System.out.println("Nova Descrição: ");
                String novoDescricao = scan.nextLine();
                gerenciador.editarDescricao(tarefa, novoDescricao);

                break;

            case 3:

                scan.nextLine();
                System.out.println("Novo horario: ");
                String novoHorario = scan.nextLine();
                gerenciador.editarHorario(tarefa, novoHorario);

                break;

            case 4:

                System.out.println("Nova duração: ");
                double novoDuracao = scan.nextDouble();
                gerenciador.editarDuracao(tarefa, novoDuracao);

                break;

            case 5:

                System.out.println("Nova Prioridade: ");
                double novoPrioridade = scan.nextDouble();
                gerenciador.editarPrioridade(tarefa, novoPrioridade);

                break;

            default:

                System.out.println("Opção invalida");

        }

        System.out.println("Tarefa editada com sucesso!");
    }

    public void removerTarefa(){

        scan.nextLine();

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

        scan.nextLine();

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

        gerenciador.concluirTarefa(tarefa);

        int xpGanho = sistemaXP.calcularXP(tarefa);

        tarefa.setXpRecebido(true);

        xpGanho +=sistemaXP.bonusTodasTarefasDoDia(gerenciador);
        xpGanho += sistemaXP.bonusNovoHabito(tarefa);


        sistemaXP.adicionarXP(usuario, xpGanho);
        sistemaXP.calcularNivel(usuario);
        usuarioRepository.atualizarXP(usuario);

        System.out.println("Tarefa Concluida!");
        System.out.println("XP ganho: " + xpGanho);
        System.out.println("Nivel Atual: " + usuario.getNivel());

    }

    private void criarNovaCategoria(){

        scan.nextLine();

        System.out.println("Nome da Categoria : ");
        String nome = scan.nextLine();

        System.out.println("Cor da Categoria : ");
        String cor = scan.nextLine();

        gerenciadorCategorias.criarCategoria(nome, cor);

        System.out.println("Categoria Criada com Sucesso");
    }

    private void adicionarEtapa(){

        scan.nextLine();

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
        double peso = scan.nextDouble();

        Etapa etapa = new Etapa(nomeEtapa, peso);

        gerenciador.adicionarEtapa(tarefa, etapa);

        System.out.println("Etapa adicionada com sucesso!");
    }

    private void verEtapas(){

        scan.nextLine();

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

        scan.nextLine();

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




}
