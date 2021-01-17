package bdregistros;

public class Usuario{
    
    private String id_usuario;
    private String id_rol;
    private String nombre;
    private String activo;
    
    public Usuario(String id_usuario, String id_rol, String nombre, String activo){
        this.id_usuario = id_usuario;
        this.id_rol = id_rol;
        this.nombre = nombre;
        this.activo = activo;
    }
    
    public String getIdUsuario(){
        return this.id_usuario;
    }
    
    public String getIdRol(){
        return this.id_rol;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public String getActivo(){
        return this.activo;
    }
}