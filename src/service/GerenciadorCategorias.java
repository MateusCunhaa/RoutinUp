package service;

import model.Categoria;
import java.util.ArrayList;
import repository.CategoriaRepository;

public class GerenciadorCategorias {

    private ArrayList<Categoria> categorias;
    private CategoriaRepository categoriaRepository;


    public void adicionarCategoria(Categoria categoria){

        categorias.add(categoria);

    }

    public GerenciadorCategorias(){

        categoriaRepository = new CategoriaRepository();

        categorias = categoriaRepository.listarTodos();

        if (categorias.isEmpty()) {

            criarCategoria("Saúde", "Verde" );
            criarCategoria("Estudos", "Azul" );
            criarCategoria("Trabalho", "Roxo" );
            criarCategoria("Finanças", "Amarelo" );
            criarCategoria("Casa", "Laranja" );
            criarCategoria("Lazer", "Rosa" );
        }
    }

    public void listarCategorias(){

        for (int i = 0; i < categorias.size(); i++){

            System.out.println((i + 1) + " - " + categorias.get(i).getNome());
        }
    }

    public Categoria buscarCategoria(int escolha){

        return categorias.get(escolha - 1);
    }

    public void criarCategoria(String nome, String cor){

        Categoria novaCategoria = new Categoria(nome, cor);

        categorias.add(novaCategoria);

        categoriaRepository.salvar(novaCategoria);
    }


}
