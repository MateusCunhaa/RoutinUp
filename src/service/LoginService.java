// Realiza o login com dados vindo de LoginRequest e retorna resposta vindo de LoginResul

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

    // Metodos
    public LoginResult fazerLogin(LoginRequest request){

        // Pega dados da requisição e repository consulta no banco, se achar coloca dados do banco no usuario
        Usuario usuario = repository.buscarLogin(request.getGmail(), request.getSenha());

        // Se usuario for nul
        if (usuario == null){

            // Retorna resultado (Comunicacao)
            return new LoginResult(false,"Usuário ou senha incorretos",null);
        }

        // Retorna resultado (Comunicacao)
        return new LoginResult(true,"Login realizado", new LoginResponse(usuario.getId(),usuario.getNome(), usuario.getXp(), usuario.getNivel()));
    }


}
