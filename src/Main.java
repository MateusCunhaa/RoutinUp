import model.Categoria;
import model.Etapa;
import model.Tarefa;
import model.Usuario;
import repository.EtapaRepository;
import repository.TarefaRepository;
import repository.UsuarioRepository;
import repository.CategoriaRepository;
import service.GerenciadorTarefas;
import view.LoginSistema;
import view.MenuSistema;
import view.CadastroSistema;
import database.Conexao;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        boolean executando = true;

        while (executando) {

            System.out.println("===== RoutinUp =====");
            System.out.println("1 - Login");
            System.out.println("2 - Criar Conta");
            System.out.println("3 - Sair");
            System.out.println("Escolha: ");

            int escolha = Integer.parseInt( scan.nextLine());

            switch (escolha){

                case 1:

                    LoginSistema loginSistema = new LoginSistema(scan);
                    Usuario usuario = null;

                    while (usuario == null){

                        usuario = loginSistema.login();

                        if (usuario == null){

                            System.out.println();
                            System.out.println("Deseja tentar novamente?");
                            System.out.println("1 - Sim");
                            System.out.println("2 - Não");

                            int opcaoLogin = Integer.parseInt( scan.nextLine());

                            if (opcaoLogin == 2){
                                break;
                            }
                        }
                    }

                    if (usuario != null){

                        MenuSistema menu = new MenuSistema(usuario, scan);
                        menu.iniciar();
                    }
                    break;

                case 2:

                    CadastroSistema cadastro = new CadastroSistema(scan);
                    cadastro.cadastrar();

                    System.out.println();
                    System.out.println("Agora faça o login pra entra no RoutinUp!");

                    break;

                case 3:

                    System.out.println("Encerrando RoutinUp ...");
                    executando = false;

                    break;

                default:

                    System.out.println("Opção Invalida!");
            }
        }

        scan.close();

    }
}





