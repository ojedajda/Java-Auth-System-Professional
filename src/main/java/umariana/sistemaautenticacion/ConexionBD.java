package umariana.sistemaautenticacion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    // Cambiar según tu configuración de base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/autenticacion"; 
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    
    public static Connection getConexion() throws SQLException {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
    }
}