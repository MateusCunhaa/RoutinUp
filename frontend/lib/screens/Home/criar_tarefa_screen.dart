import 'package:flutter/material.dart';
import 'package:routinup_app/models/categoria.dart';
import 'package:routinup_app/screens/Home/criar_categoria_screen.dart';
import 'package:routinup_app/services/api_service.dart';

class CriarTarefaScreen extends StatefulWidget {

  final int usuarioId;

  const CriarTarefaScreen({
    super.key,
    required this.usuarioId,
  });

  @override
  State<CriarTarefaScreen> createState() =>
      _CriarTarefaScreenState();
}

class _CriarTarefaScreenState
    extends State<CriarTarefaScreen> {

  final TextEditingController nomeController =
  TextEditingController();

  final TextEditingController descricaoController =
  TextEditingController();

  final TextEditingController horarioController =
  TextEditingController();

  final TextEditingController duracaoController =
  TextEditingController();

  int prioridade = 1;

  int? duracaoMinutos;

  List<Categoria> categorias = [];
  Categoria? categoriaSelecionada;

  bool carregandoCategorias = true;
  String? erroCategorias;

  // Metodos
  @override
  void initState() {
    super.initState();
    carregarCategorias();
  }

  Future<void> carregarCategorias() async {
    setState(() {
      carregandoCategorias = true;
      erroCategorias = null;
    });

    try {
      final resultado =
      await ApiService.listarCategorias(widget.usuarioId);

      if (!mounted) return;

      setState(() {
        categorias = resultado;
        categoriaSelecionada =
        resultado.isNotEmpty ? resultado.first : null;
        carregandoCategorias = false;
      });
    } catch (e) {
      debugPrint('Erro ao carregar categorias: $e');

      if (!mounted) return;

      setState(() {
        erroCategorias = 'Não foi possível carregar as categorias.';
        carregandoCategorias = false;
      });
    }
  }

  Future<void> selecionarHorario() async {
    final TimeOfDay? horarioEscolhido =
    await showDialog<TimeOfDay>(
      context: context,
      builder: (context) => SeletorHorarioDialog(
        initialTime: TimeOfDay.now(),
        titulo: "Escolha o horário da tarefa",
      ),
    );

    if (horarioEscolhido == null || !mounted) {
      return;
    }

    setState(() {
      final hora = horarioEscolhido.hour.toString().padLeft(2, '0');
      final minuto = horarioEscolhido.minute.toString().padLeft(2, '0');

      horarioController.text = "$hora:$minuto";
    });
  }

  Future<void> selecionarDuracao() async {
    final TimeOfDay inicial = duracaoMinutos == null
        ? const TimeOfDay(hour: 1, minute: 0)
        : TimeOfDay(
      hour: duracaoMinutos! ~/ 60,
      minute: duracaoMinutos! % 60,
    );

    final TimeOfDay? escolhida = await showDialog<TimeOfDay>(
      context: context,
      builder: (context) => SeletorHorarioDialog(
        initialTime: inicial,
        titulo: "Escolha a duração",
      ),
    );

    if (escolhida == null || !mounted) {
      return;
    }

    final total = escolhida.hour * 60 + escolhida.minute;
    final horas = total ~/ 60;
    final minutos = total % 60;

    setState(() {
      duracaoMinutos = total;

      if (horas > 0 && minutos > 0) {
        duracaoController.text = "${horas}h ${minutos}min";
      } else if (horas > 0) {
        duracaoController.text = "${horas}h";
      } else {
        duracaoController.text = "$minutos min";
      }
    });
  }

  Color corDaCategoria(String valor) {
    try {
      String hex = valor.replaceAll('#', '');

      if (hex.length == 6) {
        hex = 'FF$hex';
      }

      return Color(int.parse(hex, radix: 16));
    } catch (_) {
      return const Color(0xFF737B87);
    }
  }


  IconData iconeDaCategoria(String? nome) {
    const icones = <String, IconData>{
      'category': Icons.category_outlined,
      'school': Icons.school_outlined,
      'work': Icons.work_outline,
      'home': Icons.home_outlined,
      'favorite': Icons.favorite_border,
      'fitness_center': Icons.fitness_center,
      'restaurant': Icons.restaurant_outlined,
      'sports_esports': Icons.sports_esports,
      'payments': Icons.payments_outlined,
      'palette': Icons.palette_outlined,
      'book': Icons.menu_book_outlined,
      'checklist': Icons.checklist,
      'shopping': Icons.shopping_bag_outlined,
      'directions_run': Icons.directions_run,
      'music': Icons.music_note,
      'flight': Icons.flight,
      'pets': Icons.pets,
      'coffee': Icons.local_cafe_outlined,
      'cleaning': Icons.cleaning_services_outlined,
      'build': Icons.build_outlined,
    };

    return icones[nome] ?? Icons.category_outlined;
  }




  Future<void> abrirSeletorCategoria() async {
    final resultado = await showModalBottomSheet<dynamic>(
      context: context,
      isScrollControlled: true,
      backgroundColor: const Color(0xFF1C1C1C),
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(
          top: Radius.circular(24),
        ),
      ),
      builder: (context) {
        return SafeArea(
          child: SizedBox(
            height: MediaQuery.of(context).size.height * 0.65,
            child: Column(
              children: [
                const SizedBox(height: 12),

                Container(
                  width: 40,
                  height: 4,
                  decoration: BoxDecoration(
                    color: Colors.white38,
                    borderRadius: BorderRadius.circular(10),
                  ),
                ),

                Padding(
                  padding: const EdgeInsets.all(20),
                  child: Row(
                    children: [
                      const Expanded(
                        child: Text(
                          'Escolha uma categoria',
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                      IconButton(
                        onPressed: () =>
                            Navigator.pop(context),
                        icon: const Icon(
                          Icons.close,
                          color: Colors.white70,
                        ),
                      ),
                    ],
                  ),
                ),

                Expanded(
                  child: ListView.builder(
                    itemCount: categorias.length + 1,
                    itemBuilder: (context, index) {

                      // OPÇÃO PARA CRIAR UMA NOVA CATEGORIA
                      if (index == categorias.length) {
                        return Column(
                          children: [
                            const Divider(
                              color: Color(0xFF404040),
                              height: 1,
                            ),

                            ListTile(
                              leading: const CircleAvatar(
                                backgroundColor: Color(0xFF332A0D),
                                child: Icon(
                                  Icons.add,
                                  color: Color(0xFFFFB900),
                                ),
                              ),
                              title: const Text(
                                'Criar nova categoria',
                                style: TextStyle(
                                  color: Color(0xFFFFB900),
                                  fontWeight: FontWeight.w600,
                                ),
                              ),
                              subtitle: const Text(
                                'Personalize nome, cor e ícone',
                                style: TextStyle(
                                  color: Colors.white54,
                                ),
                              ),
                              trailing: const Icon(
                                Icons.chevron_right,
                                color: Color(0xFFFFB900),
                              ),
                              onTap: () {
                                Navigator.pop(context, 'criar');
                              },
                            ),
                          ],
                        );
                      }

                      // CATEGORIAS EXISTENTES
                      final categoria = categorias[index];
                      final cor = corDaCategoria(categoria.cor);

                      final selecionada =
                          categoriaSelecionada?.id == categoria.id;

                      return ListTile(
                        leading: CircleAvatar(
                          backgroundColor:
                          cor.withValues(alpha: 0.20),
                          child: Icon(
                            iconeDaCategoria(categoria.icone),
                            color: cor,
                          ),
                        ),
                        title: Text(
                          categoria.nome,
                          style: const TextStyle(
                            color: Colors.white,
                          ),
                        ),
                        trailing: Icon(
                          selecionada
                              ? Icons.check_circle
                              : Icons.radio_button_unchecked,
                          color: selecionada
                              ? const Color(0xFFFFB900)
                              : Colors.white38,
                        ),
                        onTap: () {
                          Navigator.pop(context, categoria);
                        },
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

    if (!mounted || resultado == null) {
      return;
    }

    // USUÁRIO ESCOLHEU CRIAR UMA CATEGORIA
    if (resultado == 'criar') {
      final Map<String, dynamic>? dados =
      await Navigator.push<Map<String, dynamic>>(
        context,
        MaterialPageRoute(
          builder: (_) => CriarCategoriaScreen(
            usuarioId: widget.usuarioId,
          ),
        ),
      );

      if (dados == null || !mounted) {
        return;
      }

      final novaCategoria = Categoria.fromJson(dados);

      setState(() {
        final indice = categorias.indexWhere(
              (categoria) => categoria.id == novaCategoria.id,
        );

        if (indice == -1) {
          categorias.add(novaCategoria);
        } else {
          categorias[indice] = novaCategoria;
        }

        categoriaSelecionada = novaCategoria;
      });

      return;
    }

    // USUÁRIO ESCOLHEU UMA CATEGORIA EXISTENTE
    if (resultado is Categoria) {
      setState(() {
        categoriaSelecionada = resultado;
      });
    }
  }


  @override
  Widget build(BuildContext context) {

    return Scaffold(

      appBar: AppBar(
        title: const Text("Nova Tarefa"),
        centerTitle: true,
      ),


      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),

        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [

            const Text(
              "Nova Tarefa",
              style: TextStyle(
                fontSize: 28,
                fontWeight: FontWeight.bold,
              ),
            ),

            const SizedBox(height: 24),

            // NOME
            TextField(
              controller: nomeController,
              decoration: const InputDecoration(
                labelText: "Nome da tarefa",
                prefixIcon: Icon(Icons.task_alt),
                border: OutlineInputBorder(),
              ),
            ),

            const SizedBox(height: 16),

            // DESCRIÇÃO
            TextField(
              controller: descricaoController,
              maxLines: 3,
              decoration: const InputDecoration(
                labelText: "Descrição",
                prefixIcon: Icon(Icons.description),
                border: OutlineInputBorder(),
              ),
            ),

            const SizedBox(height: 16),

            // HORÁRIO

            TextField(
              controller: horarioController,
              readOnly: true,
              onTap: selecionarHorario,
              decoration: const InputDecoration(
                labelText: "Horário",
                hintText: "Escolher horário",
                prefixIcon: Icon(Icons.access_time),
                suffixIcon: Icon(Icons.arrow_drop_down),
                border: OutlineInputBorder(),
              ),
            ),


            const SizedBox(height: 16),

            // DURAÇÃO

            TextField(
              controller: duracaoController,
              readOnly: true,
              onTap: selecionarDuracao,
              decoration: const InputDecoration(
                labelText: "Duração",
                hintText: "Escolher duração",
                prefixIcon: Icon(Icons.timer),
                suffixIcon: Icon(Icons.arrow_drop_down),
                border: OutlineInputBorder(),
              ),
            ),


            const SizedBox(height: 24),


            // CATEGORIA
            if (carregandoCategorias)
              const Center(
                child: CircularProgressIndicator(),
              )
            else if (erroCategorias != null)
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(erroCategorias!),
                  TextButton(
                    onPressed: carregarCategorias,
                    child: const Text("Tentar novamente"),
                  ),
                ],
              )
            else
              InkWell(
                onTap: categorias.isEmpty
                    ? null
                    : abrirSeletorCategoria,
                borderRadius: BorderRadius.circular(14),
                child: Container(
                  width: double.infinity,
                  height: 58,
                  padding: const EdgeInsets.symmetric(
                    horizontal: 14,
                    vertical: 7,
                  ),
                  decoration: BoxDecoration(
                    color: Colors.black,
                    borderRadius: BorderRadius.circular(14),
                    border: Border.all(
                      color: const Color(0xFFECAA00),
                      width: 1,
                    ),
                  ),
                  child: Row(
                    children: [
                      // LADO ESQUERDO: RÓTULO
                      SizedBox(
                        width: 120,
                        child: Row(
                          children: const [
                            Icon(
                              Icons.category_outlined,
                              color: Colors.white,
                              size: 22,
                            ),
                            SizedBox(width: 12),
                            Text(
                              "Categoria",
                              style: TextStyle(
                                color: Colors.white60,
                                fontSize: 16,
                              ),
                            ),
                          ],
                        ),
                      ),

                      // DIVISÓRIA
                      Container(
                        width: 1,
                        height: 26,
                        color: Colors.white24,
                      ),

                      const SizedBox(width: 12),

                      // LADO DIREITO: CATEGORIA SELECIONADA
                      Expanded(
                        child: Container(
                          height: double.infinity,
                          padding: const EdgeInsets.symmetric(
                            horizontal: 10,
                          ),
                          decoration: BoxDecoration(
                            color: categoriaSelecionada == null
                                ? Colors.transparent
                                : corDaCategoria(
                              categoriaSelecionada!.cor,
                            ).withValues(alpha: 0.18),
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: categoriaSelecionada == null
                              ? const Align(
                            alignment: Alignment.centerLeft,
                            child: Text(
                              "Selecionar",
                              style: TextStyle(
                                color: Colors.white54,
                                fontSize: 14,
                              ),
                            ),
                          )
                              : Row(
                            children: [
                              Icon(
                                iconeDaCategoria(
                                  categoriaSelecionada!.icone,
                                ),
                                color: corDaCategoria(
                                  categoriaSelecionada!.cor,
                                ),
                                size: 20,
                              ),

                              const SizedBox(width: 9),

                              Expanded(
                                child: Text(
                                  categoriaSelecionada!.nome,
                                  overflow: TextOverflow.ellipsis,
                                  maxLines: 1,
                                  style: const TextStyle(
                                    color: Colors.white,
                                    fontSize: 14,
                                    fontWeight: FontWeight.w600,
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),

                      const SizedBox(width: 8),

                      // SETA DO SELETOR
                      const Icon(
                        Icons.keyboard_arrow_down,
                        color: Colors.white70,
                        size: 23,
                      ),
                    ],
                  ),
                ),
              ),


            const SizedBox(height: 24),


            // PRIORIDADE
            const Text(
              "Prioridade",
              style: TextStyle(
                fontSize: 17,
                fontWeight: FontWeight.bold,
              ),
            ),


            SliderTheme(
              data: SliderTheme.of(context).copyWith(
                trackHeight: 6,
                activeTrackColor: const Color(0xFFFFB300),
                inactiveTrackColor: const Color(0xFF424242),
                thumbColor: const Color(0xFFFFB300),
                overlayColor: const Color(0x29FFB300),
              ),
              child: Slider(
                value: prioridade.toDouble(),
                min: 1,
                max: 5,
                divisions: 4,
                onChanged: (valor) {
                  setState(() {
                    prioridade = valor.toInt();
                  });
                },
              ),
            ),


            Center(
              child: Text(
                "$prioridade de 5",
                style: const TextStyle(
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),

            const SizedBox(height: 24),

            // BOTÃO CRIAR
            SizedBox(
              width: double.infinity,
              height: 52,

              child: ElevatedButton(
                onPressed: () {
                  debugPrint("Nome: ${nomeController.text}");
                  debugPrint("Descrição: ${descricaoController.text}");
                  debugPrint("Horário: ${horarioController.text}");
                  debugPrint("Duração: $duracaoMinutos");
                  debugPrint("Prioridade: $prioridade");
                },

                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFFFFB300),
                  foregroundColor: Colors.black,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(14),
                  ),
                ),

                child: const Text(
                  "CRIAR TAREFA",
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: 16,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),


    );
  }
}


class SeletorHorarioDialog extends StatefulWidget {
  final TimeOfDay initialTime;
  final String titulo;

  const SeletorHorarioDialog({
    super.key,
    required this.initialTime,
    required this.titulo,
  });

  @override
  State<SeletorHorarioDialog> createState() =>
      _SeletorHorarioDialogState();
}

class _SeletorHorarioDialogState
    extends State<SeletorHorarioDialog> {

  late int hora;
  late int minuto;

  late FixedExtentScrollController horasController;
  late FixedExtentScrollController minutosController;

  @override
  void initState() {
    super.initState();

    hora = widget.initialTime.hour;
    minuto = widget.initialTime.minute;

    horasController =
        FixedExtentScrollController(initialItem: hora);

    minutosController =
        FixedExtentScrollController(initialItem: minuto);
  }

  @override
  void dispose() {
    horasController.dispose();
    minutosController.dispose();
    super.dispose();
  }

  Widget construirRoda({
    required String titulo,
    required int quantidade,
    required int selecionado,
    required FixedExtentScrollController controller,
    required ValueChanged<int> aoSelecionar,
  }) {
    return Expanded(
      child: Column(
        children: [
          Text(
            titulo,
            style: const TextStyle(
              color: Colors.white70,
              fontSize: 14,
            ),
          ),

          const SizedBox(height: 8),

          Expanded(
            child: Stack(
              alignment: Alignment.center,
              children: [
                IgnorePointer(
                  child: Container(
                    height: 44,
                    decoration: BoxDecoration(
                      color: const Color(0x22FFB300),
                      borderRadius: BorderRadius.circular(10),
                      border: Border.symmetric(
                        horizontal: BorderSide(
                          color: const Color(0xFFFFB300)
                              .withValues(alpha: 0.6),
                        ),
                      ),
                    ),
                  ),
                ),

                ListWheelScrollView(
                  controller: controller,
                  itemExtent: 44,
                  physics: const FixedExtentScrollPhysics(),
                  diameterRatio: 1.5,
                  onSelectedItemChanged: aoSelecionar,

                  children: List.generate(
                    quantidade,
                        (index) {
                      final selecionadoAtual =
                          index == selecionado;

                      return Center(
                        child: Text(
                          index.toString().padLeft(2, '0'),
                          style: TextStyle(
                            color: selecionadoAtual
                                ? const Color(0xFFFFB300)
                                : Colors.white54,
                            fontSize: selecionadoAtual ? 26 : 20,
                            fontWeight: selecionadoAtual
                                ? FontWeight.bold
                                : FontWeight.normal,
                          ),
                        ),
                      );
                    },
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: const Color(0xFF111111),

      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(24),
        side: const BorderSide(
          color: Color(0xFF333333),
        ),
      ),

      child: Padding(
        padding: const EdgeInsets.all(20),

        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              widget.titulo,
              textAlign: TextAlign.center,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 18,
                fontWeight: FontWeight.bold,
              ),
            ),

            const SizedBox(height: 20),

            SizedBox(
              height: 220,

              child: Row(
                children: [
                  construirRoda(
                    titulo: "Horas",
                    quantidade: 24,
                    selecionado: hora,
                    controller: horasController,
                    aoSelecionar: (valor) {
                      setState(() {
                        hora = valor;
                      });
                    },
                  ),

                  const Padding(
                    padding: EdgeInsets.symmetric(horizontal: 8),
                    child: Text(
                      ":",
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 28,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),

                  construirRoda(
                    titulo: "Minutos",
                    quantidade: 60,
                    selecionado: minuto,
                    controller: minutosController,
                    aoSelecionar: (valor) {
                      setState(() {
                        minuto = valor;
                      });
                    },
                  ),
                ],
              ),
            ),

            const Divider(color: Color(0xFF333333)),

            const SizedBox(height: 8),

            Row(
              children: [
                TextButton(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  child: const Text(
                    "Cancelar",
                    style: TextStyle(
                      color: Colors.white70,
                    ),
                  ),
                ),

                const Spacer(),

                ElevatedButton(
                  onPressed: () {
                    Navigator.pop(
                      context,
                      TimeOfDay(
                        hour: hora,
                        minute: minuto,
                      ),
                    );
                  },

                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color(0xFFFFB300),
                    foregroundColor: Colors.black,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),

                  child: const Text(
                    "OK",
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
