import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:routinup_app/models/tarefa.dart';
import 'package:routinup_app/models/categoria.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter/foundation.dart';

class ApiService {

  static const String baseUrl = "http://10.0.2.2:8080";

  // Armazenamento seguro da sessão
  static const FlutterSecureStorage _armazenamentoSeguro = FlutterSecureStorage();

  static const String _chaveToken = 'routinup_sessao_token';


  // CABEÇALHOS DAS REQUISIÇÕES AUTENTICADAS
  static Future<Map<String, String>> headersComToken({
    bool enviarJson = false,
  }) async {

    final token = await obterToken();

    if (token == null || token.isEmpty) {
      throw Exception(
        'Sessão não encontrada. Faça login novamente.',
      );
    }

    return {
      'Accept': 'application/json',
      if (enviarJson)
        'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    };
  }


  // Recuperar token
  static Future<String?> obterToken() async {

    return await _armazenamentoSeguro.read(key: _chaveToken,);
  }

  // Encerrar sessão
  static Future<void> limparSessao() async {

    await _armazenamentoSeguro.delete(key: _chaveToken,);
  }

  // LOGIN
  static Future<Map<String, dynamic>> login(
      String gmail,
      String senha,
      ) async {

    // Dados que serão enviados para o Java
    final dados = {
      "gmail": gmail,
      "senha": senha,
    };

    final resposta = await http.post(
      Uri.parse("$baseUrl/login"),

      headers: {
        "Content-Type": "application/json",
      },

      body: jsonEncode(dados),
    );


    print("STATUS RECEBIDO:");
    print(resposta.statusCode);

    print("RESPOSTA DO BACKEND:");
    print(resposta.body);

    // Converter resposta do JAVA
    final Map<String,dynamic> dadosResposta = Map<String, dynamic>.from(jsonDecode(resposta.body) as Map,);

    debugPrint('===== RESPOSTA DO LOGIN =====');
    debugPrint('Status HTTP: ${resposta.statusCode}');
    debugPrint('Mensagem: ${dadosResposta['mensagem']}');
    debugPrint('ID: ${dadosResposta['id']}');
    debugPrint('Token recebido: ${dadosResposta['token'] != null}',
    );

    // Recuperar token gerado pelo JAVA
    final token = dadosResposta['token']?.toString();

    // Salvar token se login for realizado
    if (resposta.statusCode == 200 && token != null && token.isNotEmpty) {

      await _armazenamentoSeguro.write(key: _chaveToken, value: token,);
    }

    return dadosResposta;

  }

  // CADASTRO
  static Future<Map<String, dynamic>> cadastrar(
      String nome,
      String gmail,
      String senha,
      ) async {

    final dados = {
      "nome": nome,
      "gmail": gmail,
      "senha": senha,
    };

    print("ENVIANDO CADASTRO PARA O BACKEND:");
    print(dados);

    final resposta = await http.post(
      Uri.parse("$baseUrl/cadastro"),

      headers: {
        "Content-Type": "application/json",
      },

      body: jsonEncode(dados),
    );

    print("STATUS DO CADASTRO:");
    print(resposta.statusCode);

    print("RESPOSTA DO CADASTRO:");
    print(resposta.body);

    return jsonDecode(resposta.body);
  }


  static Future<List<Tarefa>> listarTarefasHoje() async {

    // 1. RECUPERAR TOKEN
    final headers = await headersComToken();

    // 2. SOLICITAR TAREFAS AO BACKEND
    final resposta = await http.get(
      Uri.parse('$baseUrl/tarefas-hoje'),
      headers: headers,
    );

    print('STATUS DAS TAREFAS: ${resposta.statusCode}');
    print('RESPOSTA DAS TAREFAS: ${resposta.body}');

    // 3. VERIFICAR RESPOSTA
    if (resposta.statusCode != 200) {
      throw Exception(
        'Erro ao buscar tarefas: ${resposta.statusCode}',
      );
    }

    // 4. CONVERTER JSON
    final List<dynamic> dados =
    jsonDecode(resposta.body) as List<dynamic>;

    return dados.map((item) {
      return Tarefa.fromJson(
        Map<String, dynamic>.from(item as Map),
      );
    }).toList();
  }




  // LISTAR CATEGORIAS
  static Future<List<Categoria>> listarCategorias(
      int usuarioId,
      ) async {

    final headers = await headersComToken();

    final resposta = await http.get(
      Uri.parse(
        "$baseUrl/categorias?usuarioId=$usuarioId",
      ),
      headers: headers,
    );

    if (resposta.statusCode != 200) {
      throw Exception(
        "Erro ao carregar categorias: ${resposta.statusCode}",
      );
    }

    final List<dynamic> dados =
    jsonDecode(resposta.body) as List<dynamic>;

    return dados
        .map(
          (categoria) => Categoria.fromJson(
        Map<String, dynamic>.from(categoria as Map),
      ),
    )
        .toList();
  }


  static Future<Map<String, dynamic>> criarCategoria({
    required String nome,
    required String cor,
    required String icone,
  }) async {

    // Pega token e outros cabeçalhos
    final headers = await headersComToken(enviarJson: true,);

    // Envia requisição ao JAVA
    final resposta = await http.post(
      Uri.parse("$baseUrl/categorias"),
      headers: headers,

      body: jsonEncode({
        "nome": nome.trim(),
        "cor": cor,
        "icone": icone,
      }),
    );

    // Converte resposta do java
    final Map<String, dynamic> dados =
    Map<String, dynamic>.from(
      jsonDecode(resposta.body) as Map,
    );

    if (resposta.statusCode != 201) {
      throw Exception(
        dados["mensagem"] ?? "Erro ao criar categoria",
      );
    }

    return dados;
  }


  static Future<Map<String, dynamic>> editarCategoria({
    required int categoriaId,
    required String nome,
    required String cor,
    required String icone,
  }) async {

    final headers = await headersComToken(enviarJson: true,);

    // Envia requisição PUT (atualizar)
    final resposta = await http.put(Uri.parse("$baseUrl/categorias"),

      headers: headers,

      body: jsonEncode({
        "categoriaId": categoriaId,
        "nome": nome.trim(),
        "cor": cor,
        "icone": icone,
      }),
    );

    // Converte resposta que vem do Java
    final Map<String, dynamic> dados =
    Map<String, dynamic>.from(
      jsonDecode(resposta.body) as Map,
    );

    if (resposta.statusCode != 200) {
      throw Exception(
        dados["mensagem"] ?? "Erro ao editar categoria",
      );
    }

    return dados;
  }


  static Future<void> excluirCategoria({
    required int categoriaId,
  }) async {
    // Pega token e outros cabeçalhos
    final headers = await headersComToken(enviarJson: true);

    // Envia requisição delete
    final resposta = await http.delete(
      Uri.parse("$baseUrl/categorias"),

    headers:headers,

      body: jsonEncode({
        "categoriaId": categoriaId,
      }),
    );

    // Converte resposta Java
    final Map<String, dynamic> dados =
    Map<String, dynamic>.from(
      jsonDecode(resposta.body) as Map,
    );

    if (resposta.statusCode != 200) {
      throw Exception(
        dados["mensagem"] ?? "Erro ao excluir categoria",
      );
    }
  }




}