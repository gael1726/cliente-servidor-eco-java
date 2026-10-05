/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.unison.javacosas_gui.eco;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    private Socket socket;
    private PrintWriter salidaServidor;
    private BufferedReader entradaServidor;

    public boolean conectar(String host, int puerto) {
        try {
            socket = new Socket(host, puerto);
            salidaServidor = new PrintWriter(socket.getOutputStream(), true);
            entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;
        } catch (Exception e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return false;
        }
    }

    public String recibirMensajeInicial() {
        try {
            return entradaServidor.readLine();
        } catch (Exception e) {
            return "Error al recibir mensaje inicial: " + e.getMessage();
        }
    }

    public String enviarMensaje(String mensaje) {
        try {
            salidaServidor.println(mensaje);
            return entradaServidor.readLine();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public void cerrar() {
        try {
            socket.close();
        } catch (Exception e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}

