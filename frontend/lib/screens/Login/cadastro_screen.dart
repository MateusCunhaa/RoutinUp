
import 'package:flutter/material.dart';
import 'package:routinup_app/services/api_service.dart';

class CadastroScreen extends StatefulWidget {
  const CadastroScreen({super.key});

  @override
  State<CadastroScreen> createState() => _CadastroScreenState();
}

class _CadastroScreenState extends State<CadastroScreen> {
  final nomeController = TextEditingController();
  final gmailController = TextEditingController();
  final senhaController = TextEditingController();
  final confirmarSenhaController = TextEditingController();

  String? mensagemErro;
  bool carregando = false;

  @override
  void dispose() {
    nomeController.dispose();
    gmailController.dispose();
    senhaController.dispose();
    confirmarSenhaController.dispose();
    super.dispose();
  }

  Future<void> realizarCadastro() async {
    final nome = nomeController.text.trim();
    final gmail = gmailController.text.trim();
    final senha = senhaController.text;
    final confirmarSenha = confirmarSenhaController.text;

    if (nome.isEmpty || gmail.isEmpty || senha.isEmpty ||
        confirmarSenha.isEmpty) {
      setState(() {
        mensagemErro = 'Preencha todos os campos.';
      });
      return;
    }

    if (!gmail.contains('@') || !gmail.contains('.')) {
      setState(() {
        mensagemErro = 'Digite um email válido.';
      });
      return;
    }

    if (senha.length < 6) {
      setState(() {
        mensagemErro = 'A senha deve ter pelo menos 6 caracteres.';
      });
      return;
    }

    if (senha != confirmarSenha) {
      setState(() {
        mensagemErro = 'As senhas não coincidem.';
      });
      return;
    }

    setState(() {
      carregando = true;
      mensagemErro = null;
    });

    try {
      final resultado = await ApiService.cadastrar(
        nome,
        gmail,
        senha,
      );

      if (!mounted) return;

      if (resultado['sucesso'] == true) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Cadastro realizado com sucesso!'),
          ),
        );

        Navigator.pop(context);
      } else {
        setState(() {
          mensagemErro = resultado['mensagem'] ??
              'Não foi possível realizar o cadastro.';
        });
      }
    } catch (e) {
      if (!mounted) return;

      setState(() {
        mensagemErro =
        'Não foi possível conectar ao servidor. Tente novamente.';
      });

      debugPrint('Erro no cadastro: $e');
    } finally {
      if (mounted) {
        setState(() {
          carregando = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    const dourado = Color(0xFFD4AF37);

    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Colors.black,
        foregroundColor: dourado,
        title: const Text('Criar conta'),
      ),
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(30),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Image.asset(
                'assets/images/LogoRoutinUP.png',
                width: 180,
                height: 150,
              ),

              const SizedBox(height: 20),

              const Text(
                'Comece sua jornada!',
                textAlign: TextAlign.center,
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),

              const SizedBox(height: 30),

              TextField(
                controller: nomeController,
                style: const TextStyle(color: Colors.white),
                decoration: const InputDecoration(
                  labelText: 'Nome',
                  prefixIcon: Icon(Icons.person),
                  border: OutlineInputBorder(),
                ),
              ),

              const SizedBox(height: 18),

              TextField(
                controller: gmailController,
                keyboardType: TextInputType.emailAddress,
                style: const TextStyle(color: Colors.white),
                decoration: const InputDecoration(
                  labelText: 'Gmail',
                  prefixIcon: Icon(Icons.email),
                  border: OutlineInputBorder(),
                ),
              ),

              const SizedBox(height: 18),

              TextField(
                controller: senhaController,
                obscureText: true,
                style: const TextStyle(color: Colors.white),
                decoration: const InputDecoration(
                  labelText: 'Senha',
                  prefixIcon: Icon(Icons.lock),
                  border: OutlineInputBorder(),
                ),
              ),

              const SizedBox(height: 18),

              TextField(
                controller: confirmarSenhaController,
                obscureText: true,
                style: const TextStyle(color: Colors.white),
                decoration: const InputDecoration(
                  labelText: 'Confirmar senha',
                  prefixIcon: Icon(Icons.lock_outline),
                  border: OutlineInputBorder(),
                ),
              ),

              if (mensagemErro != null) ...[
                const SizedBox(height: 15),
                Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Icon(
                      Icons.warning_amber_rounded,
                      color: Colors.redAccent,
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: Text(
                        mensagemErro!,
                        style: const TextStyle(
                          color: Colors.redAccent,
                        ),
                      ),
                    ),
                  ],
                ),
              ],

              const SizedBox(height: 30),

              SizedBox(
                height: 50,
                child: ElevatedButton(
                  onPressed: carregando ? null : realizarCadastro,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: dourado,
                    foregroundColor: Colors.black,
                  ),
                  child: carregando
                      ? const CircularProgressIndicator(
                    color: Colors.black,
                  )
                      : const Text(
                    'CRIAR CONTA',
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
