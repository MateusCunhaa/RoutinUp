import 'package:flutter/material.dart';
import 'package:routinup_app/screens/Home/home_screen.dart';
import 'package:routinup_app/services/api_service.dart';
import 'package:routinup_app/screens/Login/cadastro_screen.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {

  final TextEditingController emailController = TextEditingController();
  final TextEditingController senhaController = TextEditingController();

  String? mensagemErro;

  @override
  Widget build(BuildContext context) {

    return Scaffold(

      body: Center(

        child: SingleChildScrollView(

          padding: const EdgeInsets.all(30),

          child: Column(

            mainAxisAlignment: MainAxisAlignment.center,

            children: [

              // LOGO
              Image.asset(
                'assets/images/LogoRoutinUP.png',
                width: 230,
              ),

              const SizedBox(height: 50),


              // EMAIL
              TextField(

                controller: emailController,

                decoration: const InputDecoration(

                  labelText: "Gmail",

                  prefixIcon: Icon(
                    Icons.email,
                  ),

                ),

              ),


              const SizedBox(height: 20),


              // SENHA
              TextField(

                controller: senhaController,

                obscureText: true,

                decoration: const InputDecoration(

                  labelText: "Senha",

                  prefixIcon: Icon(
                    Icons.lock,
                  ),

                ),

              ),


              // MENSAGEM DE ERRO
              if (mensagemErro != null)

                Padding(

                  padding: const EdgeInsets.only(
                    top: 10,
                    left: 8,
                  ),

                  child: Row(

                    children: [

                      const Icon(
                        Icons.warning_amber_rounded,
                        size: 20,
                        color: Colors.redAccent,
                      ),

                      const SizedBox(width: 8),

                      Expanded(

                        child: Text(

                          mensagemErro!,

                          style: const TextStyle(
                            color: Colors.redAccent,
                            fontSize: 14,
                            fontWeight: FontWeight.w600,
                          ),

                        ),

                      ),

                    ],

                  ),

                ),


              // ESQUECI SENHA
              Align(

                alignment: Alignment.centerRight,

                child: TextButton(

                  onPressed: () {},

                  child: const Text(
                    "Esqueci a minha senha",
                  ),

                ),

              ),


              const SizedBox(height: 20),


              // BOTÃO ENTRAR
              SizedBox(

                width: double.infinity,

                child: ElevatedButton(

                  onPressed: () async {

                    String email = emailController.text;
                    String senha = senhaController.text;


                    final resultado = await ApiService.login(
                      email,
                      senha,
                    );


                    print("RESPOSTA RECEBIDA NA TELA:");
                    print(resultado);


                    // LOGIN CORRETO
                    if (resultado["nome"] != null) {

                      if (!mounted) return;


                      // Limpa qualquer erro anterior
                      setState(() {
                        mensagemErro = null;
                      });


                      Navigator.push(

                        context,

                        MaterialPageRoute(

                          builder: (context) => HomeScreen(

                            usuarioId: resultado["id"],

                            nomeUsuario: resultado["nome"],

                            progresso: 0,

                            sequencia: 0,

                            tarefas: [],

                            xp: resultado["xp"] ?? 0,

                            nivel: resultado["nivel"] ?? 1,

                            xpParaProximoNivel: 50,

                          ),

                        ),

                      );


                    } else {

                      if (!mounted) return;


                      // LOGIN INCORRETO
                      setState(() {

                        mensagemErro =
                            resultado["mensagem"] ??
                                "Usuário ou senha incorretos";

                      });

                    }

                  },


                  child: const Text(
                    "ENTRAR",
                  ),

                ),

              ),


              const SizedBox(height: 20),


              // CRIAR CONTA

              SizedBox(
                width: double.infinity,

                child: ElevatedButton(
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => const CadastroScreen(),
                      ),
                    );
                  },

                  child: const Text(
                    "CADASTRAR",
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),

            ],

          ),

        ),

      ),

    );

  }

}