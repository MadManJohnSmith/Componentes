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
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "{CALL GetUsersWithAttendance()}";
            try (CallableStatement stmt = conn.prepareCall(sql);
                    ResultSet rs = stmt.executeQuery()) {

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
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }

        return "[" + String.join(",", jsonRows) + "]";
    }

    @WebMethod(operationName = "getUserDetails")
    public List<String> getUserDetails(String username) {
        List<String> userDetails = new ArrayList<>();

        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "{CALL GetUserDetailsWithLastAttendance(?)}";
            stmt = conn.prepareCall(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();

            if (rs.next()) {
                userDetails.add(rs.getString("username"));
                userDetails.add(rs.getString("name"));
                userDetails.add(rs.getString("rol"));
                userDetails.add(rs.getString("estado"));
                userDetails.add(rs.getString("lastEntryDate"));
                userDetails.add(rs.getString("lastEntryTime"));
                userDetails.add(rs.getString("lastExitDate"));
                userDetails.add(rs.getString("lastExitTime"));
                Blob fotoBlob = rs.getBlob("foto");
                if (fotoBlob != null) {
                    byte[] fotoBytes = fotoBlob.getBytes(1, (int) fotoBlob.length());
                    String fotoBase64 = java.util.Base64.getEncoder().encodeToString(fotoBytes);
                    userDetails.add(fotoBase64);
                } else {
                    userDetails.add("");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            userDetails.clear();
            userDetails.add("Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userDetails;
    }

    @WebMethod(operationName = "getUserDetailsJSON")
    public String getUserDetailsJSON(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "SELECT username, name, rol, estado, foto FROM Usuarios WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();

            if (rs.next()) {
                // Construir el objeto JSON manualmente
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("{");
                jsonBuilder.append("\"username\":\"").append(rs.getString("username")).append("\",");
                jsonBuilder.append("\"name\":\"").append(rs.getString("name")).append("\",");
                jsonBuilder.append("\"rol\":\"").append(rs.getString("rol")).append("\",");

                Blob fotoBlob = rs.getBlob("foto");
                if (fotoBlob != null) {
                    byte[] fotoBytes = fotoBlob.getBytes(1, (int) fotoBlob.length());
                    String fotoBase64 = java.util.Base64.getEncoder().encodeToString(fotoBytes);
                    jsonBuilder.append("\"foto\":\"").append(fotoBase64).append("\"");
                } else {
                    jsonBuilder.append("\"foto\":null");
                }

                jsonBuilder.append("}");
                return jsonBuilder.toString();
            } else {
                return "{}"; // Usuario no encontrado
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage() + "\"}";
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @WebMethod(operationName = "deleteUser")
    public String deleteUser(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "DELETE FROM Usuarios WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0 ? "Success" : "Error: Usuario no encontrado.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @WebMethod(operationName = "updateUser")
    public String updateUser(String username, String jsonChanges) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Construir la consulta SQL dinámicamente
            StringBuilder sql = new StringBuilder("UPDATE Usuarios SET ");
            List<String> keys = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            // Procesar los cambios desde el JSON
            String[] changes = jsonChanges.split(";");
            for (String change : changes) {
                String[] pair = change.split("=");
                if (pair.length == 2) {
                    keys.add(pair[0].trim());
                    values.add(pair[1].trim());
                }
            }

            // Validar que hay cambios para actualizar
            if (keys.isEmpty()) {
                return "Error: No se especificaron cambios para actualizar.";
            }

            // Crear el cuerpo del SQL con las claves
            for (String key : keys) {
                sql.append(key).append(" = ?, ");
            }
            sql.delete(sql.length() - 2, sql.length()); // Eliminar la última coma y espacio
            sql.append(" WHERE username = ?");

            stmt = conn.prepareStatement(sql.toString());

            // Asignar valores dinámicamente
            int index = 1;
            for (Object value : values) {
                stmt.setObject(index++, value);
            }
            stmt.setString(index, username);

            // Ejecutar la actualización
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0 ? "Success" : "Error: Usuario no encontrado.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @WebMethod(operationName = "addUser")
    public String addUser(String name, String username, String password, String rol, String fotoBase64) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "INSERT INTO Usuarios (name, username, password, rol, foto) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, rol);
            stmt.setString(5, fotoBase64);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return "Success";
            } else {
                return "Error: No se pudo agregar el usuario.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
