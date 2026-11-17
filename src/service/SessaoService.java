package service;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessaoService {

    // Declara variavel que referencia objetos com chave (sessao) String e valor associado Integer (ID do usuario),Recebe objeto ConcurrentHashMap (acesso simultâneo com segurança)
    private static final Map<String, Integer> sessoes = new ConcurrentHashMap<>();


    public static String criarSessao(int usuarioId) {

        // Gera um identificador UUID aleatório, converte-o para String e guarda na variavel
        String token = UUID.randomUUID().toString();

        // Adiciona associacao entre identificador UUID e ID do usuario
        sessoes.put(token, usuarioId);

        // Retorna token
        return token;
    }

    public static Integer buscarUsuarioId(String token) {

        // Se token for null ou estiver em branco
        if (token == null || token.isBlank()) {

            // Retorna null
            return null;
        }

        // Retorna token
        return sessoes.get(token);
    }


    public static void encerrarSessao(String token) {

        // Se token não for null
        if (token != null) {

            // Remove token
            sessoes.remove(token);
        }
    }



}
