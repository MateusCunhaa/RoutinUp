
class Recorrencia {

  // Atributos

  // Frequência da recorrência
  final String tipoRecorrencia;

  // Intervalo entre as ocorrências
  final int intervalo;

  // Data de início
  final DateTime dataInicio;

  // Dias da semana selecionados
  final List<String> diasSemana;

  // Regra utilizada no calendário
  final String regraCalendario;

  // Dia específico do mês
  final int? diaDoMes;

  // Indica se ocorre no último dia do mês
  final bool ultimoDiaMes;

  // Mês específico do ano
  final int? mesDoAno;

  // Posição da semana dentro do mês
  final String? posicaoMes;

  // Dia da semana dentro do mês
  final String? diaSemanaMes;


  // Construtor
  Recorrencia({

    required this.tipoRecorrencia,
    required this.intervalo,
    required this.dataInicio,
    this.diasSemana = const [],
    this.regraCalendario = 'DIA_DO_MES',
    this.diaDoMes,
    this.ultimoDiaMes = false,
    this.mesDoAno,
    this.posicaoMes,
    this.diaSemanaMes,

  });


  // Converte objeto Recorrencia em JSON
  Map<String, dynamic> toJson() {

    return {

      'tipoRecorrencia': tipoRecorrencia,
      'intervalo': intervalo,
      'dataInicio': dataInicio.toIso8601String().split('T').first,
      'diasSemana': diasSemana,
      'regraCalendario': regraCalendario,
      'diaDoMes': diaDoMes,
      'ultimoDiaMes': ultimoDiaMes,
      'mesDoAno': mesDoAno,
      'posicaoMes': posicaoMes,
      'diaSemanaMes': diaSemanaMes,

    };
  }

  // Metodo

  // Converte JSON recebido do Java em objeto Recorrencia
  factory Recorrencia.fromJson(Map<String, dynamic> json) {

    return Recorrencia(

      tipoRecorrencia: json['tipoRecorrencia'] ?? 'NUNCA',

      intervalo: (json['intervalo'] as num?)?.toInt() ?? 1,

      dataInicio: DateTime.parse(
        json['dataInicio'],
      ),

      diasSemana: (json['diasSemana'] as List<dynamic>?)
          ?.map((dia) => dia.toString())
          .toList() ?? [],

      regraCalendario:
      json['regraCalendario'] ?? 'DIA_DO_MES',

      diaDoMes: (json['diaDoMes'] as num?)?.toInt(),

      ultimoDiaMes: json['ultimoDiaMes'] == true,

      mesDoAno: (json['mesDoAno'] as num?)?.toInt(),

      posicaoMes: json['posicaoMes'],

      diaSemanaMes: json['diaSemanaMes'],

    );
  }
}
