package repository;

import database.Conexao;
import model.Etapa;
import model.Recorrencia;
import model.Tarefa;
import model.Categoria;
import service.GerenciadorTarefas;
import repository.RecorrenciaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TarefaRepository {


    // Metodos
    public void salvar(Tarefa tarefa){

        // Define comando SQL
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
        sequencia,
        data_conclusao,
        bonus_habito_recebido, 
        categoria_id,
        xp_recebido
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)  
        RETURNING id  -- devolve ID da tarefa
        """;


        // Conecta ao banco de dados, Prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) comando SQL
        try(Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {

            // Insere dados/atributos no comando SQL (define valor do parametro "?")
            comando.setInt(1, tarefa.getUsuarioId());
            comando.setString(2, tarefa.getNome());
            comando.setString(3, tarefa.getDescricao());
            comando.setString(4, tarefa.getHorario());
            comando.setDouble(5, tarefa.getDuracao());
            comando.setDouble(6, tarefa.getPrioridade());
            comando.setDouble(7, tarefa.getPorcentagem());
            comando.setBoolean(8, tarefa.isConcluida());
            comando.setInt(9, tarefa.getSequencia());

            // Se data conclusão não estiver vazia
            if (tarefa.getDataConclusao() != null) {

                // Pega data de conclusao e define como valor do parametro (?)
                comando.setDate(10, java.sql.Date.valueOf(tarefa.getDataConclusao()));
                // se não
            } else {

                // Define parametro (?) como null
                comando.setDate(10, null);
            }

            comando.setBoolean(11, tarefa.isBonusHabitoRecebido());

            if (tarefa.getCategoria() != null) {
                comando.setInt(12, tarefa.getCategoria().getId());
            }else {

                comando.setNull(12, java.sql.Types.INTEGER);
            }

            comando.setBoolean(13, tarefa.isXpRecebido());

            // executa comando SQL
            var resultado = comando.executeQuery();

            // Se resultado tiver proximo (Busca com ponteiro)
            if (resultado.next()){

                // Pega o Id do banco de dados e coloca no objeto
                tarefa.setId(resultado.getInt("id"));
            }


        // Pega excessao
        }catch(SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }
        // Cria objeto
        RecorrenciaRepository recorrenciaRepository = new RecorrenciaRepository();

        // Salva recorrencia puxado pelo Id da tarefa
        recorrenciaRepository.salvar(tarefa.getRecorrencia(), tarefa.getId());
    }

    public void atualizar(Tarefa tarefa){

        // Define comando SQL
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

        // Conecta ao banco de dados, Prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) comando SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Insere dados/atributos no comando SQL (define valor do parametro "?")
            comando.setString(1, tarefa.getNome());
            comando.setString(2, tarefa.getDescricao());
            comando.setString(3, tarefa.getHorario());
            comando.setDouble(4, tarefa.getDuracao());
            comando.setDouble(5, tarefa.getPrioridade());

            // Se categoria for diferente de null
            if (tarefa.getCategoria() != null) {

                // Pega categoria pelo id
                comando.setInt(6, tarefa.getCategoria().getId());
            // Se não
            } else {

                // Define categoria como null
                comando.setNull(6, Types.INTEGER);
            }
            comando.setInt(7, tarefa.getId());

            // Executa comando SQL
            comando.executeUpdate();

        // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }
    }

    public void atualizarConclusao(Tarefa tarefa){

        // Define comando SQL
        String sql = """
                UPDATE tarefa
                SET concluida = ?, 
                    sequencia = ?,
                    xp_recebido = ?,
                    data_conclusao = ?          
                WHERE id = ?
                """;

        // Conecta ao banco de dados, Prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) comando SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Insere dados/atributos no comando SQL (define valor do parametro "?")
            comando.setBoolean(1, tarefa.isConcluida());
            comando.setInt(2, tarefa.getSequencia());
            comando.setBoolean(3, tarefa.isXpRecebido());

            comando.setDate(4, java.sql.Date.valueOf(tarefa.getDataConclusao()));

            comando.setInt(5, tarefa.getId());

            // Executa comando SQL
            comando.executeUpdate();

        // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }
    }

    public void remover(Tarefa tarefa){

        // Define comando SQL
        String sql = """
                DELETE FROM tarefa
                WHERE id = ?
                """;

        // Conecta ao banco de dados, Prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) comando SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Insere dados/atributos no comando SQL (define valor do parametro "?")
            comando.setInt(1, tarefa.getId());

            // Executa comando SQL
            comando.executeUpdate();

        // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Tarefa> listarTodos(int usuarioId){

        // Cria lista
        ArrayList<Tarefa> tarefas = new ArrayList<>();

        // Define comando SQL
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

        // Conecta ao banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) comando SQL
        try(Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {

            // Define atributos/dados que preencherão os "?"
            comando.setInt(1, usuarioId);

            // Executa consulta SQL
            var resultado = comando.executeQuery();

            // Enquanto a lista resultado tiver proximo (Busca com o ponteiro)
            while (resultado.next()){

                // Cria objeto e guarda objeto tarefa vindo de montarTarefa
                Tarefa tarefa = montarTarefa(resultado);

                // Adiciona tarefa a lista
                tarefas.add(tarefa);


            }
            // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }

        // retorna lista tarefas
    return tarefas;

    }

    private Tarefa montarTarefa(ResultSet resultado){

        // Cria ob jeto categoria como null
        Categoria categoria = null;


        try{

            // SE categoria_nome não for null
            if (resultado.getString("categoria_nome") != null ){

                // Cria objeto, pega atributos do banco, define como atributos do objeto
                categoria = new Categoria(resultado.getString("categoria_nome"), resultado.getString("categoria_cor"));
                categoria.setId(resultado.getInt("categoria_id"));
            }

            // Cria objeto Tarefa com atributos que vieram do banco de dados
            Tarefa tarefa = new Tarefa(
                    resultado.getString("nome"),
                    resultado.getString("descricao"),
                    resultado.getString("horario"),
                    resultado.getDouble("duracao"),
                    resultado.getDouble("prioridade"),
                    categoria
            );

            // Pega valor do banco de dados e atribui ao objeto tarefa
            tarefa.setId(resultado.getInt("id"));
            tarefa.setUsuarioId(resultado.getInt("usuario_id"));
            tarefa.setPorcentagem(resultado.getDouble("porcentagem"));
            tarefa.setBonusHabitoRecebido(resultado.getBoolean("bonus_habito_recebido"));
            tarefa.setConcluida(resultado.getBoolean("concluida"));
            tarefa.setSequencia(resultado.getInt("sequencia"));
            tarefa.setXpRecebido(resultado.getBoolean("xp_recebido"));

            // Cria objeto
            RecorrenciaRepository recorrenciaRepository = new RecorrenciaRepository();
            // Salva na variavel recorrencia puxada pelo id da tarefa
            Recorrencia recorrencia = recorrenciaRepository.buscarPorTarefa(tarefa.getId());
            // Se recorrencia não for null
            if (recorrencia != null ) {
                tarefa.setRecorrencia(recorrencia);
            }
            java.sql.Date data =resultado.getDate("data_conclusao");

            // Se data não for null
            if (data != null){

                tarefa.setDataConclusao(data.toLocalDate());
            }

            // Cria objeto etapaRepository
            EtapaRepository etapaRepository = new EtapaRepository();

            // Cria lista etapas e guarda nela etapas relacionadas a uma tarefa, essas etapas vieram do banco de dados
            ArrayList<Etapa> etapas = etapaRepository.listarPorTarefa(tarefa.getId());

            // Atribui essas etapas ao objeto tarefa
            tarefa.setEtapas(etapas);


            // retorna objeto tarefa
            return tarefa;

            // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }
    }




}
