package service;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessaoService {

    // Armazena o token da sessão e o ID verdadeiro do usuario (devo ter que colocar sessão no banco )
    private static final Map<String, Integer> sessoes = new ConcurrentHashMap<>();

    // Cria sessão apos login
    public static String criarSessao(int usuarioId) {

        String token = UUID.randomUUID().toString();

        sessoes.put(token, usuarioId);

        return token;
    }

    // Identifica usuario atraves da sessão
    public static Integer buscarUsuarioId(String token) {

        if (token == null || token.isBlank()) {

            return null;
        }

        return sessoes.get(token);
    }

    // Encerrar sessão
    public static void encerrarSessao(String token) {

        if (token != null) {

            sessoes.remove(token);
        }
    }



}
