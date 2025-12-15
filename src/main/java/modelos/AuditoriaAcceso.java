package modelos;

import java.sql.Timestamp;

public class AuditoriaAcceso {
    private int idAuditoria;
    private int idUsuario;
    private Timestamp fechaAcceso;
    private String direccionIp;
    private Usuario usuario; // Para mostrar info del usuario
    
    // Constructores
    public AuditoriaAcceso() {}
    
    public AuditoriaAcceso(int idUsuario, String direccionIp) {
        this.idUsuario = idUsuario;
        this.direccionIp = direccionIp;
    }
    
    // Getters y Setters
    public int getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(int idAuditoria) { this.idAuditoria = idAuditoria; }
    
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    
    public Timestamp getFechaAcceso() { return fechaAcceso; }
    public void setFechaAcceso(Timestamp fechaAcceso) { this.fechaAcceso = fechaAcceso; }
    
    public String getDireccionIp() { return direccionIp; }
    public void setDireccionIp(String direccionIp) { this.direccionIp = direccionIp; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}