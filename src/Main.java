import model.Categoria;
import model.Etapa;
import model.Tarefa;
import model.Usuario;
import repository.EtapaRepository;
import repository.TarefaRepository;
import repository.UsuarioRepository;
import repository.CategoriaRepository;
import service.GerenciadorTarefas;
import view.MenuSistema;
import database.Conexao;

public class Main {
    public static void main(String[] args) {

        MenuSistema menu = new MenuSistema();
        menu.iniciar();

    }
}





