package repository;

import database.Conexao;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioRepository {

    public void salvar(Usuario usuario){

        String sql = """ 
            INSERT INTO usuario     
            (nome, gmail, senha, xp, nivel)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id
            """;

        try(Connection conexao = Conexao.conectar();
        PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setString(1, usuario.getNome());

            comando.setString(2, usuario.getGmail());

            comando.setString(3, usuario.getSenha());

            comando.setInt(4, usuario.getXp());

            comando.setInt(5, usuario.getNivel());

            var resultado = comando.executeQuery();

            if (resultado.next()){

                usuario.setId(resultado.getInt("id"));
            }

            System.out.println("Usuario salvo no banco");

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Usuario buscarLogin(String gmail, String senha){

        String sql = """
            SELECT *
            FROM usuario
            WHERE gmail = ?
            AND senha = ?
            """;

        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setString(1, gmail);
            comando.setString(2, senha);


            var resultado = comando.executeQuery();

            if (resultado.next()){

                Usuario usuario = new Usuario(
                        resultado.getString("nome"),
                        resultado.getString("gmail"),
                        resultado.getString("senha")
                );

                usuario.setId(
                        resultado.getInt("id")
                );

                usuario.setXp(
                        resultado.getInt("xp")
                );

                usuario.setNivel(
                        resultado.getInt("nivel")
                );

                return usuario;

            }
        }catch (SQLException e){

            throw new RuntimeException(e);
        }

        return null;
    }

    public void atualizarXP(Usuario usuario){

        String sql = """
                UPDATE usuario
                SET xp = ?,
                    nivel = ?
                WHERE id = ?
                """;

        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setInt(1, usuario.getXp());

            comando.setInt(2, usuario.getNivel());

            comando.setInt(3, usuario.getId());

            comando.executeUpdate();

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }

    public Usuario buscarPorId(int id){

        String sql = """
                SELECT *
                FROM usuario
                WHERE id = ?
                """;

        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setInt(1, id);

            var resultado =comando.executeQuery();

            if (resultado.next()){

                Usuario usuario = new Usuario(
                        resultado.getString("nome"),
                        resultado.getString("gmail"),
                        resultado.getString("senha")
                );

                usuario.setId(
                        resultado.getInt("id")
                );

                usuario.setXp(
                        resultado.getInt("xp")
                );

                usuario.setNivel(
                        resultado.getInt("nivel")
                );

                return usuario;
            }
        }catch (SQLException e){

            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean existeGmail(String gmail){

        String sql = """
                SELECT 1
                FROM usuario
                WHERE gmail = ?
                """;


        try(Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1,gmail);

            var resultado = comando.executeQuery();

            return resultado.next();

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }

    public boolean existeNome(String nome){

        String sql = """
                SELECT 1
                FROM usuario
                WHERE nome = ?
                """;


        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setString(1, nome);

            var reultado = comando.executeQuery();

            return reultado.next();

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }




}
