package servlets;

import dao.UsuarioDAO;
import modelos.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "GestionUsuariosServlet", urlPatterns = {"/admin/usuarios"})
public class GestionUsuariosServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GestionUsuariosServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GestionUsuariosServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        // Verificar que sea administrador
        if (usuario == null || !tieneRol(usuario, "Administrador")) {
            response.sendError(403, "Acceso denegado");
            return;
        }
        
        try {
            List<Usuario> usuarios = usuarioDAO.obtenerTodosUsuarios();
            request.setAttribute("usuarios", usuarios);
            request.getRequestDispatcher("/admin/gestion-usuarios.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al obtener usuarios: " + e.getMessage());
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        }
    }
    
    private boolean tieneRol(Usuario usuario, String nombreRol) {
        if (usuario.getRoles() == null) return false;
        return usuario.getRoles().stream()
                .anyMatch(rol -> rol.getRol().equals(nombreRol));
    }

    @Override
    public String getServletInfo() {
        return "Servlet para gestión de usuarios (Admin)";
    }
}