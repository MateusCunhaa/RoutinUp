package view;

import java.util.Scanner;

import model.Usuario;
import repository.UsuarioRepository;

public class CadastroSistema {

    private Scanner scan;
    private UsuarioRepository usuarioRepository;

    public CadastroSistema(Scanner scan) {

        this.scan = scan;
        usuarioRepository = new UsuarioRepository();

    }

    private boolean senhaValida(String senha){

        if (senha.length() < 8){

            return false;
        }

        boolean temLetra = false;
        boolean temNumero = false;

        for (char c : senha.toCharArray()){

            if (Character.isLetter(c)){

                temLetra = true;

            }

            if (Character.isDigit(c)){

                temNumero = true;
            }
        }

        return temLetra && temNumero;

    }

    public void cadastrar(){

        System.out.println("===== CADASTRO =====");

        System.out.println("Nome");
        String nome = scan.nextLine();

        if (usuarioRepository.existeNome(nome)){

            System.out.println("Esse nome já existe!");
            return;
        }

        System.out.println("Gmail");
        String gmail = scan.nextLine();

        if (usuarioRepository.existeGmail(gmail)){

            System.out.println("Esse gmail já esta sendo usado!");
            return;
        }

        System.out.println("Senha");
        String senha = scan.nextLine();

        if (!senhaValida(senha)){

            System.out.println("Senha inválida! Deve possuir no mínimo 8 caracteres e possuir letras e números.");
            return;
        }

        Usuario usuario = new Usuario(
                nome,
                gmail,
                senha
        );

        usuarioRepository.salvar(usuario);

        System.out.println("Conta criada com sucesso!");

    }




}
