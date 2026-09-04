package service;

import model.Usuario;
import model.Categoria;
import java.util.ArrayList;
import repository.CategoriaRepository;

public class GerenciadorCategorias {

    private ArrayList<Categoria> categorias;
    private CategoriaRepository categoriaRepository;
    private Usuario usuario;


    public void adicionarCategoria(Categoria categoria){

        categorias.add(categoria);

    }

    public GerenciadorCategorias(Usuario usuario){

        this.usuario = usuario;
        categoriaRepository = new CategoriaRepository();
        categorias = categoriaRepository.listarTodos(usuario.getId());

        if (categorias.isEmpty()) {

            criarCategoria("Saúde", "Verde" );
            criarCategoria("Estudos", "Azul Claro" );
            criarCategoria("Trabalho", "Roxo" );
            criarCategoria("Finanças", "Amarelo" );
            criarCategoria("Casa", "Laranja" );
            criarCategoria("Lazer", "Rosa" );
            criarCategoria("Esporte", "Azul Escuro");
        }
    }

    public void listarCategorias(){

        for (int i = 0; i < categorias.size(); i++){

            System.out.println((i + 1) + " - " + categorias.get(i).getNome());
        }
    }

    public Categoria buscarCategoria(int escolha){

        if (escolha <= 0 || escolha > categorias.size()){

            return null;
        }

        return categorias.get(escolha - 1);
    }

    public void criarCategoria(String nome, String cor){

        Categoria novaCategoria = new Categoria(nome, cor);

        categorias.add(novaCategoria);

        categoriaRepository.salvar(
                novaCategoria,
                usuario.getId()
        );
    }

    public  int quantidadeCategoria(){

        return categorias.size();

    }


}
