package servlets;

import dao.UsuarioDAO;
import modelos.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ActualizarPerfilServlet", urlPatterns = {"/docente/actualizarPerfil"})
public class ActualizarPerfilServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ActualizarPerfilServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ActualizarPerfilServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        // Verificar que esté autenticado
        if (usuario == null) {
            response.sendRedirect("../login.jsp");
            return;
        }
        
        try {
            String nombre = request.getParameter("nombre");
            String nuevaPassword = request.getParameter("nuevaPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            
            // Validar que las contraseñas coincidan si se van a cambiar
            if (nuevaPassword != null && !nuevaPassword.trim().isEmpty()) {
                if (!nuevaPassword.equals(confirmPassword)) {
                    request.setAttribute("error", "Las contraseñas no coinciden");
                    request.getRequestDispatcher("perfil.jsp").forward(request, response);
                    return;
                }
            }
            
            // Actualizar nombre en la base de datos
            boolean actualizado = actualizarUsuario(usuario.getIdUsuario(), nombre, 
                                  nuevaPassword != null && !nuevaPassword.trim().isEmpty() ? nuevaPassword : null);
            
            if (actualizado) {
                // Actualizar el usuario en sesión
                usuario.setNombre(nombre);
                session.setAttribute("usuario", usuario);
                
                request.setAttribute("exito", "Perfil actualizado correctamente");
            } else {
                request.setAttribute("error", "Error al actualizar el perfil");
            }
            
            request.getRequestDispatcher("perfil.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en la base de datos: " + e.getMessage());
            request.getRequestDispatcher("perfil.jsp").forward(request, response);
        }
    }
    
    private boolean actualizarUsuario(int idUsuario, String nombre, String nuevaPassword) throws SQLException {
        if (nuevaPassword != null && !nuevaPassword.trim().isEmpty()) {
            // Actualizar nombre y contraseña
            String sql = "UPDATE usuarios SET nombre = ?, contraseña = ? WHERE idUsuario = ?";
            try (Connection con = UsuarioDAO.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setString(2, usuarioDAO.encriptarPassword(nuevaPassword));
                ps.setInt(3, idUsuario);
                return ps.executeUpdate() > 0;
            }
        } else {
            // Actualizar solo nombre
            String sql = "UPDATE usuarios SET nombre = ? WHERE idUsuario = ?";
            try (Connection con = UsuarioDAO.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setInt(2, idUsuario);
                return ps.executeUpdate() > 0;
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet para actualizar perfil de docente";
    }
}