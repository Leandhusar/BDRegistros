package bdregistros;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Ventana extends JFrame{
    public Ventana(){
        //Informacion a mostrar en la tabla
        super("Tabla de usuarios");
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Rol");
        modelo.addColumn("Activo");
        
        //Insercion de datos en la tabla inicia con un modelo vacio que se actualiza despues de presionar el boton de busqueda
        //o sus subsecuentes
        JTable tabla_usuarios = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla_usuarios);
        scroll.setBounds(20, 140, 440, 300);
        
        JButton limpiar_boton = new JButton("Limpiar");
        limpiar_boton.setBounds(20, 20, 80, 20);
        
        JButton buscar_boton = new JButton("Buscar");
        buscar_boton.setBounds(120, 20, 80, 20);
        
        JTextField campo_nombre = new JTextField("Buscar usuario");
        campo_nombre.setBounds(20, 60, 240, 20);
        
        JButton crear_boton = new JButton("Crear");
        crear_boton.setBounds(20, 100, 80, 20);
        
        JButton guardar_boton = new JButton("Guardar");
        guardar_boton.setEnabled(false);
        guardar_boton.setBounds(20, 460, 80, 20);
        
        JButton editar_boton = new JButton("Editar");
        editar_boton.setBounds(120, 460, 80, 20);
        int contador = 0;
        
        JButton eliminar_boton = new JButton("Eliminar");
        eliminar_boton.setBounds(220, 460, 80, 20);
        
        JButton guardar_edicion_boton = new JButton("Terminar");
        guardar_edicion_boton.setBounds(320, 460, 80, 20);
        guardar_edicion_boton.setEnabled(false);
        
        JLabel nombre_label = new JLabel("Nombre");
        nombre_label.setBounds(20, 500, 80, 20);
        
        JLabel rol_label = new JLabel("Rol");
        rol_label.setBounds(20, 540, 80, 20);
        
        JLabel activo_label = new JLabel("Activo");
        activo_label.setBounds(20, 580, 80, 20);
        
        JTextField insertar_nombre_t = new JTextField("");
        insertar_nombre_t.setBounds(120, 500, 200, 20);
        insertar_nombre_t.setEnabled(false);
        
        JComboBox roles = new JComboBox();
        roles.setBounds(120, 540, 200, 20);
        roles.addItem("Administrador");
        roles.addItem("Auditor");
        roles.addItem("Auxiliar");
        roles.setEnabled(false);
        
        JRadioButton si = new JRadioButton("Si");
        si.setEnabled(false);
        si.setBounds(120, 580, 40, 20);
        JRadioButton no = new JRadioButton("No");
        no.setBounds(180, 580, 40, 20);
        no.setEnabled(false);
        ButtonGroup bg = new ButtonGroup();
        bg.add(si);
        bg.add(no);
        si.setSelected(true);
        
        //Agregar elementos de la primera seccion aca
        add(limpiar_boton);
        add(buscar_boton);
        add(campo_nombre);
        add(crear_boton);
        add(guardar_boton);
        add(editar_boton);
        add(eliminar_boton);
        add(scroll);
        add(nombre_label);
        add(rol_label);
        add(activo_label);
        add(insertar_nombre_t);
        add(roles);
        add(si);
        add(no);
        add(guardar_edicion_boton);
        
        //Propiedades de la ventana
        setSize(500, 700);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Eventos de los botones
        ActionListener limpiar = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                campo_nombre.setText("");
            }
        };
        
        ActionListener buscar = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    tabla_usuarios.setModel(UsuariosBusqueda(campo_nombre.getText()));
                } catch (SQLException ex) {
                }
            }
        };
        
        ActionListener crear = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                guardar_boton.setEnabled(true);
                editar_boton.setEnabled(false);
                eliminar_boton.setEnabled(false);
                
                insertar_nombre_t.setEnabled(true);
                roles.setEnabled(true);
                si.setEnabled(true);
                no.setEnabled(true);
            }
        };
        
        ActionListener guardar = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    if(noEsNombreValido(insertar_nombre_t.getText())){
                        JOptionPane.showMessageDialog(null, "Nombre de usuario no valido o ya existente");
                    }
                    else{
                        //Obteniendo rol para query
                        String rol_aux = (String) roles.getSelectedItem();
                        if(rol_aux.equals("Administrador")){rol_aux = "1";}
                        if(rol_aux.equals("Auditor")){rol_aux = "2";}
                        if(rol_aux.equals("Auxiliar")){rol_aux = "3";}
                        
                        //Obteniendo activo para query
                        String activo_aux = "";
                        if(si.isSelected()){activo_aux = "Si";}
                        if(no.isSelected()){activo_aux = "No";}
                        
                        Usuario auxiliar = new Usuario("", rol_aux, insertar_nombre_t.getText(), activo_aux);
                        tabla_usuarios.setModel(agregarUsuario(campo_nombre.getText(), auxiliar));
                        guardar_boton.setEnabled(false);
                        editar_boton.setEnabled(true);
                        eliminar_boton.setEnabled(true);
                
                        insertar_nombre_t.setEnabled(false);
                        roles.setEnabled(false);
                        si.setEnabled(false);
                        no.setEnabled(false);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        
        ActionListener editar = new ActionListener(){
            @Override
            
            public void actionPerformed(ActionEvent e){
                if(tabla_usuarios.getSelectedRow() >= 0){
                    String nombre_editar = (String) tabla_usuarios.getValueAt(tabla_usuarios.getSelectedRow(), 0);
                    try {
                        String id_aux = obtenerIdUsuarioSeleccionado(nombre_editar);
                    } catch (SQLException ex) {
                        Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    guardar_boton.setEnabled(false);
                    editar_boton.setEnabled(false);
                    eliminar_boton.setEnabled(false);
                    insertar_nombre_t.setEnabled(true);
                    roles.setEnabled(true);
                    si.setEnabled(true);
                    no.setEnabled(true);
                    guardar_edicion_boton.setEnabled(true);
                    
                    insertar_nombre_t.setText(nombre_editar);
                }
            }
            
        };
        
        ActionListener eliminar = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(tabla_usuarios.getSelectedRow() >= 0){
                    String nombre_eliminar = (String) tabla_usuarios.getValueAt(tabla_usuarios.getSelectedRow(), 0);
                    try {
                        tabla_usuarios.setModel(eliminarUsuario(campo_nombre.getText(), nombre_eliminar));
                    } catch (SQLException ex) {
                        Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        
        ActionListener terminar = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String nombre_actual = (String) tabla_usuarios.getValueAt(tabla_usuarios.getSelectedRow(), 0);
                String nombre_editado = insertar_nombre_t.getText();
                
                try {
                    if(noEsNombreValido(insertar_nombre_t.getText()) && nombre_actual.equals(nombre_editado) == false){
                        JOptionPane.showMessageDialog(null, "Nombre de usuario no valido");
                    }
                    else{
                        //Obteniendo id del usuario actual
                        String id_usr = obtenerIdUsuarioSeleccionado(nombre_actual);
                        
                        //Obteniendo rol para query
                        String rol_aux = (String) roles.getSelectedItem();
                        if(rol_aux.equals("Administrador")){rol_aux = "1";}
                        if(rol_aux.equals("Auditor")){rol_aux = "2";}
                        if(rol_aux.equals("Auxiliar")){rol_aux = "3";}
                        
                        //Obteniendo activo para query
                        String activo_aux = "";
                        if(si.isSelected()){activo_aux = "Si";}
                        if(no.isSelected()){activo_aux = "No";}
                        
                        Usuario auxiliar = new Usuario(id_usr, rol_aux, nombre_editado, activo_aux);
                        tabla_usuarios.setModel(editarUsuario(campo_nombre.getText(), auxiliar));
                        guardar_boton.setEnabled(false);
                        editar_boton.setEnabled(true);
                        eliminar_boton.setEnabled(true);
                        guardar_edicion_boton.setEnabled(false);
                
                        insertar_nombre_t.setEnabled(false);
                        roles.setEnabled(false);
                        si.setEnabled(false);
                        no.setEnabled(false);
                    }
                    } catch (SQLException ex) {
                        Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
        };
        
        limpiar_boton.addActionListener(limpiar);
        buscar_boton.addActionListener(buscar);
        crear_boton.addActionListener(crear);
        guardar_boton.addActionListener(guardar);
        editar_boton.addActionListener(editar);
        eliminar_boton.addActionListener(eliminar);
        guardar_edicion_boton.addActionListener(terminar);
    }
    
    
    
    //Funciones a utilizar en los eventos de los botones
    //---------------------------------------------------------------------------------------------------------------------------------
    
    public static void mostrarAplicacion(){
        Ventana ventana = new Ventana();
    }
    
    public static String obtenerIdUsuarioSeleccionado(String nombre) throws SQLException{
        String id = "";
        Conexion ob = new Conexion();
        id = ob.obtenerIdUsuarioBD(nombre);
        return id;
    }
    
    public static DefaultTableModel UsuariosBusqueda(String nombre) throws SQLException{
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Rol");
        modelo.addColumn("Activo");
        
        Conexion ob = new Conexion();
        ListaUsuarios lista_usuarios = new ListaUsuarios();
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        
        lista_usuarios = ob.buscarUsuarioPorNombre(nombre);
        usuarios = lista_usuarios.getListaUsuarios();
        
        //imprimir usuarios consultados
        for(int usr=0; usr<usuarios.size(); usr++){
            Usuario aux = usuarios.get(usr);
            String rol_aux = aux.getIdRol();
            if(rol_aux.equals("1")){
                rol_aux = "Administrador";
            }
            if(rol_aux.equals("2")){
                rol_aux = "Auditor";
            }
            if(rol_aux.equals("3")){
                rol_aux = "Axiliar";
            }
            String [] user = {aux.getNombre(), rol_aux, aux.getActivo()};
            modelo.addRow(user);
        }
        
        return modelo;
    }
    
    //Se invoca esta funcion para eliminar un usuario seleccionado por su nombre
    public static DefaultTableModel eliminarUsuario(String nombre, String nombre_eliminar) throws SQLException{
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Rol");
        modelo.addColumn("Activo");
        
        Conexion ob = new Conexion();
        ob.borrarUsuario(nombre_eliminar);
        
        ob = new Conexion();
        ListaUsuarios lista_usuarios = new ListaUsuarios();
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        
        lista_usuarios = ob.buscarUsuarioPorNombre(nombre);
        usuarios = lista_usuarios.getListaUsuarios();
        
        //imprimir usuarios consultados
        for(int usr=0; usr<usuarios.size(); usr++){
            Usuario aux = usuarios.get(usr);
            String rol_aux = aux.getIdRol();
            if(rol_aux.equals("1")){
                rol_aux = "Administrador";
            }
            if(rol_aux.equals("2")){
                rol_aux = "Auditor";
            }
            if(rol_aux.equals("3")){
                rol_aux = "Axiliar";
            }
            String [] user = {aux.getNombre(), rol_aux, aux.getActivo()};
            modelo.addRow(user);
        }
        
        return modelo;
    }
    
    public static DefaultTableModel agregarUsuario(String nombre, Usuario nuevo_usuario) throws SQLException{
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Rol");
        modelo.addColumn("Activo");
        
        Conexion ob = new Conexion();
        ob.agregarUsuario(nuevo_usuario.getIdRol(), nuevo_usuario.getNombre(), nuevo_usuario.getActivo());
        
        ob = new Conexion();
        ListaUsuarios lista_usuarios = new ListaUsuarios();
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        
        lista_usuarios = ob.buscarUsuarioPorNombre(nombre);
        usuarios = lista_usuarios.getListaUsuarios();
        
        //imprimir usuarios consultados
        for(int usr=0; usr<usuarios.size(); usr++){
            Usuario aux = usuarios.get(usr);
            String rol_aux = aux.getIdRol();
            if(rol_aux.equals("1")){
                rol_aux = "Administrador";
            }
            if(rol_aux.equals("2")){
                rol_aux = "Auditor";
            }
            if(rol_aux.equals("3")){
                rol_aux = "Axiliar";
            }
            String [] user = {aux.getNombre(), rol_aux, aux.getActivo()};
            modelo.addRow(user);
        }
        
        return modelo;
    }
    
    //Metodo para editar usuario a partir de su identificador
    public static DefaultTableModel editarUsuario(String nombre, Usuario nuevo_usuario) throws SQLException{
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Rol");
        modelo.addColumn("Activo");
        
        Conexion ob = new Conexion();
        ob.editarUsuario(nuevo_usuario.getIdUsuario(), nuevo_usuario.getIdRol(), nuevo_usuario.getNombre(), nuevo_usuario.getActivo());
        
        ob = new Conexion();
        ListaUsuarios lista_usuarios = new ListaUsuarios();
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        
        lista_usuarios = ob.buscarUsuarioPorNombre(nombre);
        usuarios = lista_usuarios.getListaUsuarios();
        
        //imprimir usuarios consultados
        for(int usr=0; usr<usuarios.size(); usr++){
            Usuario aux = usuarios.get(usr);
            String rol_aux = aux.getIdRol();
            if(rol_aux.equals("1")){
                rol_aux = "Administrador";
            }
            if(rol_aux.equals("2")){
                rol_aux = "Auditor";
            }
            if(rol_aux.equals("3")){
                rol_aux = "Axiliar";
            }
            String [] user = {aux.getNombre(), rol_aux, aux.getActivo()};
            modelo.addRow(user);
        }
        
        return modelo;
    }
    
    public static boolean noEsNombreValido(String nombre_prueba) throws SQLException{
        Conexion ob = new Conexion();
        return (ob.existeNombre(nombre_prueba) || nombre_prueba.equals(""));
    }
}