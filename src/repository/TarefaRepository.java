package repository;

import database.Conexao;
import model.Etapa;
import model.Tarefa;
import model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class TarefaRepository {

    public void salvar(Tarefa tarefa){

        String sql = """
        INSERT INTO tarefa
        (
        nome,
        descricao,
        horario,
        duracao,
        prioridade,
        concluida,
        sequencia,
        categoria_id
        xp_recebido
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)  
        RETURNING id
        """;


        try(Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setString(1, tarefa.getNome());

            comando.setString(2, tarefa.getDescricao());

            comando.setString(3, tarefa.getHorario());

            comando.setDouble(4, tarefa.getDuracao());

            comando.setDouble(5, tarefa.getPrioridade());

            comando.setBoolean(6, tarefa.isConcluida());

            comando.setInt(7, tarefa.getSequencia());

            comando.setInt(8, tarefa.getCategoria().getId());

            comando.setBoolean(9, tarefa.isXpRecebido());


            var resultado = comando.executeQuery();

            if (resultado.next()){

                tarefa.setId(resultado.getInt("id"));
            }

            System.out.println("Tarefa salva no banco! ID: " + tarefa.getId());

        }catch(SQLException e){

            throw new RuntimeException(e);
        }
    }

    public ArrayList<Tarefa> listarTodos(){

        ArrayList<Tarefa> tarefas = new ArrayList<>();
        EtapaRepository etapaRepository = new EtapaRepository();

        String sql = """
            Select
                t.*,
                c.nome AS categoria_nome,
                c.cor AS categoria_cor
            FROM tarefa t
            LEFT JOIN categoria c
            ON t.categoria_id = c.id
            """;

        try(Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {

            var resultado = comando.executeQuery();

            while (resultado.next()){

                Categoria categoria = null;

                if (resultado.getString("categoria_nome") != null) {

                    categoria = new Categoria(
                                    resultado.getString("categoria_nome"),
                                    resultado.getString("categoria_cor")
                            );

                    categoria.setId(
                            resultado.getInt("categoria_id")
                    );
                }

                Tarefa tarefa = new Tarefa(
                        resultado.getString("nome"),
                        resultado.getString("descricao"),
                        resultado.getString("horario"),
                        resultado.getDouble("duracao"),
                        resultado.getDouble("prioridade"),
                        categoria

                );

                tarefa.setId(resultado.getInt("id"));

                ArrayList<Etapa> etapas = etapaRepository.listarPorTarefa(tarefa.getId());

                for (Etapa etapa : etapas){

                    tarefa.adicionarEtapa(etapa);
                }

                tarefa.setConcluida(
                        resultado.getBoolean("concluida")
                );

                tarefa.setSequencia(
                        resultado.getInt("sequencia")
                );

                tarefa.setXpRecebido(resultado.getBoolean("xp_recebido"));

                tarefas.add(tarefa);
            }
        }catch (SQLException e){

            throw new RuntimeException(e);
        }

    return tarefas;
    }

    public void atualizarConclusao(Tarefa tarefa){

        String sql = """
                UPDATE tarefa
                SET concluida = ?, 
                    sequencia = ?
                    xp_recebido = ?
                WHERE id = ?
                """;

        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setBoolean(1, tarefa.isConcluida());

            comando.setInt(2, tarefa.getSequencia());

            comando.setBoolean(3, tarefa.isXpRecebido());

            comando.setInt(4, tarefa.getId());


            comando.executeUpdate();

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }



}
