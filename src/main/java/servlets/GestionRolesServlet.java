package servlets;

import dao.UsuarioDAO;
import modelos.Usuario;
import modelos.Rol;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "GestionRolesServlet", urlPatterns = {"/admin/gestionRoles"})
public class GestionRolesServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GestionRolesServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GestionRolesServlet at " + request.getContextPath() + "</h1>");
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
            String idUsuarioParam = request.getParameter("idUsuario");
            
            if (idUsuarioParam != null) {
                // Mostrar gestión de roles para un usuario específico
                int idUsuario = Integer.parseInt(idUsuarioParam);
                Usuario usuarioEditar = usuarioDAO.obtenerUsuarioPorId(idUsuario);
                List<Rol> todosRoles = obtenerTodosRoles();
                
                request.setAttribute("usuarioEditar", usuarioEditar);
                request.setAttribute("todosRoles", todosRoles);
                request.getRequestDispatcher("/admin/gestion-roles.jsp").forward(request, response);
            } else {
                // Mostrar lista de usuarios para gestionar roles
                List<Usuario> usuarios = usuarioDAO.obtenerTodosUsuarios();
                request.setAttribute("usuarios", usuarios);
                request.getRequestDispatcher("/admin/lista-gestion-roles.jsp").forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en la base de datos: " + e.getMessage());
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario == null || !tieneRol(usuario, "Administrador")) {
            response.sendError(403, "Acceso denegado");
            return;
        }
        
        try {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String[] rolesSeleccionados = request.getParameterValues("roles");
            
            // Actualizar roles del usuario
            boolean actualizado = actualizarRolesUsuario(idUsuario, rolesSeleccionados);
            
            if (actualizado) {
                request.setAttribute("exito", "Roles actualizados correctamente");
            } else {
                request.setAttribute("error", "Error al actualizar roles");
            }
            
            // Redirigir a la lista de gestión de roles
            response.sendRedirect("gestionRoles");
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en la base de datos: " + e.getMessage());
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        }
    }
    
    private List<Rol> obtenerTodosRoles() throws SQLException {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles";
        
        try (Connection con = UsuarioDAO.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("idRol"));
                rol.setRol(rs.getString("rol"));
                roles.add(rol);
            }
        }
        return roles;
    }
    
    private boolean actualizarRolesUsuario(int idUsuario, String[] rolesSeleccionados) throws SQLException {
        // Eliminar roles actuales
        String sqlEliminar = "DELETE FROM usuariosroles WHERE idUsuario = ?";
        try (Connection con = UsuarioDAO.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlEliminar)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
        
        // Insertar nuevos roles
        if (rolesSeleccionados != null && rolesSeleccionados.length > 0) {
            String sqlInsertar = "INSERT INTO usuariosroles (idUsuario, idRol) VALUES (?, ?)";
            try (Connection con = UsuarioDAO.getConexion();
                 PreparedStatement ps = con.prepareStatement(sqlInsertar)) {
                
                for (String idRolStr : rolesSeleccionados) {
                    int idRol = Integer.parseInt(idRolStr);
                    ps.setInt(1, idUsuario);
                    ps.setInt(2, idRol);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
        return true;
    }
    
    private boolean tieneRol(Usuario usuario, String nombreRol) {
        if (usuario.getRoles() == null) return false;
        return usuario.getRoles().stream()
                .anyMatch(rol -> rol.getRol().equals(nombreRol));
    }

    @Override
    public String getServletInfo() {
        return "Servlet para gestión de roles";
    }
}