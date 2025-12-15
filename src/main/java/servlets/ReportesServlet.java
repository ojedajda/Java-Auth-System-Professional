package servlets;

import dao.UsuarioDAO;
import modelos.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ReportesServlet", urlPatterns = {"/admin/reportes", "/coordinador/reportes"})
public class ReportesServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ReportesServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReportesServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        // Verificar permisos
        boolean esAdmin = tieneRol(usuario, "Administrador");
        boolean esCoordinador = tieneRol(usuario, "Coordinador");
        
        if (usuario == null || (!esAdmin && !esCoordinador)) {
            response.sendError(403, "Acceso denegado");
            return;
        }
        
        try {
            // Obtener estadísticas para los reportes
            Map<String, Object> estadisticas = obtenerEstadisticas();
            request.setAttribute("estadisticas", estadisticas);
            request.setAttribute("esAdmin", esAdmin);
            
            if (esAdmin) {
                request.getRequestDispatcher("/admin/reportes.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/coordinador/reportes.jsp").forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al generar reportes: " + e.getMessage());
            if (esAdmin) {
                request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/coordinador/dashboard.jsp").forward(request, response);
            }
        }
    }
    
    private Map<String, Object> obtenerEstadisticas() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        
        // Total de usuarios
        String sqlUsuarios = "SELECT COUNT(*) as total FROM usuarios";
        try (Connection con = UsuarioDAO.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlUsuarios);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.put("totalUsuarios", rs.getInt("total"));
            }
        }
        
        // Usuarios por rol
        String sqlRoles = "SELECT r.rol, COUNT(ur.idUsuario) as cantidad " +
                         "FROM roles r LEFT JOIN usuariosroles ur ON r.idRol = ur.idRol " +
                         "GROUP BY r.idRol, r.rol";
        List<Map<String, Object>> usuariosPorRol = new ArrayList<>();
        try (Connection con = UsuarioDAO.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlRoles);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> rolStat = new HashMap<>();
                rolStat.put("rol", rs.getString("rol"));
                rolStat.put("cantidad", rs.getInt("cantidad"));
                usuariosPorRol.add(rolStat);
            }
        }
        stats.put("usuariosPorRol", usuariosPorRol);
        
        // Accesos por día (últimos 7 días)
        String sqlAccesos = "SELECT DATE(fecha_acceso) as fecha, COUNT(*) as accesos " +
                           "FROM auditoria_accesos " +
                           "WHERE fecha_acceso >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                           "GROUP BY DATE(fecha_acceso) " +
                           "ORDER BY fecha";
        List<Map<String, Object>> accesosPorDia = new ArrayList<>();
        try (Connection con = UsuarioDAO.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlAccesos);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> accesoStat = new HashMap<>();
                accesoStat.put("fecha", rs.getDate("fecha"));
                accesoStat.put("accesos", rs.getInt("accesos"));
                accesosPorDia.add(accesoStat);
            }
        }
        stats.put("accesosPorDia", accesosPorDia);
        
        // Usuarios activos (con al menos un acceso en los últimos 30 días)
        String sqlActivos = "SELECT COUNT(DISTINCT idUsuario) as activos " +
                           "FROM auditoria_accesos " +
                           "WHERE fecha_acceso >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
        try (Connection con = UsuarioDAO.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlActivos);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.put("usuariosActivos", rs.getInt("activos"));
            }
        }
        
        return stats;
    }
    
    private boolean tieneRol(Usuario usuario, String nombreRol) {
        if (usuario == null || usuario.getRoles() == null) return false;
        return usuario.getRoles().stream()
                .anyMatch(rol -> rol.getRol().equals(nombreRol));
    }

    @Override
    public String getServletInfo() {
        return "Servlet para generación de reportes";
    }
}