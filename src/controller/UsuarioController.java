package controller;

import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

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
            String gmail = corpo.split("\"gmail\":\"")[1].split("\"")[0];
            String senha = corpo.split("\"senha\":\"")[1].split("\"")[0];

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
                 "nome":"%s",
                 "xp":%d,
                 "nivel":%d
                 }        
                         """.formatted(resultado.getMensagem(), usuarioResposta.getNome(), usuarioResposta.getXp(),usuarioResposta.getNivel());

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
            String nome = corpo
                    .split("\"nome\"\\s*:\\s*\"")[1]
                    .split("\"")[0];

            String gmail = corpo
                    .split("\"gmail\"\\s*:\\s*\"")[1]
                    .split("\"")[0];

            String senha = corpo
                    .split("\"senha\"\\s*:\\s*\"")[1]
                    .split("\"")[0];


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

        // liga o servidor
        servidor.start();

    }

}