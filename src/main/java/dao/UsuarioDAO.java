package dao;

import umariana.sistemaautenticacion.ConexionBD;
import modelos.Usuario;
import modelos.Rol;
import modelos.AuditoriaAcceso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsuarioDAO {
    
    /**
     * Método para encriptar contraseña con SHA-256
     */
    public String encriptarPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Método para obtener conexión (NUEVO MÉTODO AGREGADO)
     */
    public static Connection getConexion() throws SQLException {
        return ConexionBD.getConexion();
    }
    
    /**
     * Método para registrar nuevo usuario
     */
    public boolean registrarUsuario(String nombre, String correo, String password) throws SQLException {
        boolean registrado = false;
        String sql = "INSERT INTO usuarios (nombre, correo, contraseña) VALUES (?, ?, ?)";
        
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, encriptarPassword(password));
            
            int filas = ps.executeUpdate();
            registrado = (filas > 0);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrado;
    }
    
    /**
     * Método para autenticar usuario
     */
    public Usuario autenticarUsuario(String correo, String password) throws SQLException {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND contraseña = ?";
        
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, correo);
            ps.setString(2, encriptarPassword(password));
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContraseña(rs.getString("contraseña"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                
                // Obtener roles del usuario
                usuario.setRoles(obtenerRolesUsuario(usuario.getIdUsuario()));
            }
        }
        return usuario;
    }
    
    /**
     * Método para obtener roles de un usuario
     */
    public List<Rol> obtenerRolesUsuario(int idUsuario) throws SQLException {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT r.* FROM roles r " +
                    "INNER JOIN usuariosroles ur ON r.idRol = ur.idRol " +
                    "WHERE ur.idUsuario = ?";
        
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("idRol"));
                rol.setRol(rs.getString("rol"));
                roles.add(rol);
            }
        }
        return roles;
    }
    
    /**
     * Método para verificar si el correo ya existe
     */
    public boolean existeCorreo(String correo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
        
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    /**
     * Método para registrar acceso en auditoría
     */
    public void registrarAcceso(int idUsuario, String direccionIp) throws SQLException {
        String sql = "INSERT INTO auditoria_accesos (idUsuario, direccion_ip) VALUES (?, ?)";
        
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ps.setString(2, direccionIp);
            ps.executeUpdate();
        }
    }
    
    /**
     * Método para obtener todos los usuarios (solo para administradores)
     */
    public List<Usuario> obtenerTodosUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                usuario.setRoles(obtenerRolesUsuario(usuario.getIdUsuario()));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }
    
    /**
     * MÉTODO NUEVO: Obtener usuario por ID
     */
    public Usuario obtenerUsuarioPorId(int idUsuario) throws SQLException {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE idUsuario = ?";
        
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContraseña(rs.getString("contraseña"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                usuario.setRoles(obtenerRolesUsuario(usuario.getIdUsuario()));
            }
        }
        return usuario;
    }
}