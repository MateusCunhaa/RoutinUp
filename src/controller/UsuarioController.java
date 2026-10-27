package controller;

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
import service.LoginService;


public class UsuarioController {

    // criou porta no servidor
    public static void iniciarServidor() throws IOException {

        HttpServer servidor = HttpServer.create(
                new InetSocketAddress(8080),
                0
        );


        // Criação do endpoint /login
        servidor.createContext("/login", troca -> {

            // Cria objeto de LoginService
            LoginService service = new LoginService();

            // LER JSON QUE VEM DO FLUTTER
            BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(troca.getRequestBody())
            );


            // String corpo fica com os dados que foram recebidos
            String corpo = leitor.lines().reduce("", (a, b) -> a + b);

            // Extrai dados limpos (tira do JSON) e coloca dentro das variaveis

            String gmail = corpo.split("\"gmail\"\\s*:\\s*\"")[1].split("\"")[0];
            String senha = corpo.split("\"senha\"\\s*:\\s*\"")[1].split("\"")[0];


            // Cria objeto onde ficam os dados que chegaram
            LoginRequest loginRequest = new LoginRequest(gmail,senha);

            // Envia os dados do login para o Service
             LoginResult resultado = service.fazerLogin(loginRequest);

             // Variavel (Atributo)
             String resposta;

             // Confere foi feito com sucesso e constroi a resposta
             if (resultado.isSucesso()){

                 LoginResponse usuarioResposta = resultado.getUsuario();

                 //Exibir login (Temporario)
                 System.out.println("===== LOGIN RECEBIDO =====");
                 System.out.println("Gmail: " + gmail);
                 System.out.println("Senha: " + senha);
                 System.out.println("Login realizado com sucesso!");


                 resposta = """
                 {
                 "mensagem":"%s",
                 "id":%d,
                 "nome":"%s",
                 "xp":%d,
                 "nivel":%d
                 }        
                         """.formatted(resultado.getMensagem(),usuarioResposta.getId(), usuarioResposta.getNome(), usuarioResposta.getXp(),usuarioResposta.getNivel());

             }else {

                 //Exibir login (Temporario)
                 System.out.println("===== LOGIN RECEBIDO =====");
                 System.out.println("Gmail: " + gmail);
                 System.out.println("Senha: " + senha);
                 System.out.println("Usuario não encontrado");

                 resposta = """
                 {
                    "mensagem":"%s"
                 }        
                         """.formatted(resultado.getMensagem());

             }

            // Pega e adiciona o cabeçalho da resposta que vou enviar
            troca.getResponseHeaders().add("Content-Type", "application/json");

            // Manda o cabeçalho da resposta para quem fez a solicitação
            troca.sendResponseHeaders(200, resposta.getBytes().length);

            // Manda o body da resposta para o solicitante
            troca.getResponseBody().write(resposta.getBytes());

            System.out.println("Servidor iniciado na porta 8080!");

            troca.close();

        });

        // Criação do endpoint /cadastro
        servidor.createContext("/cadastro", troca ->{

            // Cria objeto de CadastroService
            CadastroService cadastroService = new CadastroService();

            // Lê o JSON que vem do Flutter
            BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(troca.getRequestBody())
            );

            // String corpo fica com os dados que foram recebidos
            String corpo = leitor.lines().reduce("", (a, b) -> a + b);


            // Extrai dados limpos (tira do JSON) e coloca dentro das variaveis
            String nome = corpo.split("\"nome\"\\s*:\\s*\"")[1].split("\"")[0];
            String gmail = corpo.split("\"gmail\"\\s*:\\s*\"")[1].split("\"")[0];
            String senha = corpo.split("\"senha\"\\s*:\\s*\"")[1].split("\"")[0];

            // Cria objeto onde ficam os dados que chegaram
            CadastroRequest cadastroRequest = new CadastroRequest(nome, gmail, senha);

            // Envia os dados de cadastro para o Service
            CadastroResult resultado = cadastroService.cadastrar(cadastroRequest);

            // Variavel (Atributo)
            String resposta;

            // Confere se cadastro foi feito com sucesso e constroi resposta
            if (resultado.isSucesso()) {

                //Exibir cadastro (Temporario)
                System.out.println("===== CADASTRO RECEBIDO =====");
                System.out.println("Nome: " + nome);
                System.out.println("Gmail: " + gmail);
                System.out.println("Senha: " + senha);
                System.out.println("Cadastro realizado com sucesso!");

                resposta = """
        {
            "sucesso":true,
            "mensagem":"%s"
        }
        """.formatted(resultado.getMensagem());

            } else {

                //Exibir cadastro (Temporario)
                System.out.println("===== CADASTRO RECEBIDO =====");
                System.out.println("Nome: " + nome);
                System.out.println("Gmail: " + gmail);
                System.out.println("Senha: " + senha);
                System.out.println("Erro no cadastro!");

                resposta = """
        {
            "sucesso":false,
            "mensagem":"%s"
        }
        """.formatted(resultado.getMensagem());

            }

            // Pega e adiciona o cabeçalho da resposta que vou enviar
            troca.getResponseHeaders().add("Content-Type", "application/json");

            // Manda o cabeçalho da resposta para quem fez a solicitação
            troca.sendResponseHeaders(200, resposta.getBytes().length);

            // Manda o body da resposta para o solicitante
            troca.getResponseBody().write(resposta.getBytes());


            troca.close();

        });

        // Criação do endpoint /tarefas-hoje
        servidor.createContext("/tarefas-hoje", troca -> {
            responderTarefas(troca, true);
        });

        // Criação do endpoint /tarefas
        servidor.createContext("/tarefas", troca -> {
            responderTarefas(troca, false);
        });

        // Criação do endpoint /categorias
        servidor.createContext("/categorias", troca -> {
            responderCategorias(troca);
        });


        // liga o servidor
        servidor.start();

    }

    // Metodos

    // Metodo que prepara textos para serem inseridos no JSON sem quebrar a estrutura (formata)
    private static String escaparJson(String valor) {

        if (valor == null) {
            return "";
        }

        return valor
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    //
    private static void responderTarefas(

            // Variavel (Abributo)
            com.sun.net.httpserver.HttpExchange troca, // Classe nativa do java, troca referencia um objeto que contém informações e recursos relacionados à requisição HTTP
            boolean somenteHoje

    )
     //Metodo pode gerar excessão (erro ) essa exceção poderá ser repassada para quem chamou o metodo
     throws IOException {

        // Se não for "Get" envia um erro pro solicitante
        if (!troca.getRequestMethod().equalsIgnoreCase("GET")) {
            enviarJson(troca, 405,
                    "{\"mensagem\":\"Método não permitido\"}");
            return;
        }

        // Pega dados dps do "?" da URL (o ID no caso)
        String consulta = troca.getRequestURI().getRawQuery();
        String usuarioIdTexto = null;

        // Formata ID
        if (consulta != null) {
            for (String parametro : consulta.split("&")) {
                String[] partes = parametro.split("=", 2); // Divide parametro em 2 partes, "=" indica onde texto sera dividido

                if (partes.length == 2
                        && partes[0].equals("usuarioId")) {
                    usuarioIdTexto = partes[1]; // Pega o ID formatado
                    break;
                }
            }
        }

        // Se o ID for null envia erro pro solicitante
        if (usuarioIdTexto == null) {
            enviarJson(troca, 400,
                    "{\"mensagem\":\"Informe o usuarioId\"}");
            return;
        }

        // Variavel (Atributo)
        int usuarioId;


        try {
            usuarioId = Integer.parseInt(usuarioIdTexto);

            if (usuarioId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            enviarJson(troca, 400,
                    "{\"mensagem\":\"usuarioId inválido\"}");
            return;
        }

        TarefaRepository repository = new TarefaRepository();

        ArrayList<Tarefa> tarefas;

        if (somenteHoje) {
            tarefas = repository.listarHoje(usuarioId);
        } else {
            tarefas = repository.listarTodos(usuarioId);
        }

        String resposta = serializarTarefas(tarefas);

        enviarJson(troca, 200, resposta);
    }

    //
    private static String serializarTarefas(
            ArrayList<Tarefa> tarefas
    ) {

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < tarefas.size(); i++) {

            Tarefa tarefa = tarefas.get(i);

            if (i > 0) {
                json.append(",");
            }

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

            // Dias da semana
            json.append("\"diasSemana\":[");

            for (int d = 0; d < tarefa.getDiasSemana().size(); d++) {

                if (d > 0) {
                    json.append(",");
                }

                json.append(textoJson(tarefa.getDiasSemana().get(d)));
            }

            json.append("],");

            // Data de conclusão
            json.append("\"dataConclusao\":");

            if (tarefa.getDataConclusao() == null) {
                json.append("null");
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

            if (tarefa.getCategoria() == null) {

                json.append("null");

            } else {

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

            for (int e = 0; e < tarefa.getEtapas().size(); e++) {

                Etapa etapa = tarefa.getEtapas().get(e);

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

        return json.toString();
    }


    private static void responderCategorias(
            com.sun.net.httpserver.HttpExchange troca
    ) throws IOException {

        if (troca.getRequestMethod().equalsIgnoreCase("POST")) {
            criarCategoria(troca);
            return;
        }

        if (troca.getRequestMethod().equalsIgnoreCase("PUT")) {
            editarCategoria(troca);
            return;
        }

        if (troca.getRequestMethod().equalsIgnoreCase("DELETE")) {
            excluirCategoria(troca);
            return;
        }

        if (!troca.getRequestMethod().equalsIgnoreCase("GET")) {
            enviarJson(
                    troca,
                    405,
                    "{\"mensagem\":\"Método não permitido\"}"
            );
            return;
        }

        String consulta = troca.getRequestURI().getRawQuery();
        String usuarioIdTexto = null;

        if (consulta != null) {
            for (String parametro : consulta.split("&")) {
                String[] partes = parametro.split("=", 2);

                if (partes.length == 2
                        && partes[0].equals("usuarioId")) {
                    usuarioIdTexto = partes[1];
                    break;
                }
            }
        }

        if (usuarioIdTexto == null) {
            enviarJson(
                    troca,
                    400,
                    "{\"mensagem\":\"Informe o usuarioId\"}"
            );
            return;
        }

        int usuarioId;

        try {
            usuarioId = Integer.parseInt(usuarioIdTexto);

            if (usuarioId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            enviarJson(
                    troca,
                    400,
                    "{\"mensagem\":\"usuarioId inválido\"}"
            );
            return;
        }

        CategoriaRepository repository = new CategoriaRepository();

        ArrayList<Categoria> categorias =
                repository.listarTodos(usuarioId);

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < categorias.size(); i++) {
            Categoria categoria = categorias.get(i);

            if (i > 0) {
                json.append(",");
            }

            json.append("{")
                    .append("\"id\":").append(categoria.getId()).append(",")
                    .append("\"nome\":").append(textoJson(categoria.getNome())).append(",")
                    .append("\"cor\":").append(textoJson(categoria.getCor())).append(",")
                    .append("\"icone\":").append(textoJson(categoria.getIcone())).append(",")
                    .append("\"usuarioId\":").append(categoria.getUsuarioId())
                    .append("}");
        }

        json.append("]");

        enviarJson(troca, 200, json.toString());
    }


    private static void criarCategoria(
            com.sun.net.httpserver.HttpExchange troca
    ) throws IOException {

        // Pega body da requisição, le e guarda tudo dentro de "corpo"
        String corpo = new String(
                troca.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );


        String nome = extrairCampoJsonCategoria(corpo, "nome");
        String cor = extrairCampoJsonCategoria(corpo, "cor");
        String icone = extrairCampoJsonCategoria(corpo, "icone");
        String usuarioIdTexto = extrairNumeroJsonCategoria(corpo, "usuarioId");

        if (nome == null || cor == null ||
                icone == null || usuarioIdTexto == null) {

            enviarJson(
                    troca,
                    400,
                    "{\"mensagem\":\"Informe nome, cor, icone e usuarioId\"}"
            );
            return;
        }

        nome = nome.trim();
        cor = cor.trim();
        icone = icone.trim();

        if (nome.isEmpty() || nome.length() > 100 ||
                !cor.matches("#[0-9a-fA-F]{6}") ||
                icone.isEmpty() || icone.length() > 100) {

            enviarJson(
                    troca,
                    400,
                    "{\"mensagem\":\"Dados da categoria inválidos\"}"
            );
            return;
        }

        int usuarioId;

        try {
            usuarioId = Integer.parseInt(usuarioIdTexto);

            if (usuarioId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            enviarJson(
                    troca,
                    400,
                    "{\"mensagem\":\"usuarioId inválido\"}"
            );
            return;
        }

        CategoriaRepository repository =
                new CategoriaRepository();

        if (repository.existeNomeVisivelParaUsuario(
                nome, usuarioId)) {

            enviarJson(
                    troca,
                    409,
                    "{\"mensagem\":\"Já existe uma categoria com esse nome\"}"
            );
            return;
        }

        Categoria categoria =
                new Categoria(nome, cor, icone);

        try {
            repository.salvar(categoria, usuarioId);

            String json = "{"
                    + "\"id\":" + categoria.getId() + ","
                    + "\"nome\":" + textoJson(categoria.getNome()) + ","
                    + "\"cor\":" + textoJson(categoria.getCor()) + ","
                    + "\"icone\":" + textoJson(categoria.getIcone()) + ","
                    + "\"usuarioId\":" + usuarioId
                    + "}";

            enviarJson(troca, 201, json);

        } catch (RuntimeException e) {
            System.err.println(
                    "Erro ao salvar categoria: " + e.getMessage()
            );

            enviarJson(
                    troca,
                    500,
                    "{\"mensagem\":\"Não foi possível salvar a categoria\"}"
            );
        }
    }

    private static void editarCategoria(
            com.sun.net.httpserver.HttpExchange troca
    ) throws IOException {

        String corpo = new String(
                troca.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        String categoriaIdTexto =
                extrairNumeroJsonCategoria(corpo, "categoriaId");

        String usuarioIdTexto =
                extrairNumeroJsonCategoria(corpo, "usuarioId");

        String nome =
                extrairCampoJsonCategoria(corpo, "nome");

        String cor =
                extrairCampoJsonCategoria(corpo, "cor");

        String icone =
                extrairCampoJsonCategoria(corpo, "icone");

        if (categoriaIdTexto == null ||
                usuarioIdTexto == null ||
                nome == null ||
                cor == null ||
                icone == null) {

            enviarJson(
                    troca,
                    400,
                    "{\"mensagem\":\"Dados incompletos\"}"
            );

            return;
        }

        int categoriaId;
        int usuarioId;

        try {
            categoriaId = Integer.parseInt(categoriaIdTexto);
            usuarioId = Integer.parseInt(usuarioIdTexto);

            if (categoriaId <= 0 || usuarioId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            enviarJson(
                    troca,
                    400,
                    "{\"mensagem\":\"IDs inválidos\"}"
            );

            return;
        }

        nome = nome.trim();
        cor = cor.trim();
        icone = icone.trim();

        CategoriaRepository repository =
                new CategoriaRepository();

        if (repository.existeNomeVisivelParaUsuarioExcetoId(
                nome,
                usuarioId,
                categoriaId
        )) {

            enviarJson(
                    troca,
                    409,
                    "{\"mensagem\":\"Já existe uma categoria com esse nome\"}"
            );

            return;
        }

        Categoria categoria =
                new Categoria(nome, cor, icone);

        categoria.setId(categoriaId);

        boolean atualizou =
                repository.atualizarPersonalizada(
                        categoria,
                        usuarioId
                );

        if (!atualizou) {

            enviarJson(
                    troca,
                    403,
                    "{\"mensagem\":\"Categoria não encontrada ou não pertence ao usuário\"}"
            );

            return;
        }

        String json = "{"
                + "\"id\":" + categoriaId + ","
                + "\"nome\":" + textoJson(nome) + ","
                + "\"cor\":" + textoJson(cor) + ","
                + "\"icone\":" + textoJson(icone) + ","
                + "\"usuarioId\":" + usuarioId
                + "}";

        enviarJson(
                troca,
                200,
                json
        );
    }

    private static void excluirCategoria(
            com.sun.net.httpserver.HttpExchange troca
    ) throws IOException {

        String corpo = new String(
                troca.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        String categoriaIdTexto =
                extrairNumeroJsonCategoria(
                        corpo,
                        "categoriaId"
                );

        String usuarioIdTexto =
                extrairNumeroJsonCategoria(
                        corpo,
                        "usuarioId"
                );

        if (categoriaIdTexto == null ||
                usuarioIdTexto == null) {

            enviarJson(
                    troca,
                    400,
                    "{\"mensagem\":\"Informe categoriaId e usuarioId\"}"
            );

            return;
        }

        int categoriaId;
        int usuarioId;

        try {
            categoriaId =
                    Integer.parseInt(categoriaIdTexto);

            usuarioId =
                    Integer.parseInt(usuarioIdTexto);

            if (categoriaId <= 0 || usuarioId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            enviarJson(
                    troca,
                    400,
                    "{\"mensagem\":\"IDs inválidos\"}"
            );

            return;
        }

        CategoriaRepository repository =
                new CategoriaRepository();

        boolean removeu =
                repository.removerPersonalizada(
                        categoriaId,
                        usuarioId
                );

        if (!removeu) {

            enviarJson(
                    troca,
                    403,
                    "{\"mensagem\":\"Categoria não encontrada ou não pertence ao usuário\"}"
            );

            return;
        }

        enviarJson(
                troca,
                200,
                "{\"mensagem\":\"Categoria excluída com sucesso\"}"
        );
    }

    private static String extrairCampoJsonCategoria(
            String json,
            String campo
    ) {
        String regex = "\"" + Pattern.quote(campo) + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"";

        Matcher matcher = Pattern.compile(regex).matcher(json);

        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1)
                .replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\\", "\\");
    }

    private static String extrairNumeroJsonCategoria(
            String json,
            String campo
    ) {
        String regex = "\"" + Pattern.quote(campo)
                + "\"\\s*:\\s*(\\d+)";

        Matcher matcher = Pattern.compile(regex).matcher(json);

        return matcher.find() ? matcher.group(1) : null;
    }




    private static String textoJson(String texto) {

        if (texto == null) {
            return "null";
        }

        return "\""
                + texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                + "\"";
    }


    private static void enviarJson(
            com.sun.net.httpserver.HttpExchange troca,
            int status,
            String resposta
    ) throws IOException {

        byte[] dados = resposta.getBytes(
                java.nio.charset.StandardCharsets.UTF_8
        );

        troca.getResponseHeaders().set(
                "Content-Type",
                "application/json; charset=UTF-8"
        );

        troca.sendResponseHeaders(status, dados.length);

        try (var corpo = troca.getResponseBody()) {
            corpo.write(dados);
        }
    }



}