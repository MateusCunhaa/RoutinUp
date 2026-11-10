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



    // Metodos
    public void adicionarXP(Usuario usuario, int valor){

        int xpAtual = usuario.getXp();

        usuario.setXp(xpAtual + valor);
    }

    public void aumentarHabitosCriados(Usuario usuario){

        int quantidadeAtual = usuario.getHabitosCriados();

        usuario.setHabitosCriados(quantidadeAtual + 1);
    }
}
