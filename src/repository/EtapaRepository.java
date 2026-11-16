package repository;

import model.Etapa;
import database.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class EtapaRepository {


    // Metodos
    public void salvar(Etapa etapa, int tarefaid){

        // Define comando SQL
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

        // Conecta ao banco de dados, Prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) comando SQL
        try(Connection conexao = Conexao.conectar();PreparedStatement comando = conexao.prepareStatement(sql)) {

            //Insere Dados/atributos para comando SQL (define valor do parametro "?")
            comando.setString(1, etapa.getNome());
            comando.setDouble(2, etapa.getPeso());
            comando.setBoolean(3, etapa.isConcluida());
            comando.setInt(4, tarefaid);

            // Executa comando SQL
            comando.executeUpdate();

            System.out.println("Etapa salva no banco!");

            // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Etapa> listarPorTarefa(int tarefa_Id){

        // Cria lista
        ArrayList<Etapa> etapas = new ArrayList<>();

        // Define consulta SQL
        String sql = """
                SELECT * 
                FROM etapa
                WHERE tarefa_id = ?
                """;

        // Conecta ao banco de dados, Prepara  (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) comando SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Insere dados/atributos para comando SQL (define valor do parametro "?")
            comando.setInt(1, tarefa_Id);

            // Executa consulta
            var resultado = comando.executeQuery();

            // enquanto tiver proximo na lista de resultados (consulta)
            while (resultado.next()){

                // Pega atributos nome e peso
                Etapa etapa = new Etapa(
                        resultado.getString("nome"),
                        resultado.getDouble("peso")
                );

                // Insere id e concluida (sim ou não)
                etapa.setId(resultado.getInt("id"));
                etapa.setConcluida(resultado.getBoolean("concluida"));

                // Adiciona etapa a lista etapas
                etapas.add(etapa);
            }
            // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }

        // retorna lista
        return etapas;
    }

    public void removerPorTarefa(int tarefaId){

        // Define comando SQL
        String sql = """
                DELETE FROM etapa
                WHERE tarefa_id = ?
                """;

        // Conecta ao banco de dados, Prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) comando SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Insere dados/atributos no comando SQL (define valor do parametro "?")
            comando.setInt(1, tarefaId);

            // Executa comando
            comando.executeUpdate();

            // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }
    }

    public void atualizarConclusao(Etapa etapa){

        // Define comando SQL
        String sql = """
                UPDATE etapa
                SET concluida = ?
                WHERE id = ?
                """;

        // Conecta com banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) comando SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Insere dados/atributos no comando SQL (define valor do parametro "?")
            comando.setBoolean(1,etapa.isConcluida());
            comando.setInt(2, etapa.getId());

            // Executa comando
            comando.executeUpdate();

            // Pega excessao
        }catch (SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }
    }





}
