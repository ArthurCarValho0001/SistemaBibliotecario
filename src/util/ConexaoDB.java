package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

    private static Connection instancia;

    public static Connection getConexao() throws SQLException {

        if (instancia == null || instancia.isClosed()) {
            instancia = DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
        }

        return instancia;
    }
}