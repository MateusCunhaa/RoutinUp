package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Tarefa;
import model.Etapa;
import model.Categoria;

import repository.TarefaRepository;
import repository.CategoriaRepository;

import dto.login.LoginRequest;
import dto.login.LoginResponse;
import dto.login.LoginResult;
import dto.cadastro.CadastroRequest;
import dto.cadastro.CadastroResult;

import service.CadastroService;
import service.GerenciadorTarefas;
import service.LoginService;
import service.SessaoService;

public class UsuarioController {

    // Metodos

    // Metodo Principal (Main)
    public static void iniciarServidor() throws IOException {

        // Metodo cria um endereço de rede e guarda na variavel da classe HttpServer (classe responsável por criar e controlar um servidor HTTP)
        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);


        // Criação do endpoint /login, define HttpEchange (Troca de informações entre o cliente e o servidor HTTP)
        servidor.createContext("/login", troca -> {responderLogin(troca);});

        // Criação do endpoint /cadastro, define HttpEchange (Troca de informações entre o cliente e o servidor HTTP)
        servidor.createContext("/cadastro", troca -> {responderCadastro(troca);});

        // Criação do endpoint /tarefas-hoje, define HttpEchange (Troca de informações entre o cliente e o servidor HTTP)
        servidor.createContext("/tarefas-hoje", troca -> {responderTarefas(troca, true);});

        // Criação do endpoint /tarefas, define HttpEchange (Troca de informações entre o cliente e o servidor HTTP)
        servidor.createContext("/tarefas", troca -> {responderTarefas(troca, false);});

        // Criação do endpoint /categorias, define HttpEchange (Troca de informações entre o cliente e o servidor HTTP)
        servidor.createContext("/categorias", troca -> {responderCategorias(troca);});


        // Inicia o servidor
        servidor.start();

        System.out.println("Servidor iniciado na porta 8080!");

    }

    // Metodos principais
     private static void responderLogin(HttpExchange troca) throws IOException {

         // Cria objeto
         LoginService service = new LoginService();

         // Pega o body da requisicao ,converte de bytes pra caracteres e guarda na variavel da classe BufferedReader (facilita a leitura do conteudo)
         BufferedReader leitor = new BufferedReader(new InputStreamReader(troca.getRequestBody()));

         // Declara variavel que armazena todas as linhas unificadas
         String corpo = leitor.lines().reduce("", (a, b) -> a + b);

         // Extrai dados limpos (tira do JSON) e coloca dentro das variaveis

         String gmail = corpo.split("\"gmail\"\\s*:\\s*\"")[1].split("\"")[0];
         String senha = corpo.split("\"senha\"\\s*:\\s*\"")[1].split("\"")[0];


         // Cria objeto onde ficam os dados que chegaram (Requisicao)
         LoginRequest loginRequest = new LoginRequest(gmail,senha);

         // Pega resultado da tentativa de login (sucesso,mensagem,usuario)
         LoginResult resultado = service.fazerLogin(loginRequest);

         // Variavel (Atributo)
         String resposta;

         // Se sucesso deu "true"
         if (resultado.isSucesso()){

             // Pega usuario (id, nome, xp, nivel) e guarda na variavel
             LoginResponse usuarioResposta = resultado.getUsuario();

             // Cria tooken/sessao
             String token = SessaoService.criarSessao(usuarioResposta.getId());

             // Construindo resposta
             resposta = """
                 {
                 "mensagem":"%s",
                 "token" : "%s",
                 "id":%d,
                 "nome":"%s",
                 "xp":%d,
                 "nivel":%d
                 }        
                         """.formatted(resultado.getMensagem(),token,usuarioResposta.getId(), usuarioResposta.getNome(), usuarioResposta.getXp(),usuarioResposta.getNivel());

         }else {

             resposta = """
                 {
                    "mensagem":"%s"
                 }        
                         """.formatted(resultado.getMensagem());

         }

         // Formate e envia comunicação Json com HttpExchange,status e resposta
         enviarJson(troca, 200, resposta);


     }

     private static void responderCadastro (HttpExchange troca) throws IOException {

         // Cria objeto
         CadastroService cadastroService = new CadastroService();

         // Pega o body da requisição e converte para String
         String corpo = new String(troca.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);


         // Extrai dados limpos (tira do JSON) e coloca dentro das variaveis
         String nome = corpo.split("\"nome\"\\s*:\\s*\"")[1].split("\"")[0];
         String gmail = corpo.split("\"gmail\"\\s*:\\s*\"")[1].split("\"")[0];
         String senha = corpo.split("\"senha\"\\s*:\\s*\"")[1].split("\"")[0];

         // Cria objeto onde ficam os dados que chegaram (Requisicao)
         CadastroRequest cadastroRequest = new CadastroRequest(nome, gmail, senha);

         // Pega resultado da tentativa de cadastro (sucesso,mensagem)
         CadastroResult resultado = cadastroService.cadastrar(cadastroRequest);

         // Variavel (Atributo)
         String resposta;

         // Se sucesso deu "true"
         if (resultado.isSucesso()) {

             // Construindo resposta
             resposta = """
        {
            "sucesso":true,
            "mensagem":"%s"
        }
        """.formatted(resultado.getMensagem());

         } else {

             resposta = """
        {
            "sucesso":false,
            "mensagem":"%s"
        }
        """.formatted(resultado.getMensagem());

         }

         // Formate e envia comunicação Json com HttpExchange,status e resposta
         enviarJson(troca, 200, resposta);

     }

    private static void responderTarefas(com.sun.net.httpserver.HttpExchange troca, boolean somenteHoje) throws IOException {

        // Pega metodo request
        String metodo = troca.getRequestMethod();

        // Se não for "GET" ou "POST"
        if (!metodo.equalsIgnoreCase("GET") && !metodo.equalsIgnoreCase("POST")) {

            // Comunica erro
            enviarJson(troca, 405, "{\"mensagem\":\"Método não permitido\"}");
            // Retorna
            return;
        }

        // Retorna id do usuario pego pela sessao
        Integer usuarioId = obterUsuarioIdAutenticado(troca);

        // Se usuarioId é null
        if (usuarioId == null) {

            // Envia erro (status) e mensagem
            enviarJson(troca, 401, "{\"mensagem\":\"Sessão inválida ou expirada. Faça login novamente.\"}");

            // Retorna
            return;
        }

        // Cria objeto
        TarefaRepository repository = new TarefaRepository();

        // Cria lista de tarefas
        ArrayList<Tarefa> tarefas;

        // Se somenteHoje é true
        if (somenteHoje) {
            // Cria objeto
            GerenciadorTarefas gerenciador = new GerenciadorTarefas();

            tarefas = gerenciador.listarHoje(usuarioId);
        // Se não
        } else {
            // Lista todas tarefas do usuario
            tarefas = repository.listarTodos(usuarioId);
        }

        // Cria variavel que guarda lista formatada
        String resposta = serializarTarefas(tarefas);

        // Envia Json com HttpExchange,status e resposta
        enviarJson(troca, 200, resposta);
    }

    private static void responderCategorias(com.sun.net.httpserver.HttpExchange troca) throws IOException {

        // Se metodo for POST
        if (troca.getRequestMethod().equalsIgnoreCase("POST")) {
            // Metodo criar catergoria
            criarCategoria(troca);
            // Retorna
            return;
        }

        // Se metodo for PUT
        if (troca.getRequestMethod().equalsIgnoreCase("PUT")) {
            // Metodo editar categoria
            editarCategoria(troca);
            // Retorna
            return;
        }

        // Se metodo for DELETE
        if (troca.getRequestMethod().equalsIgnoreCase("DELETE")) {
            // Metodo excluir categoria
            excluirCategoria(troca);
            // Retorna
            return;
        }

        // Se metodo não for GET
        if (!troca.getRequestMethod().equalsIgnoreCase("GET")) {
            // Envia JSON comunicando erro
            enviarJson(troca, 405, "{\"mensagem\":\"Método não permitido\"}");
            // Retorna
            return;
        }

        // obtem usuario pela sessao
        Integer usuarioId = obterUsuarioIdAutenticado(troca);

        // Se usuario for null
        if (usuarioId == null) {
            // Envia JSON comunicando erro
            enviarJson(troca, 401,  "{\"mensagem\":\"Sessão inválida ou expirada. Faça login novamente.\"}");
            // Retorna
            return;
        }

        // Cria objeto
        CategoriaRepository repository = new CategoriaRepository();

        // Usa metodo pra pegar todas categorias do usuario (banco de dados) e cria lista e coloca esse categoria
        ArrayList<Categoria> categorias = repository.listarTodos(usuarioId);

        // // Cria objeto StringBuilder (classe do Java utilizada para construir e modificar textos), conteudo inicial do objeto "["
        StringBuilder json = new StringBuilder("[");

        // Enquanto tamanho da lista for maior que "i"
        for (int i = 0; i < categorias.size(); i++) {
            // Vai exibir todas categoria
            Categoria categoria = categorias.get(i);

            // Se i maior que 0
            if (i > 0) {
                // Adiciona virgula ao JSON
                json.append(",");
            }
            // Adiciona conteudo ao JSON (StringBuilder)
            json.append("{")

                    .append("\"id\":").append(categoria.getId()).append(",")
                    .append("\"nome\":").append(textoJson(categoria.getNome())).append(",")
                    .append("\"cor\":").append(textoJson(categoria.getCor())).append(",")
                    .append("\"icone\":").append(textoJson(categoria.getIcone())).append(",")
                    .append("\"usuarioId\":").append(categoria.getUsuarioId())
                    .append("}");
        }

        json.append("]");

        // Envia comunicação
        enviarJson(troca, 200, json.toString());
    }

    // Metodos

    private static String serializarTarefas(ArrayList<Tarefa> tarefas) {

        // Cria objeto StringBuilder (classe do Java utilizada para construir e modificar textos), conteudo inicial do objeto "["
        StringBuilder json = new StringBuilder("[");

        // Enquanto tamanho da lista tarefas for maior que i
        for (int i = 0; i < tarefas.size(); i++) {

            // Tarefa recebera cada elemento da lista
            Tarefa tarefa = tarefas.get(i);

            // Se "i" for maior que 0
            if (i > 0) {
                // Coloca a virgula
                json.append(",");
            }

            // Adiciona conteudo ao JSON (StringBuilder)
            json.append("{")
                    .append("\"id\":").append(tarefa.getId()).append(",")
                    .append("\"usuarioId\":").append(tarefa.getUsuarioId()).append(",")
                    .append("\"nome\":").append(textoJson(tarefa.getNome())).append(",")
                    .append("\"descricao\":").append(textoJson(tarefa.getDescricao())).append(",")
                    .append("\"horario\":").append(textoJson(tarefa.getHorario())).append(",")
                    .append("\"duracao\":").append(tarefa.getDuracao()).append(",")
                    .append("\"prioridade\":").append(tarefa.getPrioridade()).append(",")
                    .append("\"porcentagem\":").append(tarefa.getPorcentagem()).append(",")
                    .append("\"concluida\":").append(tarefa.isConcluida()).append(",")
                    .append("\"repeticao\":").append(tarefa.isRepeticao()).append(",");


            json.append("\"diasSemana\":[");

            // Enquanto tamanho da lista for maior que "d"
            for (int d = 0; d < tarefa.getDiasSemana().size(); d++) {

                // Se "d" for maior que 0
                if (d > 0) {

                    json.append(",");
                }
                // Adiciona todos os dias que tarefa possui ao JSON
                json.append(textoJson(tarefa.getDiasSemana().get(d)));
            }

            json.append("],");


            json.append("\"dataConclusao\":");

            // Se Data conclusão for null
            if (tarefa.getDataConclusao() == null) {
                json.append("null");
            // Se não
            } else {
                json.append(textoJson(tarefa.getDataConclusao().toString()));
            }

            json.append(",")
                    .append("\"sequencia\":").append(tarefa.getSequencia()).append(",")
                    .append("\"bonusHabitoRecebido\":")
                    .append(tarefa.isBonusHabitoRecebido()).append(",")
                    .append("\"xpRecebido\":").append(tarefa.isXpRecebido()).append(",");

            // Categoria completa
            json.append("\"categoria\":");

            // Se categoria for NULL
            if (tarefa.getCategoria() == null) {

                json.append("null");
            // Se não
            } else {

                // Pega categoria e armazena na variavel
                var categoria = tarefa.getCategoria();

                json.append("{")
                        .append("\"id\":").append(categoria.getId()).append(",")
                        .append("\"nome\":").append(textoJson(categoria.getNome())).append(",")
                        .append("\"cor\":").append(textoJson(categoria.getCor())).append(",")
                        .append("\"usuarioId\":").append(categoria.getUsuarioId())
                        .append("}");
            }

            // Etapas da tarefa
            json.append(",\"etapas\":[");

            // Enquanto lista de tarefas for maior que "e"
            for (int e = 0; e < tarefa.getEtapas().size(); e++) {

                // Pega todas etapas da lista
                Etapa etapa = tarefa.getEtapas().get(e);

                // Se "e" maior que 0
                if (e > 0) {
                    json.append(",");
                }

                json.append("{")
                        .append("\"id\":").append(etapa.getId()).append(",")
                        .append("\"nome\":").append(textoJson(etapa.getNome())).append(",")
                        .append("\"peso\":").append(etapa.getPeso()).append(",")
                        .append("\"concluida\":").append(etapa.isConcluida())
                        .append("}");
            }

            json.append("]}");
        }

        json.append("]");

        // Retorna JSON
        return json.toString();
    }

    private static void criarCategoria(com.sun.net.httpserver.HttpExchange troca) throws IOException {

        // obtem usuario pela sessão
        Integer usuarioId = obterUsuarioIdAutenticado(troca);

        // Se usuario id for NULL
        if (usuarioId == null) {

            // Envia comunicação
            enviarJson(troca, 401, "{\"mensagem\":\"Sessão inválida ou expirada. Faça login novamente.\"}");
            // Retorna
            return;
        }

        // Lê e salva body na variavel
        String corpo = new String(troca.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        // Extrai campo do body
        String nome = extrairCampoJsonCategoria(corpo, "nome");
        String cor = extrairCampoJsonCategoria(corpo, "cor");
        String icone = extrairCampoJsonCategoria(corpo, "icone");

        // Se algum campo vazio
        if (nome == null || cor == null || icone == null) {
            // Envia comunicacao
            enviarJson(troca, 400, "{\"mensagem\":\"Informe nome, cor e icone\"}");
            // Retorna
            return;
        }

        // Formata atributos (corta)
        nome = nome.trim();
        cor = cor.trim();
        icone = icone.trim();

        // Se algum atributo esiver fora das regras
        if (nome.isEmpty() || nome.length() > 100 ||
                !cor.matches("#[0-9a-fA-F]{6}") ||
                icone.isEmpty() || icone.length() > 100) {
            // Envia comunicacao
            enviarJson(troca, 400, "{\"mensagem\":\"Dados da categoria inválidos\"}");
            // Retorna
            return;
        }

        // Cria objeto
        CategoriaRepository repository = new CategoriaRepository();

        // Se ja tem categoria com nome igual
        if (repository.existeNomeVisivelParaUsuario(nome, usuarioId)) {
            // Envia comunicacao
            enviarJson(troca, 409, "{\"mensagem\":\"Já existe uma categoria com esse nome\"}");
            // Retorna
            return;
        }

        // Cria objeto categoria
        Categoria categoria = new Categoria(nome, cor, icone);


        try {
            // Salva categoria no banco de dados
            repository.salvar(categoria, usuarioId);

            // Cria JSON e preenche
            String json = "{"
                    + "\"id\":" + categoria.getId() + ","
                    + "\"nome\":" + textoJson(categoria.getNome()) + ","
                    + "\"cor\":" + textoJson(categoria.getCor()) + ","
                    + "\"icone\":" + textoJson(categoria.getIcone()) + ","
                    + "\"usuarioId\":" + usuarioId
                    + "}";

            // Envia comunicacao
            enviarJson(troca, 201, json);

        // Pega erro
        } catch (RuntimeException e) {
            // Envia erro pro ao console do programa Java
            System.err.println("Erro ao salvar categoria: " + e.getMessage());
            // Envia comunicacao
            enviarJson(troca, 500, "{\"mensagem\":\"Não foi possível salvar a categoria\"}");
        }
    }

    // Valida,comunica,formata,atualiza o banco de dados
    private static void editarCategoria(com.sun.net.httpserver.HttpExchange troca) throws IOException {

        // Obter usuario id pela sessao
        Integer usuarioId = obterUsuarioIdAutenticado(troca);

        // Se usuario id for null
        if (usuarioId == null) {
            // Envia comunicacao
            enviarJson(troca, 401, "{\"mensagem\":\"Sessão inválida ou expirada. Faça login novamente.\"}");
            // Retorna
            return;
        }

        // Lê e recebe o body e guarda na variavel
        String corpo = new String(troca.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        // Extrai atributos recebidos do body
        String categoriaIdTextoF = extrairNumeroJsonCategoria(corpo, "categoriaId");
        String nome = extrairCampoJsonCategoria(corpo, "nome");
        String cor = extrairCampoJsonCategoria(corpo, "cor");
        String icone = extrairCampoJsonCategoria(corpo, "icone");

        // Se algum campo estiver NULL
        if (categoriaIdTextoF == null || nome == null || cor == null || icone == null) {
            // Envia comunicacao
            enviarJson(troca, 400, "{\"mensagem\":\"Informe categoriaId, nome, cor e icone\"}");
            // Retorna
            return;
        }


        int categoriaId;

        try {
            // Recebe categoria id
            categoriaId = Integer.parseInt(categoriaIdTextoF);

            // Se categoria id menor ou igual a 0
            if (categoriaId <= 0 ) {
                // Comunica excessao
                throw new NumberFormatException();
            }
        // Pega excessao
        } catch (NumberFormatException e) {
            // Envia comunicacao
            enviarJson(troca, 400, "{\"mensagem\":\"CategoriaId inválido\"}");
            // Retorna
            return;
        }

        // Formata atributos (corta)
        nome = nome.trim();
        cor = cor.trim();
        icone = icone.trim();

        // Se algum atributo esiver fora das regras
        if (nome.isEmpty() || nome.length() > 100 || !cor.matches("#[0-9a-fA-F]{6}") || icone.isEmpty() || icone.length() > 100) {
            // Envia comunicacao
            enviarJson(troca, 400, "{\"mensagem\":\"Dados da categoria inválidos\"}");return;
        }
        // Cria objeto
        CategoriaRepository repository = new CategoriaRepository();

        // Se categoria esta repitida
        if (repository.existeNomeVisivelParaUsuarioExcetoId(nome, usuarioId, categoriaId)) {
            // Envia comunicacao
            enviarJson(troca, 409, "{\"mensagem\":\"Já existe uma categoria com esse nome\"}");
            // Retorna
            return;
        }

        // Cria objeto categoria
        Categoria categoria = new Categoria(nome, cor, icone);

        // Informar a ele qual é o ID da categoria que será atualizada no banco de dados
        categoria.setId(categoriaId);

        // Confere se atualizou categoria personaliza no banco de dados
        boolean atualizou = repository.atualizarPersonalizada(categoria, usuarioId);

        // Se não atualizou
        if (!atualizou) {
            // Envia comunicacao
            enviarJson(troca, 403, "{\"mensagem\":\"Categoria não encontrada ou não pertence ao usuário\"}"
            );
            // Retorna
            return;
        }

        // Define e cria JSON
        String json = "{"
                + "\"id\":" + categoriaId + ","
                + "\"nome\":" + textoJson(nome) + ","
                + "\"cor\":" + textoJson(cor) + ","
                + "\"icone\":" + textoJson(icone) + ","
                + "\"usuarioId\":" + usuarioId
                + "}";

        // Envia comunicacao
        enviarJson(troca, 200, json);
    }


    private static void excluirCategoria(com.sun.net.httpserver.HttpExchange troca) throws IOException {

        // Obtem usuario pela sessao
        Integer usuarioId = obterUsuarioIdAutenticado(troca);

        // Se usuario id for NULL
        if (usuarioId == null) {
            // Envia comunicacao
            enviarJson(troca, 401, "{\"mensagem\":\"Sessão inválida ou expirada. Faça login novamente.\"}");
            // Retorna
            return;
        }

        // Lê e Recebe body
        String corpo = new String(troca.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        // Extrai categoriaID e coloca na variavel
        String categoriaIdTexto = extrairNumeroJsonCategoria(corpo, "categoriaId");

        // Se categoria id for NULL
        if (categoriaIdTexto == null) {
            // Envia comunicacao
            enviarJson(troca, 400, "{\"mensagem\":\"Informe categoriaId\"}");
            // Retorna
            return;
        }

        int categoriaId;


        try {
            // Recebe categoria id
            categoriaId = Integer.parseInt(categoriaIdTexto);

            // Se categoria id menor ou igual a 0
            if (categoriaId <= 0) {
                // Comunica excessao
                throw new NumberFormatException();
            }
        // Pega excessao
        } catch (NumberFormatException e) {
            // Envia comunicacao
            enviarJson(troca, 400, "{\"mensagem\":\"CategoriaId inválido\"}");
            // Retorna
            return;
        }
        // Cria objeto
        CategoriaRepository repository = new CategoriaRepository();

        // Remove Categoria do banco de dados
        boolean removeu = repository.removerPersonalizada(categoriaId, usuarioId);

        // Se não removeu
        if (!removeu) {
            // Envia comunicacao
            enviarJson(troca, 403, "{\"mensagem\":\"Categoria não encontrada ou não pertence ao usuário\"}");
            // Retorna
            return;
        }
        // Envia comunicacao
        enviarJson(troca, 200, "{\"mensagem\":\"Categoria excluída com sucesso\"}");
    }

    // Prepara textos para JSON sem quebrar a estrutura (formatação)
    private static String escaparJson(String valor) {

        // Se valor é null
        if (valor == null) {

            // Retorna vazio
            return "";
        }
        // Retrona valor substituindo caracteres especiais por suas representações de escape
        return valor
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String extrairCampoJsonCategoria(String json, String campo) {
        // Define regex, protege nome do campo para que seja interpretado exatamente como foi escrito e guarda na variavel
        String regex = "\"" + Pattern.quote(campo) + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"";

        // Recebe texto onde a busca sera realizada, compila o regex e guarda em matcher (Classe responsavel por procurar correspondências de uma expressão regular)
        Matcher matcher = Pattern.compile(regex).matcher(json);

        // Se não encontrar
        if (!matcher.find()) {
            // Retorna NULL
            return null;
        }

        // Retorna conteúdo encontrado e capturado pelo primeiro grupo de captura da Regex.
        return matcher.group(1)
                // Formata
                .replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\\", "\\");
    }

    // extrair variavel
    private static String extrairNumeroJsonCategoria(String json, String campo) {

        // Define regex, protege nome do campo para que seja interpretado exatamente como foi escrito e guarda na variavel
        String regex = "\"" + Pattern.quote(campo) + "\"\\s*:\\s*(\\d+)";
        // Recebe texto onde a busca sera realizada, compila o regex e guarda em matcher (Classe responsavel por procurar correspondências de uma expressão regular)
        Matcher matcher = Pattern.compile(regex).matcher(json);

        // Se não encontrar
        if (!matcher.find()) {
            // Retorna NULL
            return null;
        }

        // Retorna conteúdo encontrado e capturado pelo primeiro grupo de captura da Regex.
        return matcher.group(1);
    }

    private static String textoJson(String texto) {

        // Se texto for null
        if (texto == null) {
            // Retorna palavra null
            return "null";
        }

        // Retorna texto formatado para JSON
        return "\""
                + texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                + "\"";
    }


    private static void enviarJson(com.sun.net.httpserver.HttpExchange troca, int status, String resposta) throws IOException {

        // Pega bytes de resposta e guarda na variavel
        byte[] dados = resposta.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        // Altera cabeçalhos
        troca.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        // Envia cabeçalhos
        troca.sendResponseHeaders(status, dados.length);


        try
                // Pega caminho do body da resposta e coloca na variavel
                (var corpo = troca.getResponseBody()) {
            // Escreve os dados no body atraves do caminho
            corpo.write(dados);
        }
    }



    private static Integer obterUsuarioIdAutenticado(com.sun.net.httpserver.HttpExchange troca) {

        // Recuperar cabeçalho Authorization e guarda ele na variavel autorizacao
        String autorizacao = troca.getRequestHeaders().getFirst("Authorization");

        // Se token for NULL ou se aurorização mão começar com "Bearer "
        if (autorizacao == null || !autorizacao.startsWith("Bearer ")) {

            // Retorna null
            return null;
        }

        // Extrai token (Remove "Bearer ", 7 caracteres)
        String token = autorizacao.substring(7).trim();

        // Se token for vazio
        if (token.isEmpty()) {

            // Retorna null
            return null;
        }

        // Retorna usuarioId pego pelo token
        return SessaoService.buscarUsuarioId(token);

    }



}