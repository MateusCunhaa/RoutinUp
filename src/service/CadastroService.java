package service;

import dto.cadastro.CadastroRequest;
import dto.cadastro.CadastroResult;
import model.Usuario;
import repository.UsuarioRepository;

public class CadastroService {

    // Variaveis (Atributos)
    private UsuarioRepository repository;

    // Construtor
    public CadastroService() {

        repository = new UsuarioRepository();

    }

    // Metodo
    public CadastroResult cadastrar(CadastroRequest request) {

        // Confere se o Gmail já existe
        if (repository.existeGmail(request.getGmail())) {

            return new CadastroResult(
                    false,
                    "Gmail já cadastrado"
            );

        }

        // Confere se o nome já existe
        if (repository.existeNome(request.getNome())) {

            return new CadastroResult(
                    false,
                    "Nome já utilizado"
            );

        }

        // Cria o usuário
        Usuario usuario = new Usuario(
                request.getNome(),
                request.getGmail(),
                request.getSenha()
        );

        // Salva no banco
        repository.salvar(usuario);

        return new CadastroResult(
                true,
                "Cadastro realizado com sucesso"
        );

    }

}