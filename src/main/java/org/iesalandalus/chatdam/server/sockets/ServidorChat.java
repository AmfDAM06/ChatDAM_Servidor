package org.iesalandalus.chatdam.server.sockets;

import org.iesalandalus.chatdam.server.model.Mensaje;
import org.iesalandalus.chatdam.server.repository.MensajeRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorChat implements Runnable {

    private final int PUERTO_CHAT = 4444;
    private final List<ManejadorCliente> clientesConectados = new ArrayList<>();
    private final MensajeRepository mensajeRepository; // <-- Añadido

    public ServidorChat(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO_CHAT)) {
            System.out.println("Servidor de Sockets TCP iniciado y escuchando en el puerto: " + PUERTO_CHAT);

            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Nuevo cliente conectado al chat desde: " + socketCliente.getInetAddress().getHostAddress());

                ManejadorCliente manejador = new ManejadorCliente(socketCliente, this);
                clientesConectados.add(manejador);
                new Thread(manejador).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void broadcastMensaje(String textoMensaje, ManejadorCliente emisor) {
        for (ManejadorCliente cliente : clientesConectados) {
            if (cliente != emisor) {
                cliente.enviarMensaje(textoMensaje);
            }
        }
    }

    // Nuevo método para que el Manejador guarde el mensaje en la BD
    public synchronized void guardarMensajeEnBD(Mensaje mensaje) {
        mensajeRepository.save(mensaje);
    }

    public synchronized void eliminarCliente(ManejadorCliente cliente) {
        clientesConectados.remove(cliente);
    }
}