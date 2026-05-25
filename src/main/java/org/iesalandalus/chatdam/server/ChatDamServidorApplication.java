package org.iesalandalus.chatdam.server;

import org.iesalandalus.chatdam.server.repository.MensajeRepository;
import org.iesalandalus.chatdam.server.sockets.ServidorChat;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChatDamServidorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatDamServidorApplication.class, args);
    }

    @Bean
    CommandLineRunner iniciarSockets(MensajeRepository mensajeRepository) {
        return args -> {
            // Le pasamos el repositorio de Spring a nuestro servidor de Sockets puro
            Thread hiloChat = new Thread(new ServidorChat(mensajeRepository));
            hiloChat.start();
        };
    }
}