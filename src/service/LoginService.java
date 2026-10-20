package service;

import dto.login.LoginRequest;
import dto.login.LoginResponse;
import dto.login.LoginResult;
import model.Usuario;
import repository.UsuarioRepository;

public class LoginService {

    // Variaveis (Atributos)
    private UsuarioRepository repository;

    // Construtor
    public LoginService() {

        repository = new UsuarioRepository();

    }

    // Metodo valida login e retorna dados da resposta
    public LoginResult fazerLogin(LoginRequest request){

        // Pega gmail e senha do POST enviado(esta nos parametros),busca pelos dados vindo do parametro dentro do banco de dados e retorna pra variavel usuario
        Usuario usuario = repository.buscarLogin(request.getGmail(), request.getSenha());

        // Se não achar usuario no banco retorna Null
        if (usuario == null){

            return new LoginResult(false,"Usuário ou senha incorretos",null);
        }

        // Dados pra resposta
        return new LoginResult(true,"Login realizado", new LoginResponse(usuario.getNome(), usuario.getXp(), usuario.getNivel()));
    }


}
