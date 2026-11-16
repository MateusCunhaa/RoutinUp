package service;

import model.Categoria;
import model.Etapa;
import repository.EtapaRepository;

public class GerenciadorEtapas {

    // Variaveis (Atributos)
    private EtapaRepository etapaRepository;

    // Construtor
    public GerenciadorEtapas() {

        etapaRepository = new EtapaRepository();
    }


    // Metodos

    public boolean validarPeso(double peso) {

        return peso >= 0 && peso <= 100;
    }

    public void alterarPeso(Etapa etapa, double peso) {

        if (validarPeso(peso)) {

            etapa.setPeso(peso);
        }else {

            System.out.println("Peso invalido. Deve estar entre 0 e 100");
        }
    }
}
