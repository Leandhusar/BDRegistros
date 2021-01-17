package bdregistros;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion{
    private Connection con;
    private Statement st;
    private ResultSet rs;
    
    public Conexion(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/registros?serverTimezone=UTC", "root", "");
            st = con.createStatement();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    //Obtiene todos los datos de los usuarios
    public void getDatos() throws SQLException{
        String query = "select * from usuario";
        try {
            rs = st.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(rs.next()){
            String id_usuario = rs.getString("id_usuario");
            String id_rol = rs.getString("id_rol");
            String nombre = rs.getString("nombre");
            String activo = rs.getString("activo");
            System.out.println(id_usuario + " " + id_rol + " " + nombre + " " + activo);
        }
        con.close();
    }
    
    //Metodo para verificar si existen usuarios registrados con el nombre_consulta en la BBDD
    public boolean existeNombre(String nombre_consulta) throws SQLException{
        String nombre = "";
        String datos_disponibles = "select * from usuario where nombre = '" + nombre_consulta + "'";
        rs = st.executeQuery(datos_disponibles);
        
        while(rs.next()){
            nombre = rs.getString("nombre");
        }
        
        con.close();
        return nombre.length() > 0;
    }
    
    //Se retorna el id_usuario de la tabla segun un nombre entregado
    public String obtenerIdUsuarioBD(String nombre) throws SQLException{
        String id = "";
        String query = "select * from usuario where nombre = '" + nombre + "'";
        rs = st.executeQuery(query);
        while(rs.next()){
            id = rs.getString("id_usuario");
        }
        return id;
    }
    
    //Se modifican los datos de un usuario a partir de su codigo de usuario.
    //Deben insertarse los dferentes campos presentes
    public void editarUsuario(String id_usuario, String id_rol, String nombre, String activo) throws SQLException{
        String query_edicion = "update usuario set id_rol = " + id_rol + ", nombre = '" + nombre + "', activo = '" + activo + 
                               "' where id_usuario = " + id_usuario;
        st.executeUpdate(query_edicion);
        con.close();
    }
    
    //Agrega un usuario nuevo con un nombre, rol y actividad dados
    public void agregarUsuario(String id_rol, String nombre, String activo) throws SQLException{
        String query = "insert into usuario (id_rol, nombre, activo) values (" + id_rol + ", '" + nombre + "', '" + activo + "')";
        st.executeUpdate(query);
        con.close();
    }
    
    //Borra un usuario a partir de su nombre de usuario
    public void borrarUsuario(String nombre) throws SQLException{
        String query = "delete from usuario where nombre = '" + nombre + "'";
        st.executeUpdate(query);
        con.close();
    }
    
    //Busca usuarios segun el nombre de usuario dado. Si no encuentra coincidencias, retorna la lista vacia
    //Si encuentra un usuario, devuelve el usuario. Si el campo es vacio, retorna todos los
    //usuarios de la base de datos
    public ListaUsuarios buscarUsuarioPorNombre(String nombre) throws SQLException{
        ListaUsuarios lista_usuarios = new ListaUsuarios();
        String query = "select * from usuario where nombre = '" + nombre + "'";
        
        if(nombre.equals("")){
            rs = st.executeQuery("select * from usuario");
        }
        else{
            rs = st.executeQuery(query);
        }
        
        while(rs.next()){
            Usuario usuario = new Usuario(
                    rs.getString("id_usuario"),
                    rs.getString("id_rol"),
                    rs.getString("nombre"),
                    rs.getString("activo")
            );
            lista_usuarios.EnlistarUsuario(usuario);
        }
        
        con.close();
        return lista_usuarios;
    }
}