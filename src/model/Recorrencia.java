package model;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

public class Recorrencia {

    // Metodos

    // enum = Definir quais opções estão disponíveis para uma determinada variável
    public enum RegraCalendario {
        // Constantes (Opções)
        DIA_DO_MES, // ex: dia 19
        DIA_DA_SEMANA // ex: segunda
    }

    // enum = Definir quais opções estão disponíveis para uma determinada variável
    public enum PosicaoMes {
        // Constantes (Opções) associada a um valor
        PRIMEIRA(1), // Primeira semana
        SEGUNDA(2), // Segunda semana
        TERCEIRA(3), // Terceira semana
        QUARTA(4), // Quarta semana
        ULTIMA(-1); // Ultima semana

        // Atributo associado as constantes
        private final int valor;

        // Construtor das constantes
        PosicaoMes(int valor) {
            // Atribui ao Valor da classe o valor do parametro
            this.valor = valor;
        }

        // Get
        public int getValor() {
            // Retorna valor
            return valor;
        }
    }

    // Atrinutos
    private TipoRecorrencia tipoRecorrencia; // Define enum de frequencia (NUNCA, DIARIA, etc)
    private int intervalo; // Define de quanto em quanto tempo ocorre
    private LocalDate dataInicio; // Data que inicia
    private Set<DayOfWeek> diasSemana; // Dias da semana (segunda, terça, etc)
    private RegraCalendario regraCalendario; // Define enum com critério que será utilizado (DIA_DO_MES (10, 21, etc) ou DIA_DA_SEMANA (segunda, sexta, etc))
    private Integer diaDoMes; // Dia do mes (19, 23, etc)
    private boolean ultimoDiaMes; // Indica se recorrencia é ultimo dia do mes
    private Integer mesDoAno; // Armazena o número do mês
    private PosicaoMes posicaoMes; // Define enum de qual semana dentro do mes (PRIMEIRA, ULTIMA, etc)
    private DayOfWeek diaSemanaMes; // Define qual dia da semana será utilizado junto com posicaoMes

    // Construtor
    public Recorrencia() {

        this.tipoRecorrencia = TipoRecorrencia.NUNCA;
        this.intervalo = 1;
        this.dataInicio = LocalDate.now();
        this.diasSemana = new HashSet<>();
        this.regraCalendario = RegraCalendario.DIA_DO_MES;
        this.ultimoDiaMes = false;
    }

    // GETs e SETs
    public TipoRecorrencia getTipoRecorrencia() {
        return tipoRecorrencia;
    }

    public void setTipoRecorrencia(TipoRecorrencia tipoRecorrencia) {
        this.tipoRecorrencia = tipoRecorrencia;
    }

    public int getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Set<DayOfWeek> getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(Set<DayOfWeek> diasSemana) {
        this.diasSemana = diasSemana;
    }

    public RegraCalendario getRegraCalendario() {
        return regraCalendario;
    }

    public void setRegraCalendario(RegraCalendario regraCalendario) {
        this.regraCalendario = regraCalendario;
    }

    public Integer getDiaDoMes() {
        return diaDoMes;
    }

    public void setDiaDoMes(Integer diaDoMes) {
        this.diaDoMes = diaDoMes;
    }

    public boolean isUltimoDiaMes() {
        return ultimoDiaMes;
    }

    public void setUltimoDiaMes(boolean ultimoDiaMes) {
        this.ultimoDiaMes = ultimoDiaMes;
    }

    public Integer getMesDoAno() {
        return mesDoAno;
    }

    public void setMesDoAno(Integer mesDoAno) {
        this.mesDoAno = mesDoAno;
    }

    public PosicaoMes getPosicaoMes() {
        return posicaoMes;
    }

    public void setPosicaoMes(PosicaoMes posicaoMes) {
        this.posicaoMes = posicaoMes;
    }

    public DayOfWeek getDiaSemanaMes() {
        return diaSemanaMes;
    }

    public void setDiaSemanaMes(DayOfWeek diaSemanaMes) {
        this.diaSemanaMes = diaSemanaMes;
    }


}
