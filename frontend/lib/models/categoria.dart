
class Categoria {
  final int id;
  final String nome;
  final String cor;
  final String icone;
  final int usuarioId;

  Categoria({
    required this.id,
    required this.nome,
    required this.cor,
    required this.icone,
    required this.usuarioId,
  });

  factory Categoria.fromJson(Map<String, dynamic> json) {
    return Categoria(
      id: (json['id'] as num?)?.toInt() ?? 0,
      nome: json['nome']?.toString() ?? '',
      cor: json['cor']?.toString() ?? '#737B87',
      icone: json['icone']?.toString() ?? 'category',
      usuarioId: (json['usuarioId'] as num?)?.toInt() ?? 0,
    );
  }
}
