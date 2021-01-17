package bdregistros;

import java.util.ArrayList;

public class ListaUsuarios{
    private ArrayList<Usuario> lista_usuarios; 

    public ListaUsuarios(){
        this.lista_usuarios = new ArrayList<Usuario>();
    }
    
    //Se agrega un usuario a la lista de usuarios
    public void EnlistarUsuario(Usuario usuario){
        this.lista_usuarios.add(usuario);
    }
    
    //Retorna la lista de usuarios
    public ArrayList<Usuario> getListaUsuarios(){
        return this.lista_usuarios;
    }
}