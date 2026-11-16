package repository;

import database.Conexao;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioRepository {


    // Metodos
    public void salvar(Usuario usuario){

        // Define comando SQL
        String sql = """ 
            INSERT INTO usuario     
            (nome, gmail, senha, xp, nivel)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id
            """;

        // Conecta ao banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL
        try(Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Insere dados/atributos no comando SQL (define valor do parametro "?")
            comando.setString(1, usuario.getNome());
            comando.setString(2, usuario.getGmail());
            comando.setString(3, usuario.getSenha());
            comando.setInt(4, usuario.getXp());
            comando.setInt(5, usuario.getNivel());

            // Executa consulta SQL
            var resultado = comando.executeQuery();

            // Se tiver proximo na lista (Busca com ponteiro)
            if (resultado.next()){

                // Pega valor do banco de dados e atribui ao objeto usuario
                usuario.setId(resultado.getInt("id"));
            }
        // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }
    }

    public Usuario buscarLogin(String gmail, String senha){

        // Define SQL
        String sql = """
            SELECT *
            FROM usuario
            WHERE gmail = ?
            AND senha = ?
            """;

        // Conecta ao banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Insere dados/atributos no comando SQL (define valor do parametro "?")
            comando.setString(1, gmail);
            comando.setString(2, senha);

            // Executa consulta SQL
            var resultado = comando.executeQuery();

            // SE Se tiver proximo na lista (Busca com ponteiro)
            if (resultado.next()){

                // Cria objeto usuario, pega seus atributos do banco de dados
                Usuario usuario = new Usuario(
                        resultado.getString("nome"),
                        resultado.getString("gmail"),
                        resultado.getString("senha")
                );

                // Pega atributos do banco e insere no objeto
                usuario.setId(resultado.getInt("id"));
                usuario.setXp(resultado.getInt("xp"));
                usuario.setNivel(resultado.getInt("nivel"));

                // Retorna objeto usuario
                return usuario;

            }
        // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }

        // Retorna null
        return null;
    }

    public Usuario buscarPorId(int id){

        // Define comando SQL
        String sql = """
                SELECT *
                FROM usuario
                WHERE id = ?
                """;
        // Conecta ao banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Insere dados/atributos no comando SQL (define valor do parametro "?")
            comando.setInt(1, id);

            // Executa consulta SQL
            var resultado = comando.executeQuery();

            // SE Se tiver proximo na lista (Busca com ponteiro)
            if (resultado.next()){

                // Cria objeto usuario, pega seus atributos do banco de dados
                Usuario usuario = new Usuario(
                        resultado.getString("nome"),
                        resultado.getString("gmail"),
                        resultado.getString("senha")
                );

                // Pega atributos do banco e insere no objeto
                usuario.setId(resultado.getInt("id"));
                usuario.setXp(resultado.getInt("xp"));
                usuario.setNivel(resultado.getInt("nivel"));

                // Retorna objeto usuario
                return usuario;
            }
        // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }

        // Retorna null
        return null;
    }

    public boolean existeGmail(String gmail){

        // Define comando SQL
        String sql = """
                SELECT 1
                FROM usuario
                WHERE gmail = ?
                """;

        // Conecta ao banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL
        try(Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {

            // Insere dados/atributos no comando SQL (define valor do parametro "?")
            comando.setString(1,gmail);

            // Executa consulta SQL
            var resultado = comando.executeQuery();

            // Tenta avançar ponteiro e retorna boolean (true ou false)
            return resultado.next();

        // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }
    }

    public boolean existeNome(String nome){

        // Define comando SQL
        String sql = """
                SELECT 1
                FROM usuario
                WHERE nome = ?
                """;

        // Conecta ao banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Insere dados/atributos no comando SQL (define valor do parametro "?")
            comando.setString(1, nome);

            // Executa consulta SQL
            var reultado = comando.executeQuery();

            // Tenta avançar ponteiro e retorna boolean (true ou false)
            return reultado.next();

        // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }
    }

    public void atualizarXP(Usuario usuario){

        // Define comando SQL
        String sql = """
                UPDATE usuario
                SET xp = ?,
                    nivel = ?
                WHERE id = ?
                """;
        // Conecta ao banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Insere dados/atributos no comando SQL (define valor do parametro "?")
            comando.setInt(1, usuario.getXp());
            comando.setInt(2, usuario.getNivel());
            comando.setInt(3, usuario.getId());
            comando.executeUpdate();

        // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }
    }




}
