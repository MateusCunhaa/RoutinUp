package service;

import model.Recorrencia;
import model.TipoRecorrencia;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class CalculadoraRecorrencia {

    public boolean aconteceEm(Recorrencia recorrencia, LocalDate data) { // data = data atual
        // Se algum atributo for NULL
        if (recorrencia == null || data == null || recorrencia.getDataInicio() == null) {
            // Retorna false
            return false;
        }
        // Pega data de inicio
        LocalDate inicio = recorrencia.getDataInicio();

        // Se a for antes deinicio
        if(data.isBefore(inicio)){
            // Retorna false
            return false;
        }
        // Verifica se a data informada corresponde ao padrão de recorrência definido
        return correspondeAoPadrao(recorrencia, data);

    }

    private boolean correspondeAoPadrao(Recorrencia recorrencia, LocalDate data) {
        // Pega data de inicio
        LocalDate inicio = recorrencia.getDataInicio();
        // Pega intervalo (Quanto em quanto tempo)
        int intervalo = recorrencia.getIntervalo();

        // Se tipo de recorrencia for null
        if (recorrencia.getTipoRecorrencia() == null) {
            // Retorna falso
            return false;
        }

        // Se tipo de recorrencia for nunca
        if (recorrencia.getTipoRecorrencia() == TipoRecorrencia.NUNCA) {
            // Retorna data de inicio
            return data.equals(inicio);
        }

        // Se intervalo menor ou igual a 0
        if (intervalo <= 0){
            // Retorna False
            return false;
        }

        // Selecionar tipo recorrencia
        switch (recorrencia.getTipoRecorrencia()) {

            // Caso 1: Todos dia ,a cada 2 dias, a cada 15 dias, etc
            case DIARIA:
                // Calcula quantidade de dias entre inicio e data atual (qaunto tempo falta pra proxima execucao), Guarda na variavel dias
                long dias = ChronoUnit.DAYS.between(inicio, data);
                // Confere se divisao tem resto 0 (dias proxima execucao x intervalo)
                return dias % intervalo == 0;

            // Caso 2: Toda sexta, Segunda, quarta e sexta, A cada 2 semanas na sexta-feira
            case SEMANAL:
                // Busca o inicio,domingo anterior ou a propia data caso seja domingo
                LocalDate inicioSemana = inicio.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY) );
                // Busca a data atual,domingo anterior ou a propia data caso seja domingo
                LocalDate semanaAtual = data.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

                // Quantidade de semanas entre inicio e semana atual
                long semanas = ChronoUnit.WEEKS.between(inicioSemana,semanaAtual);

                // Se numero de semanas desde o inicio dividido por intervalo não tiver resto 0
                if (semanas % intervalo != 0) {
                    // Retorna falso
                    return false;
                }

                // Se dias da semana vazio ou NULL
                if (recorrencia.getDiasSemana() == null || recorrencia.getDiasSemana().isEmpty() ) {
                    // Verifica se data atual e data de inicio são o mesmo dia da semana
                    return data.getDayOfWeek() == inicio.getDayOfWeek();
                }



                // Pega dias escolhidos e checa o dia atual (Ver se é o dia escolhido)
                return recorrencia.getDiasSemana().contains(data.getDayOfWeek());

            // Caso 3: nos dias 30 (tem mes que não tem), A cada 2 meses no dia 10,último dia de cada mês.
            case MENSAL:

                // Quantidade de meses entre inicio e mes atual
                long meses = ChronoUnit.MONTHS.between(YearMonth.from(inicio), YearMonth.from(data));

                // Se numero de meses desde o inicio dividido por intervalo não tiver resto 0
                if (meses % intervalo != 0) {
                    // Retorna falso
                    return false;
                }
                // Verifica se a data informada corresponde ao dia do mês definido na recorrência
                return correspondeAoDiaDoMes(recorrencia, data);

            // Caso 4: a cada 2 anos no mes 5 dia 3
            case ANUAL:

                // Quantidade de anos entre inicio e ano atual
                long anos = data.getYear() - inicio.getYear();

                // Se numero de anos desde o inicio dividido por intervalo não tiver resto 0
                if (anos % intervalo != 0) {
                    // Retorna falso
                    return false;
                }

                // Se mes do ano de recorrencia for null ou mes atual diferente de do mes de recorrencia
                if (recorrencia.getMesDoAno() == null || data.getMonthValue() != recorrencia.getMesDoAno()) {
                    // Retorna falso
                    return false;
                }
                // Verifica se a data informada corresponde ao dia do mês definido na recorrência
                return correspondeAoDiaDoMes(recorrencia, data);

            // Caso padrão
            default:
                // Retorna falso
                return false;
        }
    }


    private boolean correspondeAoDiaDoMes(Recorrencia recorrencia, LocalDate data) {

        // Se regra calendario busca dia do mes (ex: 15)
        if (recorrencia.getRegraCalendario() == Recorrencia.RegraCalendario.DIA_DO_MES) {

            // Se for ultimo dia do mes
            if (recorrencia.isUltimoDiaMes()) {

                // Retorna boolean, se dia atual é igual tamanho do mes atual
                return data.getDayOfMonth() == data.lengthOfMonth();
            }

            // Se o dia do mes estiver NULL
            if (recorrencia.getDiaDoMes() == null) {
                // Retorna falso
                return false;
            }

            // Retorna comparação entre o dia do mês da data atual e o dia do mês configurado
            return data.getDayOfMonth() == recorrencia.getDiaDoMes();
        }

        // Se regra calendario busca dia da semana (ex: segunda)
        if (recorrencia.getRegraCalendario() == Recorrencia.RegraCalendario.DIA_DA_SEMANA) {

           // Se dia da semana dentro do mês (ex: Segunda) for NULL ou posicao do mes (ex: Primeira segunda do mes)
            if (recorrencia.getDiaSemanaMes() == null || recorrencia.getPosicaoMes() == null) {
                // Retorna falso
                return false;
            }

            // Se dia do mes for diferente dia da semana dentro do mes
            if (data.getDayOfWeek() != recorrencia.getDiaSemanaMes()) {
                // Retorna falso
                return false;
            }

            // Se posição do mes (Primeira, ULTIMA, etc (semana)) for ultima semana
            if (recorrencia.getPosicaoMes() == Recorrencia.PosicaoMes.ULTIMA) {
                // Adiciona 7 dias (Semana) e verifica qual mes esta ultimo dia dessa semana e compara se esta no mes que estamos
                return data.plusWeeks(1).getMonthValue() != data.getMonthValue();
            }
            // Calcula qual semana do mes uma data esta
            int semana = ((data.getDayOfMonth() - 1) / 7) + 1;
            // Retorna boolean, semana do mês atual está corresponde à posição escolhida na recorrência.
            return semana == recorrencia.getPosicaoMes().getValor();
        }
        // Retorna falso
        return false;
    }


}
