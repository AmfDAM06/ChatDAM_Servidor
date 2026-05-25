package org.iesalandalus.chatdam.server.sockets;

import org.iesalandalus.chatdam.server.model.Mensaje;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;

public class ManejadorCliente implements Runnable {

    private final Socket socket;
    private final ServidorChat servidor;
    private DataInputStream in;
    private DataOutputStream out;

    public ManejadorCliente(Socket socket, ServidorChat servidor) {
        this.socket = socket;
        this.servidor = servidor;
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String mensajeRecibido = in.readUTF();
                System.out.println("Mensaje recibido: " + mensajeRecibido);

                // Suponemos que el cliente envía el mensaje con el formato "Nombre: Hola mundo"
                String autor = "Desconocido";
                String texto = mensajeRecibido;

                if(mensajeRecibido.contains(": ")) {
                    String[] partes = mensajeRecibido.split(": ", 2);
                    autor = partes[0];
                    texto = partes[1];
                }

                // Guardamos en Base de Datos
                Mensaje nuevoMensaje = new Mensaje(autor, texto, LocalDateTime.now());
                servidor.guardarMensajeEnBD(nuevoMensaje);

                // Reenviamos al resto
                servidor.broadcastMensaje(mensajeRecibido, this);
            }
        } catch (IOException e) {
            servidor.eliminarCliente(this);
            System.out.println("Cliente desconectado.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void enviarMensaje(String mensaje) {
        try {
            out.writeUTF(mensaje);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}