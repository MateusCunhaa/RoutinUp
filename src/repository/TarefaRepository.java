package repository;

import database.Conexao;
import model.Etapa;
import model.Tarefa;
import model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class TarefaRepository {

    public void salvar(Tarefa tarefa){

        String sql = """
        INSERT INTO tarefa
        (
        usuario_id,
        nome,
        descricao,
        horario,
        duracao,
        prioridade,
        porcentagem, 
        concluida,
        repeticao,
        dias_semana, 
        sequencia,
        data_conclusao,
        bonus_habito_recebido, 
        categoria_id,
        xp_recebido
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)  
        RETURNING id
        """;


        try(Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setInt(1, tarefa.getUsuarioId());

            comando.setString(2, tarefa.getNome());

            comando.setString(3, tarefa.getDescricao());

            comando.setString(4, tarefa.getHorario());

            comando.setDouble(5, tarefa.getDuracao());

            comando.setDouble(6, tarefa.getPrioridade());

            comando.setDouble(7, tarefa.getPorcentagem());

            comando.setBoolean(8, tarefa.isConcluida());

            comando.setBoolean(9, tarefa.isRepeticao());

            comando.setString(10, String.join(",", tarefa.getDiasSemana()));

            comando.setInt(11, tarefa.getSequencia());

            if (tarefa.getDataConclusao() != null){

                comando.setDate(12, java.sql.Date.valueOf(tarefa.getDataConclusao()));
            }else {

                comando.setDate(12, null);
            }

            comando.setBoolean(13, tarefa.isBonusHabitoRecebido());

            comando.setInt(14, tarefa.getCategoria().getId());

            comando.setBoolean(15, tarefa.isXpRecebido());


            var resultado = comando.executeQuery();

            if (resultado.next()){

                tarefa.setId(resultado.getInt("id"));
            }

            System.out.println("Tarefa salva no banco! ID: " + tarefa.getId());

        }catch(SQLException e){

            throw new RuntimeException(e);
        }
    }

    public void atualizar(Tarefa tarefa){

        String sql = """
                UPDATE tarefa
                SET nome = ?,
                    descricao = ?,
                    horario = ?,
                    duracao = ?,
                    prioridade = ?,
                    categoria_id = ?
                WHERE id = ?           
                """;

        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setString(1, tarefa.getNome());
            comando.setString(2, tarefa.getDescricao());
            comando.setString(3, tarefa.getHorario());
            comando.setDouble(4, tarefa.getDuracao());
            comando.setDouble(5, tarefa.getPrioridade());
            comando.setInt(6, tarefa.getCategoria().getId());
            comando.setInt(7, tarefa.getId());

            comando.executeUpdate();

            System.out.println("Tarefa Atualizada!");

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }

    public void remover(Tarefa tarefa){

        String sql = """
                DELETE FROM tarefa
                WHERE id = ?
                """;

        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = Conexao.conectar().prepareStatement(sql)){

            comando.setInt(1, tarefa.getId());

            comando.executeUpdate();

            System.out.println("Tarefa removida do banco!");

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }

    public ArrayList<Tarefa> listarTodos(int usuarioId){

        ArrayList<Tarefa> tarefas = new ArrayList<>();

        String sql = """
            Select
                t.*,
                c.nome AS categoria_nome,
                c.cor AS categoria_cor
            FROM tarefa t
            LEFT JOIN categoria c
            ON t.categoria_id = c.id
            WHERE t.usuario_id = ?
            """;

        try(Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, usuarioId);

            var resultado = comando.executeQuery();

            while (resultado.next()){

                Tarefa tarefa = montarTarefa(resultado);

                if (tarefa.aconteceHoje()) {

                    tarefas.add(tarefa);

                }
            }
        }catch (SQLException e){

            throw new RuntimeException(e);
        }

    return tarefas;

    }

    public ArrayList<Tarefa> listarHoje(int usuarioId){

        ArrayList<Tarefa> tarefas = new ArrayList<>();

        String sql = """
            SELECT 
                t.*,
                c.nome AS categoria_nome,
                c. cor AS categoria_cor
            FROM tarefa t
            LEFT JOIN categoria c
            ON t.categoria_id = c.id
            WHERE t.usuario_id = ?               
            """;

        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setInt(1, usuarioId);

            var resultado = comando.executeQuery();

            while (resultado.next()){

                Tarefa tarefa = montarTarefa(resultado);

                if (tarefa.aconteceHoje()) {

                    tarefas.add(tarefa);
                }
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
                    sequencia = ?,
                    xp_recebido = ?,
                    data_conclusao = ?          
                WHERE id = ?
                """;

        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setBoolean(1, tarefa.isConcluida());

            comando.setInt(2, tarefa.getSequencia());

            comando.setBoolean(3, tarefa.isXpRecebido());

            comando.setDate(4, java.sql.Date.valueOf
            (tarefa.getDataConclusao())
            );

            comando.setInt(5, tarefa.getId());


            comando.executeUpdate();

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }

    public ArrayList<Tarefa> listarTarefasDoDia(int usuarioId){

        ArrayList<Tarefa> tarefas = new ArrayList<>();

        String sql = """
                SELECT *
                FROM tarefa
                WHERE usuario_id = ?
                """;


        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            comando.setInt(1, usuarioId);
            var resultado = comando.executeQuery();

            while (resultado.next()){


            }
        }catch (SQLException e){

            throw new RuntimeException(e);
        }

        return tarefas;
    }

    private Tarefa montarTarefa(ResultSet resultado){

        Categoria categoria = null;

        try{

            if (resultado.getString("categoria_nome") != null ){

                categoria = new Categoria(
                        resultado.getString("categoria_nome"),
                        resultado.getString("categoria_cor")
                );

                categoria.setId(resultado.getInt("categoria_id"));
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

            tarefa.setPorcentagem(resultado.getDouble("porcentagem"));

            tarefa.setRepeticao(resultado.getBoolean("repeticao"));

            tarefa.setBonusHabitoRecebido(resultado.getBoolean("bonus_habito_recebido"));

            tarefa.setConcluida(resultado.getBoolean("concluida"));

            tarefa.setSequencia(resultado.getInt("sequencia"));

            tarefa.setXpRecebido(resultado.getBoolean("xp_recebido"));

            java.sql.Date data =resultado.getDate("data_conclusao");

            if (data != null){

                tarefa.setDataConclusao(data.toLocalDate());
            }

            String dias = resultado.getString("dias_semana");

            if (dias != null && !dias.isEmpty()){

                tarefa.getDiasSemana().addAll(Arrays.asList(dias.split(",")));
            }

            EtapaRepository etapaRepository = new EtapaRepository();

            ArrayList<Etapa> etapas = etapaRepository.listarPorTarefa(tarefa.getId());

            for (Etapa etapa : etapas){

                tarefa.adicionarEtapa(etapa);

            }

            return tarefa;

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }




}
