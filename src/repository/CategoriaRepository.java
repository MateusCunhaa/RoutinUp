//

package repository;

import database.Conexao;
import model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriaRepository {


    // Metodos

    // Insere dados no banco
    public void salvar(Categoria categoria, int usuarioId){

        // Define os dados que vão entrar na tabela
        String sql = """
        INSERT INTO categoria
        (nome, cor, icone, usuario_id)
        VALUES (?, ?, ?, ?)
        RETURNING   id
        """;

        // Conecta com o banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL para ser executado nessa conexao
        try(Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql);) {

            // Insere os dados
            comando.setString(1, categoria.getNome()); // (posição do parametro ("?"), Valor que sera associado

            comando.setString(2, categoria.getCor());

            comando.setString(3, categoria.getIcone());

            comando.setInt(4, usuarioId);

            // Executa query (retorna ResultSet)
            var resultado = comando.executeQuery();

            // Procura ID da categoria
            if (resultado.next()){

                categoria.setId(resultado.getInt("id"));

            }

            System.out.println("Categoria Salva! id: " + categoria.getId());

        // pega excessão
        }catch (SQLException e){

            // Comunica excessão
            throw new RuntimeException(e);
        }
    }

    // Faz pesquisa SQL pra ver todas categorias e seus atributos
    public ArrayList<Categoria> listarTodos(int usuarioId){

        // Cria lista
        ArrayList<Categoria> categorias = new ArrayList<>();

        // Define consulta SQL
        String sql = """    
            SELECT *
            FROM categoria
            WHERE usuario_id IS NULL
            OR usuario_id = ?
            """;

        // Conecta com o banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL para ser executado nessa conexao
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Insere Dados/atributos para pesquisa em comando (define valor do parametro "?")
            comando.setInt(1, usuarioId);

            // Executa query
            var resultado = comando.executeQuery();

            // Pega todas categorias (enquanto houver proxima)
            while (resultado.next()){

                Categoria categoria = new Categoria(
                        resultado.getString("nome"),
                        resultado.getString("cor"),
                        resultado.getString("icone")
                );

                // Pega dados do banco e coloca dentro do objeto categoria
                categoria.setId(resultado.getInt("id"));
                int donoId = resultado.getInt("usuario_id");
                categoria.setUsuarioId(resultado.wasNull() ? 0 : donoId);


                categorias.add(categoria);
            }
        // Pega excessão
        }catch (SQLException e){

            // Comunica excessão
            throw new RuntimeException(e);
        }

        // Retorna categorias (arraylist com todas categorias)
        return categorias;
    }

    // Consulta o banco para verificar se determinado nome já está sendo utilizado no conjunto de categorias do usuário (globais e particulares).
    public boolean existeNomeVisivelParaUsuario(String nome, int usuarioId) {

       // Define consulta SQL
        String sql = """
        SELECT 1
        FROM categoria
        WHERE LOWER(TRIM(nome)) = LOWER(TRIM(?))
          AND (usuario_id IS NULL OR usuario_id = ?)
        LIMIT 1
        """;

        // Conecta com o banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL para ser executado nessa conexao
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {

            // Insere Dados/atributos para pesquisa em comando (define valor do parametro "?")
            comando.setString(1, nome);
            comando.setInt(2, usuarioId);

            // Executa query
            var resultado = comando.executeQuery();

            // Verifica oque a consulta encontrou
            return resultado.next();

            // Pega excessão
        } catch (SQLException e) {

            // Comunica excessão
            throw new RuntimeException(e);
        }
    }

    // Verifica se já existe uma categoria com o mesmo nome durante a edição
    public boolean existeNomeVisivelParaUsuarioExcetoId(String nome, int usuarioId, int categoriaId) {

        // Define consulta SQL
        String sql = """
        SELECT 1
        FROM categoria
        WHERE LOWER(TRIM(nome)) = LOWER(TRIM(?))
          AND (usuario_id IS NULL OR usuario_id = ?)
          AND id <> ?
        LIMIT 1
        """;

        // Conecta com o banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL para ser executado nessa conexao
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {

            // Insere Dados/atributos para pesquisa em comando (define valor do parametro "?")
            comando.setString(1, nome);
            comando.setInt(2, usuarioId);
            comando.setInt(3, categoriaId);

            // Executa query
            var resultado = comando.executeQuery();

            // Avança o cursor para a primeira linha e verifica se a consulta retornou algum registro
            return resultado.next();

            // Pega a excessão
        } catch (SQLException e) {

            // Comunica a excessão
            throw new RuntimeException(e);
        }
    }


    public boolean atualizarPersonalizada(
            Categoria categoria,
            int usuarioId
    ) {
        // Define consulta SQL
        String sql = """
        UPDATE categoria
        SET nome = ?,
            cor = ?,
            icone = ?
        WHERE id = ?
          AND usuario_id = ?
        RETURNING id
        """;

        // Conecta ao banco de dados, Prepara  (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) comando SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {

           // Insere Dados/atributos para pesquisa em comando (define valor do parametro "?")
            comando.setString(1, categoria.getNome());
            comando.setString(2, categoria.getCor());
            comando.setString(3, categoria.getIcone());
            comando.setInt(4, categoria.getId());
            comando.setInt(5, usuarioId);

            // Executa consulta e retorna ResultSet
            var resultado = comando.executeQuery();

            // Avança o cursor para a primeira linha e verifica se a consulta retornou algum registro
            return resultado.next();

            // Pega excessão
        } catch (SQLException e) {

            // Comunica excessão
            throw new RuntimeException(e);
        }
    }


    public boolean removerPersonalizada(
            int categoriaId,
            int usuarioId
    ) {
        // Define consulta SQL
        String sql = """
        DELETE FROM categoria
        WHERE id = ?
          AND usuario_id = ?
        RETURNING id
        """;

        //  Conecta ao banco de dados, Prepara  (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) comando SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {

            // Insere Dados/atributos para pesquisa em comando (define valor do parametro "?")
            comando.setInt(1, categoriaId);
            comando.setInt(2, usuarioId);

            // Executa consulta
            var resultado = comando.executeQuery();

            // Avança o cursor para a primeira linha e verifica se a consulta retornou algum registro
            return resultado.next();

            // Pega excessao
        } catch (SQLException e) {

            // Comunica excessao
            throw new RuntimeException(e);
        }
    }




}
