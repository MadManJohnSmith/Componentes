package CompLogin;

import javax.jws.WebService;
import javax.jws.WebMethod;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebService(serviceName = "UserService")
public class UserService {
    private static final String URL = "jdbc:mysql://localhost:3306/puntuapp";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    @WebMethod(operationName = "getUsersWithAttendance")
    public String getUsersWithAttendance() {
        List<String> jsonRows = new ArrayList<>();
        Connection conn = null;

        try {
            // Conexión a la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Llamar al procedimiento almacenado
            String sql = "{CALL GetUsersWithAttendance()}";
            try (CallableStatement stmt = conn.prepareCall(sql);
                 ResultSet rs = stmt.executeQuery()) {

                // Construir el JSON manualmente
                while (rs.next()) {
                    StringBuilder jsonBuilder = new StringBuilder();
                    jsonBuilder.append("{")
                               .append("\"username\":\"").append(rs.getString("username")).append("\",")
                               .append("\"name\":\"").append(rs.getString("name")).append("\",")
                               .append("\"rol\":\"").append(rs.getString("rol")).append("\",")
                               .append("\"estado\":\"").append(rs.getString("estado")).append("\",")
                               .append("\"lastEntryDate\":\"").append(rs.getString("lastEntryDate") == null ? "" : rs.getString("lastEntryDate")).append("\",")
                               .append("\"lastEntryTime\":\"").append(rs.getString("lastEntryTime") == null ? "" : rs.getString("lastEntryTime")).append("\",")
                               .append("\"lastExitDate\":\"").append(rs.getString("lastExitDate") == null ? "" : rs.getString("lastExitDate")).append("\",")
                               .append("\"lastExitTime\":\"").append(rs.getString("lastExitTime") == null ? "" : rs.getString("lastExitTime")).append("\"")
                               .append("}");
                    jsonRows.add(jsonBuilder.toString());
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // Cerrar la conexión
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }

        // Unir todos los objetos JSON en un array JSON
        return "[" + String.join(",", jsonRows) + "]";
    }
}

