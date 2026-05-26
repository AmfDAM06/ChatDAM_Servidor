package org.iesalandalus.chatdam.server;

import org.iesalandalus.chatdam.server.model.Usuario;
import org.iesalandalus.chatdam.server.repository.MensajeRepository;
import org.iesalandalus.chatdam.server.repository.UsuarioRepository;
import org.iesalandalus.chatdam.server.sockets.ServidorChat;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.security.MessageDigest;
import java.util.Base64;

@SpringBootApplication
public class ChatDamServidorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatDamServidorApplication.class, args);
    }

    @Bean
    CommandLineRunner iniciarSockets(MensajeRepository mensajeRepository) {
        return args -> {
            Thread hiloChat = new Thread(new ServidorChat(mensajeRepository));
            hiloChat.start();
        };
    }

    @Bean
    CommandLineRunner inicializarBaseDeDatos(UsuarioRepository usuarioRepository) {
        return args -> {
            // Si no existe el usuario "admin", lo creamos en la nube
            if (usuarioRepository.findByUsuario("admin").isEmpty()) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest("1234".getBytes());
                String passwordCifrada = Base64.getEncoder().encodeToString(hash);

                usuarioRepository.save(new Usuario("admin", passwordCifrada));
                System.out.println("--> Usuario 'admin' creado correctamente en la nube.");
            }
        };
    }
}