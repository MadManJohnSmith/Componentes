package CompLogin;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebService(serviceName = "Login")
public class Auth {

    // Parámetros de conexión a la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/puntuapp";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    @WebMethod(operationName = "validarUsuario")
    public String validarUsuario(
            @WebParam(name = "username") String username,
            @WebParam(name = "password") String password) {

        String resultado = null;
        Connection conn = null;

        try {
            // Cargar el driver JDBC y conectar a la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Consulta SQL para validar credenciales
            String sql = "SELECT username, rol FROM usuarios WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                // Ejecutar consulta y verificar si hay resultados
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String user = rs.getString("username");
                        String rol = rs.getString("rol");
                        // Crear respuesta en formato JSON
                        resultado = "{\"username\":\"" + user + "\", \"rol\":\"" + rol + "\"}";
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // Cerrar la conexión a la base de datos
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }

        return resultado; // Devuelve el JSON o null
    }

}
