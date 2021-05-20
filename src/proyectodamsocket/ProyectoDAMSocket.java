/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectodamsocket;

/**
 *
 * @author jony8
 */
public class ProyectoDAMSocket {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Inicio Servicio Sockets
        Thread servidor = new Servidor();
        servidor.start();
        
    }
    
}
