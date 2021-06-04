/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectodamsocket;

import drinkgamecad.DrinkgamesAD;
import drinkgamecad.ExcepcionDG;
import herramientas.Frase;
import herramientas.Usuario;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonathan Gonzalez Fraga
 */
public class SesionServidor extends Thread {

    Socket clienteConectado;
    int ele = 0;
    String eleccion = "10";
    Usuario usuario = new Usuario();
    Frase frase = new Frase();
    ArrayList<Frase> frases = new ArrayList<>();
    ObjectInputStream ois = null;
    //InputStream is;

    //Constuctor Completo de la clase SesionServidor
    public SesionServidor(Socket clienteConectado) {
        this.clienteConectado = clienteConectado;
    }

    /**
     * Método que implementa el comportamiento del hilo
     * En este se recogera un Objeto via sockets TCP/IP
     * Y según su eleccion se procedera en su caso
     */
    @Override
    public void run() {

        try {     
            ois = new ObjectInputStream(clienteConectado.getInputStream());
            System.out.println("Servidor.Consola - El servidor recibe el objeto Usuario");
            usuario = (Usuario) ois.readObject();
            
            ele = usuario.getAuxSeleccion();
            System.out.println("Eleccion: "+ele);
            
            //is.close();
            System.out.println(usuario.toString());
            
            usuario.mostrarFrases();
            System.out.println("Frases...");

        } catch (IOException ex) {
            Logger.getLogger(SesionServidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SesionServidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch(NullPointerException et){
            System.out.println(et.getMessage());
        }

        //Creación de Objeto DrinkGamesAd
        DrinkgamesAD dg = null;
        int reg = 0;
        try {
            //Instanci de Objeto DrinkGamesAd
            dg = new DrinkgamesAD("192.168.1.12");
            //dg.setIp();
        } catch (ExcepcionDG ex) {
            Logger.getLogger(SesionServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            //Switch Eleccion de metodo modificacion de los registros de la base de datos
            switch (ele) {

                //Caso Insertar Usuario y Frases
                case 1:

                    reg = dg.insertarUsuario(usuario);
                    
                    System.out.println(usuario.toString());
                    
                    usuario = dg.selectId(usuario.getCorreo());
                    
                    System.out.println(usuario.toString());
                    
                    frase.setDescripcion("Frase 3");
                    frase.setTipo("YN");
                    for(int i = 1; i<41; i++){

                        frase.setDescripcion("Frase "+i);

                        if(i<=20){
                            frase.setTipo("MP");
                            reg = dg.insertarFrasesUsuario(usuario, frase);
                        }else{
                            frase.setTipo("YN");
                            reg = dg.insertarFrasesUsuario(usuario, frase);
                            //System.out.println("Registro Introducido");
                        }
                    }
                    System.out.println("Servidor.Consola - Objeto recibido del Cliente a Insertar: " + reg);
                    System.out.println(usuario.toString());

                    break;

                //Caso Modificar Usuario y Frases
                case 2:

                    reg = dg.modificarUsuario(usuario);
                    for(int i = 0; i<usuario.frases.size(); i++){

                            reg = dg.modificarFrasesUsuario(usuario, i);
                            //System.out.println("Registro Modificado");                       
                    }
                    System.out.println("Servidor.Consola - Objeto recibido del Cliente a Modifcar: " + reg);
                    System.out.println(usuario.toString());

                    break;

                //Caso Eliminar Usuario y Frases
                case 3:

                    usuario = dg.selectId(usuario.getCorreo());
                    reg = dg.eliminarFrasesDeUnUsuario(usuario);
                    reg = dg.eliminarUsuario(usuario.getCorreo());
                    System.out.println("Servidor.Consola - Objeto recibido del Cliente a Borrar: " + reg);
                    break;
                    
                //Caso Seleccionar Usuario y Frases    
                case 4:

                    usuario = dg.selectId(usuario.getCorreo());
                    frases = dg.mostrarFrases(usuario);
                    usuario.setFrases(frases);
                    //usuario.mostrarFrases();
                    System.out.println(usuario.toString());
                    
                    try {

                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                    
                    }
                    
                    ObjectOutputStream oos = new ObjectOutputStream(clienteConectado.getOutputStream());
                    oos.writeObject(usuario);
                    oos.close();
                    
                    System.out.println("Servidor.Consola - Objeto Enviado del Cliente: " + reg);
                    
                    if(usuario != null){
                        System.out.println(usuario.toString());
                    }
                    
                    
                    break;    

                default:
                    break;
                    

            }
            
            ois.close();
            
        } catch (ExcepcionDG ex) {
            Logger.getLogger(SesionServidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SesionServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
