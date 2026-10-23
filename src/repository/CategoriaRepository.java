package repository;

import database.Conexao;
import model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriaRepository {


    // Metodos
    public void salvar(Categoria categoria, int usuarioId){

        String sql = """
        INSERT INTO categoria
        (nome, cor, icone, usuario_id)
        VALUES (?, ?, ?, ?)
        RETURNING   id
        """;

        try(Connection conexao = Conexao.conectar();
            PreparedStatement comando = conexao.prepareStatement(sql);) {

            comando.setString(1, categoria.getNome());

            comando.setString(2, categoria.getCor());

            comando.setString(3, categoria.getIcone());

            comando.setInt(4, usuarioId);


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
                        resultado.getString("cor"),
                        resultado.getString("icone")
                );

                categoria.setId(resultado.getInt("id"));
                int donoId = resultado.getInt("usuario_id");
                categoria.setUsuarioId(resultado.wasNull() ? 0 : donoId);


                categorias.add(categoria);
            }
        }catch (SQLException e){

            throw new RuntimeException(e);
        }

        return categorias;
    }


    public boolean existeNomeVisivelParaUsuario(
            String nome,
            int usuarioId
    ) {
        String sql = """
        SELECT 1
        FROM categoria
        WHERE LOWER(TRIM(nome)) = LOWER(TRIM(?))
          AND (usuario_id IS NULL OR usuario_id = ?)
        LIMIT 1
        """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {
            comando.setString(1, nome);
            comando.setInt(2, usuarioId);

            var resultado = comando.executeQuery();
            return resultado.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean existeNomeVisivelParaUsuarioExcetoId(
            String nome,
            int usuarioId,
            int categoriaId
    ) {
        String sql = """
        SELECT 1
        FROM categoria
        WHERE LOWER(TRIM(nome)) = LOWER(TRIM(?))
          AND (usuario_id IS NULL OR usuario_id = ?)
          AND id <> ?
        LIMIT 1
        """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {
            comando.setString(1, nome);
            comando.setInt(2, usuarioId);
            comando.setInt(3, categoriaId);

            var resultado = comando.executeQuery();

            return resultado.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean atualizarPersonalizada(
            Categoria categoria,
            int usuarioId
    ) {
        String sql = """
        UPDATE categoria
        SET nome = ?,
            cor = ?,
            icone = ?
        WHERE id = ?
          AND usuario_id = ?
        RETURNING id
        """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {
            comando.setString(1, categoria.getNome());
            comando.setString(2, categoria.getCor());
            comando.setString(3, categoria.getIcone());
            comando.setInt(4, categoria.getId());
            comando.setInt(5, usuarioId);

            var resultado = comando.executeQuery();

            return resultado.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean removerPersonalizada(
            int categoriaId,
            int usuarioId
    ) {
        String sql = """
        DELETE FROM categoria
        WHERE id = ?
          AND usuario_id = ?
        RETURNING id
        """;

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {
            comando.setInt(1, categoriaId);
            comando.setInt(2, usuarioId);

            var resultado = comando.executeQuery();

            return resultado.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





}
