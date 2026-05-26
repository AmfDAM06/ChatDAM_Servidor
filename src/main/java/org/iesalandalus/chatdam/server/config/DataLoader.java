package org.iesalandalus.chatdam.server.config;

import org.iesalandalus.chatdam.server.model.Usuario;
import org.iesalandalus.chatdam.server.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository) {
        return args -> {
            // Si la base de datos está vacía, creamos el usuario de prueba
            if (repository.findByUsuario("admin").isEmpty()) {
                String passwordCifrada = hashearPassword("1234");
                repository.save(new Usuario("admin", passwordCifrada));
                System.out.println(" Usuario 'admin' creado automáticamente en tu base de datos local.");
            }
        };
    }

    private String hashearPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}