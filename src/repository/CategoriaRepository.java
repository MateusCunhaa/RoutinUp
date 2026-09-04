package repository;

import database.Conexao;
import model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriaRepository {

    public void salvar(Categoria categoria, int usuarioId){

        String sql = """
        INSERT INTO categoria
        (nome, cor, usuario_id)
        VALUES (?, ?, ?)
        RETURNING   id
        """;

        try(Connection conexao = Conexao.conectar();
            PreparedStatement comando = conexao.prepareStatement(sql);) {

            comando.setString(1, categoria.getNome());

            comando.setString(2, categoria.getCor());

            comando.setInt(3, usuarioId);


            var resultado = comando.executeQuery();

            if (resultado.next()){

                categoria.setId(resultado.getInt("id"));

            }

            System.out.println("Categoria Salva! id: " + categoria.getId());

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }

    public ArrayList<Categoria> listarTodos(int usuarioId){

        ArrayList<Categoria> categorias = new ArrayList<>();

        String sql = """    
            SELECT *
            FROM categoria
            WHERE usuario_id IS NULL
            OR usuario_id = ?
            """;

        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setInt(1, usuarioId);

            var resultado = comando.executeQuery();

            while (resultado.next()){

                Categoria categoria = new Categoria(
                        resultado.getString("nome"),
                        resultado.getString("cor")
                );

                categoria.setId(resultado.getInt("id"));

                categorias.add(categoria);
            }
        }catch (SQLException e){

            throw new RuntimeException(e);
        }

        return categorias;
    }



}
