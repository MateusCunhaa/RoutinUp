// Cria conexão entre o java e o postgres

package database;

import model.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    // Dados/Atributos pra se conectar ao postgres
    private static final String URL =  "jdbc:postgresql://localhost:5432/routinup";

    private static final String USUARIO = "postgres";

    private static final String SENHA = "4180";


    // Cria conexão entre o java e o postgres
    public static Connection conectar(){

        try {

            // Utiliza drivers JDBC e atributos de conexao pra estabelecer conexao com banco de dados
            return DriverManager.getConnection(URL, USUARIO, SENHA);

        // Pega excessao
        }catch(SQLException e){

            // Comunica excessao
            throw new RuntimeException(e);
        }

    }



}
