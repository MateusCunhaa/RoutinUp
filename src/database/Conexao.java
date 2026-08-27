package database;

import model.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL =  "jdbc:postgresql://localhost:5432/routinup";

    private static final String USUARIO = "postgres";

    private static final String SENHA = "4180";

    public static Connection conectar(){

        try {

            return DriverManager.getConnection(URL, USUARIO, SENHA);

        }catch(SQLException e){

            throw new RuntimeException(e);
        }

    }



}
