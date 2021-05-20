/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectodamsocket;

import drinkgamecad.DrinkgamesAD;
import drinkgamecad.ExcepcionDG;
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
    ObjectInputStream ois = null;
    //InputStream is;

    public SesionServidor(Socket clienteConectado) {
        this.clienteConectado = clienteConectado;
    }

    /**
     * Método que implementa el comportamiento del hilo
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

        DrinkgamesAD dg = null;
        int reg = 0;
        try {
            dg = new DrinkgamesAD();
        } catch (ExcepcionDG ex) {
            Logger.getLogger(SesionServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            switch (ele) {

                case 1:

                    reg = dg.insertarUsuario(usuario);
                    System.out.println("Servidor.Consola - Objeto recibido del Cliente a Insertar: " + reg);
                    System.out.println(usuario.toString());

                    break;

                case 2:

                    reg = dg.modificarUsuario(usuario);
                    System.out.println("Servidor.Consola - Objeto recibido del Cliente a Modifcar: " + reg);
                    System.out.println(usuario.toString());

                    break;

                case 3:

                    reg = dg.eliminarUsuario(usuario.getCorreo());
                    System.out.println("Servidor.Consola - Objeto recibido del Cliente a Borrar: " + reg);
                    break;
                    
                case 4:

                    usuario = dg.selectId(usuario.getCorreo());
                    System.out.println(usuario.toString());
                    
                    try {

                        Thread.sleep(1500);

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
