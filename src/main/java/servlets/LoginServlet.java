package servlets;

import dao.UsuarioDAO;
import modelos.Usuario;
import util.EmailUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        
        // Validaciones básicas
        if (correo == null || correo.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Correo y contraseña son obligatorios");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        try {
            Usuario usuario = usuarioDAO.autenticarUsuario(correo, password);
            
            if (usuario != null) {
                // Crear sesión
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("usuarioNombre", usuario.getNombre());
                session.setAttribute("usuarioCorreo", usuario.getCorreo());
                
                // Registrar acceso en auditoría
                String ipAddress = request.getRemoteAddr();
                usuarioDAO.registrarAcceso(usuario.getIdUsuario(), ipAddress);
                
                // ✅ ENVÍO DE CORREO DE NOTIFICACIÓN DE ACCESO (en segundo plano)
                new Thread(() -> {
                    enviarNotificacionAcceso(usuario, ipAddress);
                }).start();
                
                // Redirigir según el rol
                String redirectPage = determinarPaginaRedireccion(usuario);
                response.sendRedirect(redirectPage);
                
            } else {
                request.setAttribute("error", "Correo o contraseña incorrectos");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en la base de datos: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
    /**
     * Determina la página de redirección según los roles del usuario
     */
    private String determinarPaginaRedireccion(Usuario usuario) {
        if (tieneRol(usuario, "Administrador")) {
            return "admin/dashboard.jsp";
        } else if (tieneRol(usuario, "Coordinador")) {
            return "coordinador/dashboard.jsp";
        } else if (tieneRol(usuario, "Docente")) {
            return "docente/perfil.jsp";
        } else {
            return "dashboard.jsp";
        }
    }
    
    /**
     * Verifica si el usuario tiene un rol específico
     */
    private boolean tieneRol(Usuario usuario, String nombreRol) {
        if (usuario.getRoles() == null) {
            return false;
        }
        return usuario.getRoles().stream()
                .anyMatch(rol -> rol != null && rol.getRol() != null && 
                                rol.getRol().equalsIgnoreCase(nombreRol));
    }
    
    /**
     * Método para enviar notificación de acceso por correo
     * Se ejecuta en segundo plano para no bloquear el login
     */
    private void enviarNotificacionAcceso(Usuario usuario, String ipAddress) {
        try {
            System.out.println("📧 Intentando enviar notificación de acceso a: " + usuario.getCorreo());
            
            boolean correoEnviado = EmailUtil.enviarCorreoAcceso(
                usuario.getCorreo(), 
                usuario.getNombre(), 
                ipAddress
            );
            
            if (correoEnviado) {
                System.out.println("✅ Notificación de acceso enviada exitosamente a: " + usuario.getCorreo());
            } else {
                System.out.println("⚠️ No se pudo enviar notificación de acceso a: " + usuario.getCorreo());
            }
            
        } catch (Exception e) {
            // No interrumpir el flujo de login si falla el correo
            System.err.println("❌ Error enviando notificación de acceso: " + e.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet para autenticación de usuarios con notificaciones por correo";
    }
}