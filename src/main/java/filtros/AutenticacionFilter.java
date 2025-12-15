package filtros;

import modelos.Usuario;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AutenticacionFilter", 
          urlPatterns = {"/admin/*", "/coordinador/*", "/docente/*", "/dashboard.jsp"})
public class AutenticacionFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización del filtro
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        // Verificar si el usuario está autenticado
        if (session == null || session.getAttribute("usuario") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String requestURI = httpRequest.getRequestURI();
        
        // Verificar permisos según la ruta solicitada
        if (requestURI.contains("/admin/") && !tieneRol(usuario, "Administrador")) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "No tiene permisos para acceder a esta sección");
            return;
        }
        
        if (requestURI.contains("/coordinador/") && !tieneRol(usuario, "Coordinador")) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "No tiene permisos para acceder a esta sección");
            return;
        }
        
        if (requestURI.contains("/docente/") && !tieneRol(usuario, "Docente")) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "No tiene permisos para acceder a esta sección");
            return;
        }
        
        chain.doFilter(request, response);
    }
    
    private boolean tieneRol(Usuario usuario, String nombreRol) {
        if (usuario.getRoles() == null) return false;
        return usuario.getRoles().stream()
                .anyMatch(rol -> rol.getRol().equals(nombreRol));
    }
    
    @Override
    public void destroy() {
        // Cleanup resources
    }
}