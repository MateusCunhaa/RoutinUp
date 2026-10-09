package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import repository.UsuarioRepository;
import model.Usuario;
import dto.LoginRequest;
import dto.LoginResponse;


public class UsuarioController {

    // criou porta no servidor
    public static void iniciarServidor() throws IOException {

        HttpServer servidor = HttpServer.create(
                new InetSocketAddress(8080),
                0
        );


        // Criação do endpoint
        servidor.createContext("/login", troca -> {


            // LER JSON QUE VEM DO FLUTTER
            BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(troca.getRequestBody())
            );


            // String corpo fica com os dados que foram recebidos
            String corpo = leitor.lines()
                    .reduce("", (a, b) -> a + b);

            // print nos dados que chegaram
            System.out.println(corpo);

            // pegar apenas os dados corretos limpos
            String gmail = corpo.split("\"gmail\":\"")[1].split("\"")[0];
            String senha = corpo.split("\"senha\":\"")[1].split("\"")[0];

            // Cria objeto e armazena nele gmail e senha recebidos pelo metodo post
             LoginRequest loginRequest = new LoginRequest(gmail,senha);

             // Crinado objeto que se comunica com o banco de dados
            UsuarioRepository repository = new UsuarioRepository();

            // Pega gmail e senha do POST enviado(esta nos parametros),busca pelos dados vindo do parametro dentro do banco de dados e retorna pra variavel usuario
             Usuario usuario = repository.buscarLogin(loginRequest.getGmail(), loginRequest.getSenha());

             String resposta;

             // Confere se o usuario existe e constroi a resposta se existir
             if (usuario != null){

                 System.out.println("Usuario encontrado!");
                 LoginResponse respostaUsuario  = new LoginResponse(usuario.getNome(), usuario.getXp(), usuario.getNivel());
                  resposta = """
                    {
                        "nome":"%s",
                        "xp":%d,
                        "nivel":%d
                    }
                    """.formatted(respostaUsuario.getNome(),respostaUsuario.getXp(),respostaUsuario.getNivel());

             }else {

                 System.out.println("Usuario não encontrado");
                 resposta = """
                         {
                            "erro":"Usuario não encontrado"
                         }   
                         """;
             }

            // Pega e diciona o cabeçalho da resposta que vou enviar
            troca.getResponseHeaders().add(
                    "Content-Type",
                    "application/json"
            );

            // Manda o cabeçalho da resposta para quem fez a solicitação
            troca.sendResponseHeaders(
                    200,
                    resposta.getBytes().length
            );

            // Manda o body da resposta para o solicitante
            troca.getResponseBody().write(
                    resposta.getBytes()
            );


            troca.close();

        });

        // liga o servidor
        servidor.start();

    }

}