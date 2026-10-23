import 'package:flutter/material.dart';
import 'package:routinup_app/models/tarefa.dart';
import 'package:routinup_app/screens/Home/task_detail_screen.dart';
import 'package:routinup_app/services/api_service.dart';
import 'package:routinup_app/screens/Home/criar_tarefa_screen.dart';


class HomeScreen extends StatefulWidget {

  final String nomeUsuario;
  final double progresso;
  final int sequencia;
  final List<Tarefa> tarefas;
  final int xp;
  final int nivel;
  final int xpParaProximoNivel;
  final int usuarioId;

  const HomeScreen({
    super.key,
    required this.nomeUsuario,
    required this.progresso,
    required this.sequencia,
    required this.tarefas,
    required this.xp,
    required this.nivel,
    required this.xpParaProximoNivel,
    required this.usuarioId,
  });


  @override
  State<HomeScreen> createState() => _HomeScreenState();

}



class _HomeScreenState extends State<HomeScreen> {

  List<Tarefa> _tarefas = [];
  bool _carregandoTarefas = true;
  String? _erroTarefas;


  @override
  void initState() {
    super.initState();

    _tarefas = List<Tarefa>.from(widget.tarefas);

    _carregarTarefas();
  }

  Future<void> _carregarTarefas() async {
    setState(() {
      _carregandoTarefas = true;
      _erroTarefas = null;
    });

    try {
      final tarefasRecebidas =
      await ApiService.listarTarefasHoje(widget.usuarioId);

      if (!mounted) return;

      setState(() {
        _tarefas = tarefasRecebidas;
        _carregandoTarefas = false;
      });
    } catch (e) {
      debugPrint('Erro ao carregar tarefas: $e');

      if (!mounted) return;

      setState(() {
        _erroTarefas = 'Não foi possível carregar as tarefas.';
        _carregandoTarefas = false;
      });
    }
  }


  double get _progresso {
    if (_tarefas.isEmpty) {
      return 0;
    }

    final concluidas =
        _tarefas.where((item) => item.concluida).length;

    return (concluidas / _tarefas.length) * 100;
  }


  @override
  Widget build(BuildContext context) {



    return Scaffold(

      appBar: AppBar(
        title: const Text(
          "RoutinUP",
        ),
        centerTitle: true,
      ),


      body: Padding(

        padding: const EdgeInsets.all(20),


        child: Column(

          crossAxisAlignment: CrossAxisAlignment.start,


          children: [


            // SAUDAÇÃO

            Text(

              "Bem vindo, ${widget.nomeUsuario}!",

              style: const TextStyle(
                fontSize: 28,
                fontWeight: FontWeight.bold,
              ),

            ),

            const SizedBox(height: 25),

            // Chamando a barra de XP
            barraXpGame(),

            const SizedBox(height: 25),


            // PROGRESSO

            Container(

              width: double.infinity,

              padding: const EdgeInsets.all(20),


              decoration: BoxDecoration(

                borderRadius: BorderRadius.circular(20),

                color: Colors.black12,

              ),


              child: Column(

                crossAxisAlignment: CrossAxisAlignment.start,


                children: [


                  const Text(

                    "Desempenho do Dia",

                    style: TextStyle(
                      fontSize: 18,
                    ),

                  ),


                  const SizedBox(height: 10),



                  Text(

                    "${_progresso.toInt()}% concluída",

                    style: const TextStyle(

                      fontSize: 30,

                      fontWeight: FontWeight.bold,

                    ),

                  ),



                  const SizedBox(height: 15),



                  LinearProgressIndicator(

                    value: (_progresso / 100).clamp(0.0, 1.0).toDouble(),

                    minHeight: 10,

                    borderRadius: BorderRadius.circular(10),

                    color: const Color(0xFFFFB300),

                    backgroundColor: Colors.white24,

                  ),


                ],

              ),

            ),



            const SizedBox(height: 25),



        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            
            const Text(

              "Tarefas de hoje",

              style: TextStyle(

                fontSize: 24,

                fontWeight: FontWeight.bold,

              ),

            ),
            
            Text(
                "${_tarefas.length} tarefas",
                style: const TextStyle(
                  color: Color(0xFFFFB300),
                  fontSize: 14,
                  fontWeight: FontWeight.bold,
                ),
            ),

          ],
        ),


            const SizedBox(height: 14),


            // LISTA DE TAREFAS
            Expanded(
              child: _carregandoTarefas
                  ? const Center(
                child: CircularProgressIndicator(),
              )
                  : _erroTarefas != null
                  ? Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      _erroTarefas!,
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 10),
                    ElevatedButton(
                      onPressed: _carregarTarefas,
                      child: const Text('Tentar novamente'),
                    ),
                  ],
                ),
              )
                  : _tarefas.isEmpty
                  ? const Center(
                child: Text('Nenhuma tarefa para hoje.'),
              )
                  : ListView.builder(
                itemCount: _tarefas.length,
                itemBuilder: (context, index) {
                  return tarefa(context, _tarefas[index]);
                },
              ),
            ),



          ],

        ),

      ),

      // Botão Criar Tarefa
      floatingActionButtonLocation:
      FloatingActionButtonLocation.endFloat,

      floatingActionButton: Padding(
        padding: const EdgeInsets.only(
          right: 14,
          bottom: 28,
        ),

        child: SizedBox(
          width: 56,
          height: 56,

          child: FloatingActionButton(
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => CriarTarefaScreen(
                    usuarioId: widget.usuarioId,
                  ),
                ),
              );
            },

            backgroundColor: const Color(0xFFFFB300),

            foregroundColor: Colors.black,

            elevation: 5,

            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10),
            ),

            child: const Icon(
              Icons.add_rounded,
              size: 30,
            ),
          ),
        ),
      ),

    );

  }


  // CARD DA TAREFA
  Widget tarefa(BuildContext context, Tarefa tarefa) {

    return Padding(
      padding: const EdgeInsets.only(bottom: 12),

      child: InkWell(
        borderRadius: BorderRadius.circular(16),

        onTap: () {

          Navigator.push(
            context,

            MaterialPageRoute(
              builder: (context) => TaskDetailScreen(
                tarefa: tarefa,
              ),
            ),

          ).then((resultado) {

            setState(() {});

          });

        },

        child: Container(

          padding: const EdgeInsets.all(14),

          decoration: BoxDecoration(

            color: const Color(0xFF161616),

            borderRadius: BorderRadius.circular(16),

            border: Border.all(
              color: const Color(0xFFFFB300),
              width: 1.5,
            ),

          ),

          child: Row(

            children: [

              // ÍCONE DA TAREFA
              Container(

                width: 48,
                height: 48,

                decoration: BoxDecoration(

                  color: const Color(0xFF222222),

                  borderRadius: BorderRadius.circular(14),

                  border: Border.all(
                    color: const Color(0xFFFFB300),
                    width: 1,
                  ),

                ),

                child: const Icon(
                  Icons.task_alt,
                  color: Color(0xFFFFB300),
                  size: 26,
                ),

              ),

              const SizedBox(width: 14),

              // NOME + DESCRIÇÃO
              Expanded(

                child: Column(

                  crossAxisAlignment: CrossAxisAlignment.start,

                  children: [

                    Text(

                      tarefa.nome,

                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 17,
                        fontWeight: FontWeight.bold,
                      ),

                    ),

                    const SizedBox(height: 4),

                    Text(

                      tarefa.descricao,

                      maxLines: 1,

                      overflow: TextOverflow.ellipsis,

                      style: const TextStyle(
                        color: Colors.white70,
                        fontSize: 14,
                      ),

                    ),

                  ],

                ),

              ),

              const SizedBox(width: 8),

              // CHECKBOX
              Checkbox(

                value: tarefa.concluida,

                onChanged: (valor) {

                  setState(() {

                    tarefa.concluida = valor!;

                  });

                },

                activeColor: const Color(0xFFFFB300),

                checkColor: Colors.black,

                side: const BorderSide(
                  color: Colors.white,
                  width: 2,
                ),

              ),

            ],

          ),

        ),

      ),

    );

  }

  // BARRA DE XP ESTILO JOGO
  Widget barraXpGame() {
    // XP total necessário para alcançar o próximo nível
    final int xpProximoNivel =
        100 * (1 << (widget.nivel - 1));

    // XP total necessário para alcançar o nível atual
    final int xpInicioNivel = widget.nivel <= 1
        ? 0
        : 100 * (1 << (widget.nivel - 2));

    // XP acumulado dentro do nível atual
    final int xpAtualNivel =
    (widget.xp - xpInicioNivel)
        .clamp(0, xpProximoNivel - xpInicioNivel);

    // XP necessário para completar este nível
    final int xpNecessario =
        xpProximoNivel - xpInicioNivel;

    final double percentual =
    (xpAtualNivel / xpNecessario)
        .clamp(0.0, 1.0)
        .toDouble();

    return Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        // NÍVEL CENTRALIZADO
        Text(
          "Nível ${widget.nivel}",
          style: const TextStyle(
            color: Color(0xFFFFD54F),
            fontSize: 16,
            fontWeight: FontWeight.bold,
          ),
        ),

        const SizedBox(height: 4),

        // BARRA DE XP
        Container(
          width: double.infinity,
          height: 38,
          decoration: BoxDecoration(
            color: const Color(0xFF2B2B2B),
            borderRadius: BorderRadius.circular(20),
            border: Border.all(
              color: const Color(0xFFFFD54F),
              width: 2,
            ),
          ),
          child: ClipRRect(
            borderRadius: BorderRadius.circular(18),
            child: Stack(
              alignment: Alignment.centerLeft,
              children: [
                // PREENCHIMENTO DA BARRA
                FractionallySizedBox(
                  widthFactor: percentual,
                  heightFactor: 1,
                  child: Container(
                    color: const Color(0xFFFFB300),
                  ),
                ),

                // XP ATUAL / XP NECESSÁRIO
                Center(
                  child: Text(
                    "$xpAtualNivel/$xpNecessario XP",
                    style: TextStyle(
                      color: percentual >= 0.55
                          ? Colors.black
                          : Colors.white,
                      fontSize: 14,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }




}


