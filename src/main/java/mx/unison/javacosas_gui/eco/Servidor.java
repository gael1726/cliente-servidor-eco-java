package mx.unison.javacosas_gui.eco;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Servidor {

    private static final int PUERTO = 5000;
    private static final int MAX_CLIENTES = 10; // Tamaño máximo del ThreadPool
    private static AtomicInteger contadorClientes = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println("Servidor iniciado en el puerto: " + PUERTO);

        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_CLIENTES);

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            while (true) {
                // Esperar nuevas conexiones
                Socket socketCliente = servidor.accept();
                int idCliente = contadorClientes.incrementAndGet();
                System.out.println("Nuevo cliente conectado: Cliente #" + idCliente);

                // Enviar al ThreadPool una nueva tarea para manejar ese cliente
                threadPool.execute(new ManejadorCliente(socketCliente, idCliente));
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }
}

class ManejadorCliente implements Runnable {

    private Socket socket;
    private int idCliente;

    public ManejadorCliente(Socket socket, int idCliente) {
        this.socket = socket;
        this.idCliente = idCliente;
    }

    @Override
    public void run() {
        try (
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
        ) {
            salida.println("Bienvenido Cliente #" + idCliente + ". Escribe 'cerrar' para salir.");

            String mensaje;
            while ((mensaje = entrada.readLine()) != null && !"cerrar".equalsIgnoreCase(mensaje.trim())) {
                System.out.println("[Cliente #" + idCliente + "]: " + mensaje);
                salida.println("Servidor a Cliente #" + idCliente + ": " + mensaje);
            }

            System.out.println("Cliente #" + idCliente + " se ha desconectado.");

        } catch (IOException e) {
            System.out.println("Error con Cliente #" + idCliente + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar conexión del Cliente #" + idCliente);
            }
        }
    }
}
