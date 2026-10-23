import 'package:flutter/material.dart';
import '../../models/tarefa.dart';


class TaskDetailScreen extends StatelessWidget {


  final Tarefa tarefa;


  const TaskDetailScreen({

    super.key,

    required this.tarefa,

  });



  @override
  Widget build(BuildContext context) {


    return Scaffold(


      appBar: AppBar(

        title: Text(

          tarefa.nome,

        ),

      ),



      body: Padding(

        padding: const EdgeInsets.all(20),


        child: Column(


          crossAxisAlignment: CrossAxisAlignment.start,


          children: [



            Text(

              tarefa.nome,

              style: const TextStyle(

                fontSize: 32,

                fontWeight: FontWeight.bold,

              ),

            ),



            const SizedBox(height: 20),



            Text(

              "Categoria: ${tarefa.categoria}",

              style: const TextStyle(

                fontSize: 18,

              ),

            ),



            const SizedBox(height: 10),



            Text(

              "Horário: ${tarefa.horario}",

              style: const TextStyle(

                fontSize: 18,

              ),

            ),



            const SizedBox(height: 10),



            Text(

              "Descrição: ${tarefa.descricao}",

              style: TextStyle(

                fontSize: 18,

              ),

            ),


            const SizedBox(height: 10),



            Text(

              "Prioridade: ${tarefa.prioridade}",

              style: TextStyle(

                fontSize: 18,

              ),

            ),


            const SizedBox(height: 10),



            Text(

              "Sequencia: ${tarefa.sequencia}🔥",

              style: TextStyle(

                fontSize: 18,

              ),

            ),


            const Spacer(),

            SizedBox(

              width: double.infinity,

              child: ElevatedButton(

                onPressed: () {

                  tarefa.concluida = true;

                  Navigator.pop(context, tarefa);
                },

                child: const Text(

                  "CONCLUIR TAREFA",

                ),

              ),

            ),

            const SizedBox(height: 10),

            SizedBox(

              width: double.infinity,


              child: ElevatedButton(


                onPressed: () {},


                child: const Text(

                  "EDITAR TAREFA",

                ),

              ),

            ),

            const SizedBox(height: 10),

            Padding(padding: const EdgeInsets.only(bottom: 60),

            child: SizedBox(

              width: double.infinity,

              
              child: ElevatedButton(


                onPressed: () {},


                child: const Text(

                  "EXCLUIR TAREFA",

                ),

              ),

            ),
          )
        ],


        ),


      ),


    );


  }

}