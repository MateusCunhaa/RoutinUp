package repository;

import database.Conexao;

import model.Recorrencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.DayOfWeek;
import java.util.stream.Collectors;

public class RecorrenciaRepository {

    public void salvar (Recorrencia recorrencia, int tarefaId) {

        // Define consulta SQL (Se trata de uma consulta pois possui Returning)
        String sql = """
                INSERT INTO recorrencia (
                    tarefa_id,
                    tipo_recorrencia,
                    intervalo,
                    data_inicio,
                    dias_semana,
                    regra_calendario,
                    dia_do_mes,
                    ultimo_dia_mes,
                    mes_do_ano,
                   posicao_mes,
                   dia_semana_mes
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id    
                """;

    // Conecta ao banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL
    try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {

        // Define atributos/dados que preencherão os "?"
        comando.setInt(1, tarefaId);
        comando.setString(2, recorrencia.getTipoRecorrencia().name());
        comando.setInt(3, recorrencia.getIntervalo());
        comando.setDate(4, java.sql.Date.valueOf(recorrencia.getDataInicio()));

        // Junta todos elementos em uma unica variavel, formata e separa dados por virgula
        String dias = recorrencia.getDiasSemana().stream().map(DayOfWeek::name).collect(Collectors.joining(","));
        comando.setString(5, dias);

        comando.setString(6, recorrencia.getRegraCalendario() != null ? recorrencia.getRegraCalendario().name() : null);
        comando.setObject(7, recorrencia.getDiaDoMes());
        comando.setBoolean(8, recorrencia.isUltimoDiaMes());
        comando.setObject(9, recorrencia.getMesDoAno());
        comando.setString(10, recorrencia.getPosicaoMes() != null ? recorrencia.getPosicaoMes().name() : null);
        comando.setString(11, recorrencia.getDiaSemanaMes() != null ? recorrencia.getDiaSemanaMes().name() : null);

        // Executa consulta SQL (Se trata de uma consulta pois possui Returning)
        var resultado = comando.executeQuery();

        // Se resultado tiver proximo (Busca com ponteiro)
        if (resultado.next()) {
            // Coleta id do banco e guarda na variavel
            int id = resultado.getInt("id");
        }

    // Pega excessão
    } catch (SQLException e) {
        // Comunica excessao
        throw new RuntimeException(e);
    }
}

public Recorrencia buscarPorTarefa(int tarefaId) {

        // Define consulta SQL
        String sql = """
            SELECT *
            FROM recorrencia
            WHERE tarefa_id = ?
            """;

        // Conecta ao banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            // Define atributos/dados que preencherão os "?"
            comando.setInt(1, tarefaId);

            // Executa consulta SQL
            var resultado = comando.executeQuery();

            // Se resultado tiver proximo (Busca com ponteiro)
            if (resultado.next()) {
                // Cria objeto
                Recorrencia recorrencia = new Recorrencia();
                // Atributos do objeto
                // Pega dado do banco de dados transforma em constante do enum e coloca no objeto
                recorrencia.setTipoRecorrencia(model.TipoRecorrencia.valueOf(resultado.getString("tipo_recorrencia")));

                recorrencia.setIntervalo(resultado.getInt("intervalo"));
                recorrencia.setDataInicio(resultado.getDate("data_inicio").toLocalDate());

                // Pega dias do banco de dados
                String dias = resultado.getString("dias_semana");
                // Se dias não forem NULL ou vazio
                if (dias != null && !dias.isEmpty()) {

                    // Enquanto houver elementos, Divide a String toda vez que encontra uma vírgula (Vira varios elementos)
                    for (String dia : dias.split(",")) {
                        // Pega dias do banco de dados, converte para constante enum e Adiciona
                        recorrencia.getDiasSemana().add(DayOfWeek.valueOf(dia));
                    }
                }

                // Pega regra calendario do banco de dados
                String regra = resultado.getString("regra_calendario");
                // Se regra calendario for diferente de NULL
                if (regra != null) {
                    // Converte atributo e atribui a regra calendario
                    recorrencia.setRegraCalendario(Recorrencia.RegraCalendario.valueOf(regra));
                }

                // Pega dia do mes do banco de dados
                int diaDoMes = resultado.getInt("dia_do_mes");
                // Se for diferente de NULL
                if (!resultado.wasNull()) {
                    // Atribui dia do mes
                    recorrencia.setDiaDoMes(diaDoMes);
                }

                // Atribui se ocorrencia é no ultimo dia do mes
                recorrencia.setUltimoDiaMes(resultado.getBoolean("ultimo_dia_mes"));

                // Pega mes do banco de dados
                int mes = resultado.getInt("mes_do_ano");
                // Se for diferente de NULL
                if (!resultado.wasNull()) {
                    // Atribui mes
                    recorrencia.setMesDoAno(mes);
                }

                // Pega posicao mes do banco de dados
                String posicao = resultado.getString("posicao_mes");
                // Se for diferente de NULL
                if (posicao != null) {

                    // Converte atributo e atribui a posicao mes
                    recorrencia.setPosicaoMes(Recorrencia.PosicaoMes.valueOf(posicao));
                }

                // Pega dias da semana do banco de dados
                String diaSemana = resultado.getString("dia_semana_mes");
                // Se não for NULL
                if (diaSemana != null) {

                    // Converte atributo e atribui a dias da semana
                    recorrencia.setDiaSemanaMes(DayOfWeek.valueOf(diaSemana));
                }
                // Retorna objeto
                return recorrencia;
            }
            // Retorna NULL
            return null;

        // Pega excessao
        } catch (SQLException e) {
            // Comunica excessao
            throw new RuntimeException(e);
        }
}

public void atualizar(Recorrencia recorrencia, int tarefaId) {

        // Define comando SQL
        String sql = """
            UPDATE recorrencia SET
                tipo_recorrencia = ?,
                intervalo = ?,
                data_inicio = ?,
                dias_semana = ?,
                regra_calendario = ?,
                dia_do_mes = ?,
                ultimo_dia_mes = ?,
                mes_do_ano = ?,
                posicao_mes = ?,
                dia_semana_mes = ?
            WHERE tarefa_id = ?            
                """;

        // Conecta ao banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){

            // Define atributos/dados que preencherão os "?"
            comando.setString(1, recorrencia.getTipoRecorrencia().name());
            comando.setInt(2, recorrencia.getIntervalo());
            comando.setDate(3, java.sql.Date.valueOf(recorrencia.getDataInicio()));

            // Junta todos elementos em uma unica variavel, formata e separa dados por virgula
            String dias = recorrencia.getDiasSemana().stream().map(DayOfWeek::name).collect(Collectors.joining(","));
            comando.setString(4, dias);

            // Se for diferente null de pega o dado e coloca em String, se não coloca null
            comando.setString(5, recorrencia.getRegraCalendario() != null ? recorrencia.getRegraCalendario().name() : null);
            comando.setObject(6, recorrencia.getDiaDoMes());
            comando.setBoolean(7, recorrencia.isUltimoDiaMes());
            comando.setObject(8, recorrencia.getMesDoAno());

            // Se for diferente null de pega o dado e coloca em String, se não coloca null
            comando.setString(9, recorrencia.getPosicaoMes() != null ? recorrencia.getPosicaoMes().name() : null);

            // Se for diferente null de pega o dado e coloca em String, se não coloca null
            comando.setString(10, recorrencia.getDiaSemanaMes() != null ? recorrencia.getDiaSemanaMes().name() : null);
            comando.setInt(11, tarefaId);

            // Executa comando SQL e retorna quantas linhas foram afetadas
            int linhasAfetadas = comando.executeUpdate();

        // Pega excessao
        }catch (SQLException e) {
            // Comunica excessao
            throw new RuntimeException(e);
        }
}

public void remover(int tarefaId) {

        // Define comando SQL e retorna quantas linhas foram afetadas
        String sql = """
            DELETE FROM recorrencia
            WHERE tarefa_id = ?        
        """;

        // Conecta ao banco de dados, prepara (Cria estrutura de comando preparada para receber valores e ser executada pelo banco) SQL
        try (Connection conexao = Conexao.conectar(); PreparedStatement comando = conexao.prepareStatement(sql)){
            // Define atributos/dados que preencherão os "?"
            comando.setInt(1, tarefaId);

            // Executa comando SQL e
            int linhasAfetadas = comando.executeUpdate();

        // Pega excessao
        }catch (SQLException e) {
            // Comunica excessao
            throw new RuntimeException(e);
        }
}





}
