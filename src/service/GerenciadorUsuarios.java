package service;

import model.Usuario;
import repository.UsuarioRepository;

public class GerenciadorUsuarios {

    // Variaveis (Atributos)
    private UsuarioRepository usuarioRepository;

    // Construtor
    public GerenciadorUsuarios() {

        usuarioRepository = new UsuarioRepository();
    }

}




