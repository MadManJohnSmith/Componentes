package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthLogin extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String respuesta;
            boolean isVal =validarUsuario(username, password);;
            
            if(isVal){
                respuesta = "¡Bienvenido al Sistema";            }
            else{
                respuesta = "Error User/Password";
            }
            
            out.println("<!DOCTYPE html>");
        out.println("<html lang='es'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>PuntuApp - Inicio de Sesión</title>");
        out.println("<link rel='stylesheet' href='https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0&icon_names=account_circle'>");
        out.println("<link rel='stylesheet' href='https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0&icon_names=key'>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f8ff; margin: 0; }");
        out.println(".container { width: 350px; padding: 20px; background-color: #fff; border-radius: 10px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); text-align: center; }");
        out.println("h1 { font-size: 24px; margin-bottom: 20px; color: #007bff; }");
        out.println(".success { color: #2e7d32; font-weight: bold; }");
        out.println(".error { color: #d32f2f; font-weight: bold; }");
        out.println("input, button { width: 100%; padding: 10px; margin: 10px 0; border: 1px solid #ccc; border-radius: 5px; font-size: 16px; }");
        out.println("button { background-color: #007bff; color: white; cursor: pointer; border: none; }");
        out.println("button:hover { background-color: #0056b3; }");
        out.println(".material-symbols-outlined { font-size: 24px; margin-right: 8px; vertical-align: middle; }");
        out.println("a { display: inline-block; width: 50%; padding: 10px; margin: 10px 0; text-align: center; text-decoration: none; color: white; background-color: #007bff; border-radius: 5px; font-size: 16px; }");
        out.println("a:hover { background-color: #0056b3; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>PuntuApp</h1>");
        out.println("<h2 class='" + (isVal ? "success" : "error") + "'>");
        out.println(respuesta);
        out.println("</h2>");
        out.println("<a href='http://localhost:8080/CteCompLogin/'>Volver</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
            
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private static boolean validarUsuario(java.lang.String username, java.lang.String password) {
        complogin.Login service = new complogin.Login();
        complogin.Auth port = service.getAuthPort();
        return port.validarUsuario(username, password);
    }
}
