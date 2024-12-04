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
}
