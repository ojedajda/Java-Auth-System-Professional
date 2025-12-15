package servlets;

import dao.UsuarioDAO;
import util.EmailUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegistroServlet", urlPatterns = {"/registro"})
public class RegistroServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegistroServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegistroServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String nombre = request.getParameter("nombre");
            String correo = request.getParameter("correo");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            
            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Las contraseñas no coinciden");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
                return;
            }
            
            if (usuarioDAO.existeCorreo(correo)) {
                request.setAttribute("error", "El correo ya está registrado");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
                return;
            }
            
            boolean registrado = usuarioDAO.registrarUsuario(nombre, correo, password);
            
            if (registrado) {
                // ✅ ENVÍO DE CORREO DE NOTIFICACIÓN DE REGISTRO
                boolean correoEnviado = EmailUtil.enviarCorreoRegistro(correo, nombre);
                
                if (correoEnviado) {
                    request.setAttribute("exito", "Usuario registrado correctamente. Se ha enviado un correo de confirmación.");
                } else {
                    request.setAttribute("exito", "Usuario registrado correctamente. Error al enviar correo de confirmación.");
                }
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Error al registrar usuario");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en la base de datos: " + e.getMessage());
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet para registro de usuarios";
    }
}