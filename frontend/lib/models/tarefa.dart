class Tarefa {
  // Identificação
  final int id;
  final int usuarioId;

  // Dados principais
  final String nome;
  final String descricao;
  final String categoria;
  final String horario;

  // Planejamento
  final double duracao;
  final int prioridade;
  final double porcentagem;
  final bool repeticao;
  final List<String> diasSemana;

  // Conclusão e hábitos
  bool concluida;
  final int sequencia;
  final String? dataConclusao;
  final bool bonusHabitoRecebido;
  final bool xpRecebido;

  // XP estimado da tarefa
  final int xp;

  // Dados complementares da categoria
  final int categoriaId;
  final String? categoriaCor;
  final int categoriaUsuarioId;

  // Etapas recebidas do backend
  final List<Map<String, dynamic>> etapas;

  Tarefa({
    required this.nome,
    required this.descricao,
    required this.categoria,
    required this.horario,
    required this.sequencia,
    required this.xp,
    required this.prioridade,
    required this.concluida,
    this.id = 0,
    this.usuarioId = 0,
    this.duracao = 0,
    this.porcentagem = 0,
    this.repeticao = false,
    this.diasSemana = const [],
    this.dataConclusao,
    this.bonusHabitoRecebido = false,
    this.xpRecebido = false,
    this.categoriaId = 0,
    this.categoriaCor,
    this.categoriaUsuarioId = 0,
    this.etapas = const [],
  });

  // Converte o JSON recebido do Java em um objeto Tarefa
  factory Tarefa.fromJson(Map<String, dynamic> json) {
    final categoriaJson = json['categoria'];

    final String nomeCategoria =
    categoriaJson is Map
        ? categoriaJson['nome']?.toString() ?? ''
        : categoriaJson?.toString() ?? '';

    final double duracao =
        (json['duracao'] as num?)?.toDouble() ?? 0.0;

    final double prioridadeValor =
        (json['prioridade'] as num?)?.toDouble() ?? 1.0;

    // Segue a fórmula atual do SistemaXP.java
    final int xpCalculado =
        (duracao / 20).floor() +
            (prioridadeValor <= 2
                ? 1
                : prioridadeValor <= 4
                ? 2
                : 3) +
            2;

    final List<String> dias =
        (json['diasSemana'] as List<dynamic>?)
            ?.map((dia) => dia.toString())
            .toList() ??
            [];

    final List<Map<String, dynamic>> etapasRecebidas =
        (json['etapas'] as List<dynamic>?)
            ?.map(
              (etapa) =>
          Map<String, dynamic>.from(etapa as Map),
        )
            .toList() ??
            [];

    return Tarefa(
      id: (json['id'] as num?)?.toInt() ?? 0,
      usuarioId:
      (json['usuarioId'] as num?)?.toInt() ?? 0,

      nome: json['nome']?.toString() ?? '',
      descricao: json['descricao']?.toString() ?? '',
      categoria: nomeCategoria,
      horario: json['horario']?.toString() ?? '',

      duracao: duracao,
      prioridade: prioridadeValor.round(),
      porcentagem:
      (json['porcentagem'] as num?)?.toDouble() ?? 0.0,
      repeticao: json['repeticao'] == true,
      diasSemana: dias,

      concluida: json['concluida'] == true,
      sequencia:
      (json['sequencia'] as num?)?.toInt() ?? 0,
      dataConclusao: json['dataConclusao']?.toString(),
      bonusHabitoRecebido:
      json['bonusHabitoRecebido'] == true,
      xpRecebido: json['xpRecebido'] == true,

      xp: (json['xp'] as num?)?.toInt() ?? xpCalculado,

      categoriaId: categoriaJson is Map
          ? (categoriaJson['id'] as num?)?.toInt() ?? 0
          : 0,

      categoriaCor: categoriaJson is Map
          ? categoriaJson['cor']?.toString()
          : null,

      categoriaUsuarioId: categoriaJson is Map
          ? (categoriaJson['usuarioId'] as num?)?.toInt() ?? 0
          : 0,

      etapas: etapasRecebidas,
    );
  }
}