import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:routinup_app/models/tarefa.dart';
import 'package:routinup_app/models/categoria.dart';

class ApiService {

  static const String baseUrl = "http://10.0.2.2:8080";

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


    print("ENVIANDO PARA O BACKEND:");
    print(dados);


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


    return jsonDecode(resposta.body);
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

  // BUSCAR TAREFAS DE HOJE
  static Future<List<Tarefa>> listarTarefasHoje(
      int usuarioId,
      ) async {
    final url = Uri.parse(
      '$baseUrl/tarefas-hoje?usuarioId=$usuarioId',
    );

    final resposta = await http.get(
      url,
      headers: {
        'Accept': 'application/json',
      },
    );

    print('STATUS DAS TAREFAS: ${resposta.statusCode}');
    print('RESPOSTA DAS TAREFAS: ${resposta.body}');

    if (resposta.statusCode != 200) {
      throw Exception(
        'Erro ao buscar tarefas: HTTP ${resposta.statusCode}',
      );
    }

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
    final resposta = await http.get(
      Uri.parse(
        "$baseUrl/categorias?usuarioId=$usuarioId",
      ),
      headers: {
        "Accept": "application/json",
      },
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
    required int usuarioId,
    required String nome,
    required String cor,
    required String icone,
  }) async {
    final resposta = await http.post(
      Uri.parse("$baseUrl/categorias"),
      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json",
      },
      body: jsonEncode({
        "usuarioId": usuarioId,
        "nome": nome.trim(),
        "cor": cor,
        "icone": icone,
      }),
    );

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
    required int usuarioId,
    required String nome,
    required String cor,
    required String icone,
  }) async {
    final resposta = await http.put(
      Uri.parse("$baseUrl/categorias"),
      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json",
      },
      body: jsonEncode({
        "categoriaId": categoriaId,
        "usuarioId": usuarioId,
        "nome": nome.trim(),
        "cor": cor,
        "icone": icone,
      }),
    );

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
    required int usuarioId,
  }) async {
    final resposta = await http.delete(
      Uri.parse("$baseUrl/categorias"),
      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json",
      },
      body: jsonEncode({
        "categoriaId": categoriaId,
        "usuarioId": usuarioId,
      }),
    );

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