package repository;

import model.Etapa;
import database.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class EtapaRepository {

    public void salvar(Etapa etapa, int tarefaid){

        String sql = """
            INSERT INTO etapa
            (
            nome,
            peso,
            concluida,
            tarefa_id
            )
            VALUES (?, ?, ?, ?)
            """;


        try(Connection conexao = Conexao.conectar();PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, etapa.getNome());

            comando.setDouble(2, etapa.getPeso());

            comando.setBoolean(3, etapa.isConcluida());

            comando.setInt(4, tarefaid);


            comando.executeUpdate();

            System.out.println("Etapa salva no banco!");

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }

    public ArrayList<Etapa> listarPorTarefa(int tarefa_Id){

        ArrayList<Etapa> etapas = new ArrayList<>();

        String sql = """
                SELECT * 
                FROM etapa
                WHERE tarefa_id = ?
                """;

        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setInt(1, tarefa_Id);

            var resultado = comando.executeQuery();

            while (resultado.next()){

                Etapa etapa = new Etapa(
                        resultado.getString("nome"),
                        resultado.getDouble("peso")
                );

                etapa.setId(resultado.getInt("id"));

                etapa.setConcluida(
                        resultado.getBoolean("concluida")
                );

                etapas.add(etapa);
            }
        }catch (SQLException e){

            throw new RuntimeException(e);
        }

        return etapas;
    }

    public void atualizarConclusao(Etapa etapa){

        String sql = """
                UPDATE etapa
                SET concluida = ?
                WHERE id = ?
                """;

        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setBoolean(1,etapa.isConcluida());

            comando.setInt(2, etapa.getId());

            comando.executeUpdate();

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }

    public void removerPorTarefa(int tarefaId){

        String sql = """
                DELETE FROM etapa
                WHERE tarefa_id = ?
                """;

        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setInt(1, tarefaId);

            comando.executeUpdate();

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }





}
