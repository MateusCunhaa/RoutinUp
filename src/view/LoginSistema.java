package view;

import java.util.Scanner;

import model.Usuario;
import repository.UsuarioRepository;


public class LoginSistema {

    private Scanner scan;
    private UsuarioRepository usuarioRepository;

    public LoginSistema(Scanner scan){

        this.scan = scan;
        usuarioRepository = new UsuarioRepository();

    }

    public Usuario login(){

        System.out.println("===== LOGIN =====");

        System.out.println("Gmail: ");
        String gmail = scan.nextLine();

        System.out.println("Senha: ");
        String senha = scan.nextLine();


        Usuario usuario = usuarioRepository.buscarLogin(gmail, senha);


        if (usuario == null){

            System.out.println("Usuario ou Senha incorretos");

            return null;
        }

        System.out.println("Login Realizado!");
        return usuario;
    }


}
