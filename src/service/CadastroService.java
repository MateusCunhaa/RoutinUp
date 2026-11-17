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

    // Metodos
    public CadastroResult cadastrar(CadastroRequest request) {

        // Pega o campo gmail da requisicao e confere se o Gmail existe no banco de dados
        if (repository.existeGmail(request.getGmail())) {

            // Retorna resultado (Comunicacao)
            return new CadastroResult(
                    false,
                    "Gmail já cadastrado"
            );

        }

        // Pega o campo nome da requisicao e confere se o Gmail existe no banco de dados
        if (repository.existeNome(request.getNome())) {

            // Retorna resultado (Comunicacao)
            return new CadastroResult(
                    false,
                    "Nome já utilizado"
            );

        }

        // Cria o usuário com dados vindo da requisicao
        Usuario usuario = new Usuario(
                request.getNome(),
                request.getGmail(),
                request.getSenha()
        );

        // Salva no banco
        repository.salvar(usuario);

        // Retorna resultado (comunicacao)
        return new CadastroResult(
                true,
                "Cadastro realizado com sucesso"
        );

    }

}