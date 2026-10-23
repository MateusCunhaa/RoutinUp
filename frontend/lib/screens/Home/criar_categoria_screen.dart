
import 'package:flutter/material.dart';
import 'package:routinup_app/services/api_service.dart';

class CriarCategoriaScreen extends StatefulWidget {
  final int usuarioId;

  const CriarCategoriaScreen({
    super.key,
    required this.usuarioId,
  });

  @override
  State<CriarCategoriaScreen> createState() =>
      _CriarCategoriaScreenState();
}

class _CriarCategoriaScreenState
    extends State<CriarCategoriaScreen> {

  final TextEditingController _nomeController =
  TextEditingController();

  bool _salvando = false;

  String _corSelecionada = '#ECAA00';
  String _iconeSelecionado = 'category';


  static const List<Map<String, dynamic>> _cores = [
    {'hex': '#ECAA00', 'cor': Color(0xFFECAA00)}, // Amarelo
    {'hex': '#4A90E2', 'cor': Color(0xFF4A90E2)}, // Azul
    {'hex': '#9B59B6', 'cor': Color(0xFF9B59B6)}, // Roxo
    {'hex': '#2ECC71', 'cor': Color(0xFF2ECC71)}, // Verde
    {'hex': '#E74C3C', 'cor': Color(0xFFE74C3C)}, // Vermelho
    {'hex': '#1ABC9C', 'cor': Color(0xFF1ABC9C)}, // Turquesa
    {'hex': '#FF7043', 'cor': Color(0xFFFF7043)}, // Coral
    {'hex': '#78909C', 'cor': Color(0xFF78909C)}, // Cinza azulado
    {'hex': '#EC407A', 'cor': Color(0xFFEC407A)}, // Rosa
    {'hex': '#5C6BC0', 'cor': Color(0xFF5C6BC0)}, // Índigo
    {'hex': '#26C6DA', 'cor': Color(0xFF26C6DA)}, // Ciano
    {'hex': '#9CCC65', 'cor': Color(0xFF9CCC65)}, // Verde claro
    {'hex': '#8D6E63', 'cor': Color(0xFF8D6E63)}, // Marrom
    {'hex': '#AB47BC', 'cor': Color(0xFFAB47BC)}, // Violeta
    {'hex': '#FFA726', 'cor': Color(0xFFFFA726)}, // Laranja
    {'hex': '#42A5F5', 'cor': Color(0xFF42A5F5)}, // Azul claro
    {'hex': '#66BB6A', 'cor': Color(0xFF66BB6A)}, // Verde médio
    {'hex': '#EF5350', 'cor': Color(0xFFEF5350)}, // Vermelho claro
    {'hex': '#D4E157', 'cor': Color(0xFFD4E157)}, // Lima
    {'hex': '#B0BEC5', 'cor': Color(0xFFB0BEC5)}, // Cinza claro
  ];



  static const List<Map<String, dynamic>> _icones = [
    {'nome': 'category', 'icone': Icons.category_outlined},
    {'nome': 'school', 'icone': Icons.school_outlined},
    {'nome': 'work', 'icone': Icons.work_outline},
    {'nome': 'home', 'icone': Icons.home_outlined},
    {'nome': 'favorite', 'icone': Icons.favorite_border},
    {'nome': 'fitness_center', 'icone': Icons.fitness_center},
    {'nome': 'restaurant', 'icone': Icons.restaurant_outlined},
    {'nome': 'sports_esports', 'icone': Icons.sports_esports},
    {'nome': 'payments', 'icone': Icons.payments_outlined},
    {'nome': 'palette', 'icone': Icons.palette_outlined},
    {'nome': 'book', 'icone': Icons.menu_book_outlined},
    {'nome': 'checklist', 'icone': Icons.checklist},
    {'nome': 'shopping', 'icone': Icons.shopping_bag_outlined},
    {'nome': 'directions_run', 'icone': Icons.directions_run},
    {'nome': 'music', 'icone': Icons.music_note},
    {'nome': 'flight', 'icone': Icons.flight},
    {'nome': 'pets', 'icone': Icons.pets},
    {'nome': 'coffee', 'icone': Icons.local_cafe_outlined},
    {'nome': 'cleaning', 'icone': Icons.cleaning_services_outlined},
    {'nome': 'build', 'icone': Icons.build_outlined},
  ];


  Color get _corAtual {
    final cor = _cores.firstWhere(
          (item) => item['hex'] == _corSelecionada,
    );

    return cor['cor'] as Color;
  }

  IconData get _iconeAtual {
    final item = _icones.firstWhere(
          (item) => item['nome'] == _iconeSelecionado,
    );

    return item['icone'] as IconData;
  }

  @override
  void dispose() {
    _nomeController.dispose();
    super.dispose();
  }

  Future<void> _salvarCategoria() async {
    final nome = _nomeController.text.trim();

    if (nome.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Informe o nome da categoria.'),
        ),
      );
      return;
    }

    if (nome.length > 100) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text(
            'O nome deve ter no máximo 100 caracteres.',
          ),
        ),
      );
      return;
    }

    setState(() {
      _salvando = true;
    });

    try {
      final categoriaCriada =
      await ApiService.criarCategoria(
        usuarioId: widget.usuarioId,
        nome: nome,
        cor: _corSelecionada,
        icone: _iconeSelecionado,
      );

      if (!mounted) return;

      // Devolve a categoria criada para a tela anterior.
      Navigator.pop(context, categoriaCriada);

    } catch (e) {
      if (!mounted) return;

      final mensagem = e
          .toString()
          .replaceFirst('Exception: ', '');

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(mensagem),
          backgroundColor: Colors.red.shade800,
        ),
      );

    } finally {
      if (mounted) {
        setState(() {
          _salvando = false;
        });
      }
    }
  }


  Future<void> _selecionarIcone() async {
    await showModalBottomSheet<void>(
      context: context,
      backgroundColor: const Color(0xFF1C1C1C),
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(
          top: Radius.circular(24),
        ),
      ),
      builder: (sheetContext) {
        return SafeArea(
          child: Padding(
            padding: const EdgeInsets.fromLTRB(20, 12, 20, 20),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Container(
                  width: 40,
                  height: 4,
                  decoration: BoxDecoration(
                    color: Colors.white38,
                    borderRadius: BorderRadius.circular(10),
                  ),
                ),
                const SizedBox(height: 20),
                Row(
                  children: [
                    const Expanded(
                      child: Text(
                        'Escolha um ícone',
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 19,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    IconButton(
                      onPressed: () => Navigator.pop(sheetContext),
                      icon: const Icon(
                        Icons.close,
                        color: Colors.white70,
                      ),
                    ),
                  ],
                ),
                SizedBox(
                  height: MediaQuery.of(sheetContext).size.height * 0.45,
                  child: GridView.builder(
                    itemCount: _icones.length,
                    gridDelegate:
                    const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 4,
                      mainAxisSpacing: 12,
                      crossAxisSpacing: 12,
                    ),
                    itemBuilder: (context, index) {
                      final item = _icones[index];
                      final nome = item['nome'] as String;
                      final icone = item['icone'] as IconData;
                      final selecionado =
                          nome == _iconeSelecionado;

                      return InkWell(
                        borderRadius: BorderRadius.circular(14),
                        onTap: () {
                          setState(() {
                            _iconeSelecionado = nome;
                          });
                          Navigator.pop(sheetContext);
                        },
                        child: Container(
                          decoration: BoxDecoration(
                            color: selecionado
                                ? _corAtual.withValues(alpha: 0.16)
                                : const Color(0xFF111111),
                            borderRadius: BorderRadius.circular(14),
                            border: Border.all(
                              color: selecionado
                                  ? _corAtual
                                  : const Color(0xFF333333),
                              width: selecionado ? 1.5 : 1,
                            ),
                          ),
                          child: Icon(
                            icone,
                            color: selecionado
                                ? _corAtual
                                : Colors.white70,
                            size: 28,
                          ),
                        ),
                      );
                    },
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }



  Future<void> _selecionarCor() async {
    await showModalBottomSheet<void>(
      context: context,
      backgroundColor: const Color(0xFF1C1C1C),
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(
          top: Radius.circular(24),
        ),
      ),
      builder: (sheetContext) {
        return SafeArea(
          child: Padding(
            padding: const EdgeInsets.fromLTRB(20, 12, 20, 24),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Container(
                  width: 40,
                  height: 4,
                  decoration: BoxDecoration(
                    color: Colors.white38,
                    borderRadius: BorderRadius.circular(10),
                  ),
                ),
                const SizedBox(height: 20),
                Row(
                  children: [
                    const Expanded(
                      child: Text(
                        'Escolha uma cor',
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 19,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    IconButton(
                      onPressed: () => Navigator.pop(sheetContext),
                      icon: const Icon(
                        Icons.close,
                        color: Colors.white70,
                      ),
                    ),
                  ],
                ),
                SizedBox(
                  height: MediaQuery.of(sheetContext).size.height * 0.35,
                  child: GridView.builder(
                    itemCount: _cores.length,
                    gridDelegate:
                    const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 5,
                      mainAxisSpacing: 14,
                      crossAxisSpacing: 14,
                    ),
                    itemBuilder: (context, index) {
                      final item = _cores[index];
                      final hex = item['hex'] as String;
                      final cor = item['cor'] as Color;
                      final selecionada =
                          hex == _corSelecionada;

                      return InkWell(
                        borderRadius: BorderRadius.circular(50),
                        onTap: () {
                          setState(() {
                            _corSelecionada = hex;
                          });
                          Navigator.pop(sheetContext);
                        },
                        child: Center(
                          child: Container(
                            width: 42,
                            height: 42,
                            decoration: BoxDecoration(
                              color: cor,
                              shape: BoxShape.circle,
                              border: Border.all(
                                color: selecionada
                                    ? Colors.white
                                    : Colors.transparent,
                                width: 3,
                              ),
                            ),
                            child: selecionada
                                ? const Icon(
                              Icons.check,
                              color: Colors.black,
                              size: 23,
                            )
                                : null,
                          ),
                        ),
                      );
                    },
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }


  @override
  Widget build(BuildContext context) {
    const amarelo = Color(0xFFFFB900);

    return Scaffold(
      backgroundColor: Colors.black,

      appBar: AppBar(
        backgroundColor: Colors.black,
        foregroundColor: Colors.white,
        centerTitle: true,
        title: const Text('Nova Categoria'),
      ),

      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),

        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [

            const Text(
              'Criar categoria',
              style: TextStyle(
                color: Colors.white,
                fontSize: 28,
                fontWeight: FontWeight.bold,
              ),
            ),

            const SizedBox(height: 8),

            const Text(
              'Personalize uma categoria para sua rotina.',
              style: TextStyle(
                color: Colors.white60,
                fontSize: 14,
              ),
            ),

            const SizedBox(height: 32),

            // NOME
            TextField(
              controller: _nomeController,
              onChanged: (_) => setState(() {}),
              maxLength: 100,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 16,
              ),

              decoration: InputDecoration(
                labelText: 'Nome da categoria',
                labelStyle: const TextStyle(
                  color: Colors.white60,
                ),
                prefixIcon: const Icon(
                  Icons.category_outlined,
                  color: Colors.white,
                ),
                counterStyle: const TextStyle(
                  color: Colors.white54,
                ),
                enabledBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(14),
                  borderSide: const BorderSide(
                    color: amarelo,
                  ),
                ),
                focusedBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(14),
                  borderSide: const BorderSide(
                    color: amarelo,
                    width: 1.5,
                  ),
                ),
              ),
            ),

            const SizedBox(height: 28),


            // SELETOR DE ÍCONE
            InkWell(
              onTap: _selecionarIcone,
              borderRadius: BorderRadius.circular(14),
              child: Container(
                height: 58,
                padding: const EdgeInsets.symmetric(
                  horizontal: 14,
                ),
                decoration: BoxDecoration(
                  color: Colors.black,
                  borderRadius: BorderRadius.circular(14),
                  border: Border.all(
                    color: const Color(0xFFFFB900),
                  ),
                ),
                child: Row(
                  children: [
                    const Icon(
                      Icons.grid_view_outlined,
                      color: Colors.white,
                      size: 22,
                    ),
                    const SizedBox(width: 12),
                    const Text(
                      'Ícone',
                      style: TextStyle(
                        color: Colors.white60,
                        fontSize: 16,
                      ),
                    ),
                    const Spacer(),
                    Container(
                      width: 36,
                      height: 36,
                      decoration: BoxDecoration(
                        color: _corAtual.withValues(alpha: 0.18),
                        borderRadius: BorderRadius.circular(9),
                      ),
                      child: Icon(
                        _iconeAtual,
                        color: _corAtual,
                        size: 22,
                      ),
                    ),
                    const SizedBox(width: 10),
                    const Icon(
                      Icons.keyboard_arrow_down_rounded,
                      color: Colors.white70,
                    ),
                  ],
                ),
              ),
            ),

            const SizedBox(height: 14),

            // SELETOR DE COR
            InkWell(
              onTap: _selecionarCor,
              borderRadius: BorderRadius.circular(14),
              child: Container(
                height: 58,
                padding: const EdgeInsets.symmetric(
                  horizontal: 14,
                ),
                decoration: BoxDecoration(
                  color: Colors.black,
                  borderRadius: BorderRadius.circular(14),
                  border: Border.all(
                    color: const Color(0xFFFFB900),
                  ),
                ),
                child: Row(
                  children: [
                    const Icon(
                      Icons.palette_outlined,
                      color: Colors.white,
                      size: 22,
                    ),
                    const SizedBox(width: 12),
                    const Text(
                      'Cor',
                      style: TextStyle(
                        color: Colors.white60,
                        fontSize: 16,
                      ),
                    ),
                    const Spacer(),
                    Container(
                      width: 25,
                      height: 25,
                      decoration: BoxDecoration(
                        color: _corAtual,
                        shape: BoxShape.circle,
                        border: Border.all(
                          color: Colors.white54,
                          width: 1,
                        ),
                      ),
                    ),
                    const SizedBox(width: 10),
                    const Icon(
                      Icons.keyboard_arrow_down_rounded,
                      color: Colors.white70,
                    ),
                  ],
                ),
              ),
            ),

            const SizedBox(height: 32),


            // PRÉVIA
            const Text(
              'Prévia',
              style: TextStyle(
                color: Colors.white,
                fontSize: 17,
                fontWeight: FontWeight.bold,
              ),
            ),

            const SizedBox(height: 14),

            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(16),

              decoration: BoxDecoration(
                color: const Color(0xFF111111),
                borderRadius: BorderRadius.circular(14),
                border: Border.all(
                  color: const Color(0xFF333333),
                ),
              ),

              child: Row(
                children: [
                  Container(
                    width: 44,
                    height: 44,

                    decoration: BoxDecoration(
                      color: _corAtual.withValues(
                        alpha: 0.18,
                      ),
                      borderRadius: BorderRadius.circular(12),
                    ),

                    child: Icon(
                      _iconeAtual,
                      color: _corAtual,
                      size: 24,
                    ),
                  ),

                  const SizedBox(width: 14),

                  Expanded(
                    child: Text(
                      _nomeController.text.trim().isEmpty
                          ? 'Nome da categoria'
                          : _nomeController.text.trim(),

                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 16,
                      ),

                      overflow: TextOverflow.ellipsis,
                    ),
                  ),
                ],
              ),
            ),

            const SizedBox(height: 36),

            // SALVAR
            SizedBox(
              width: double.infinity,
              height: 54,

              child: ElevatedButton(
                onPressed:
                _salvando ? null : _salvarCategoria,

                style: ElevatedButton.styleFrom(
                  backgroundColor: amarelo,
                  foregroundColor: Colors.black,
                  disabledBackgroundColor:
                  amarelo.withValues(alpha: 0.5),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(14),
                  ),
                ),

                child: _salvando
                    ? const SizedBox(
                  width: 22,
                  height: 22,
                  child: CircularProgressIndicator(
                    strokeWidth: 2,
                    color: Colors.black,
                  ),
                )
                    : const Text(
                  'SALVAR CATEGORIA',
                  style: TextStyle(
                    fontSize: 15,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),

            const SizedBox(height: 16),
          ],
        ),
      ),
    );
  }
}
