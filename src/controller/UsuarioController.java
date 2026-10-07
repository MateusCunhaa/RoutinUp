package controller;

import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;


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


        });

        // liga o servidor
        servidor.start();

    }

}