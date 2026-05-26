package org.iesalandalus.chatdam.server.controller;

import org.iesalandalus.chatdam.server.dto.LoginRequest;
import org.iesalandalus.chatdam.server.model.Usuario;
import org.iesalandalus.chatdam.server.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(request.getUsuario());

        if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(request.getPassword())) {
            // AQUÍ ESTÁ EL CAMBIO DE SEGURIDAD:
            // Si el rol es null, le asignamos "EMPLEADO" para evitar errores en el cliente
            String rol = usuarioOpt.get().getRol();
            if (rol == null || rol.isEmpty()) {
                rol = "EMPLEADO";
            }

            // Devolvemos el JSON con el rol garantizado
            return ResponseEntity.ok("{\"rol\":\"" + rol + "\"}");
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    @PostMapping("/empleados")
    public ResponseEntity<String> registrarEmpleado(@RequestBody LoginRequest request) {
        if (usuarioRepository.findByUsuario(request.getUsuario()).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }
        // Guardamos con rol EMPLEADO por defecto
        Usuario nuevoEmpleado = new Usuario(request.getUsuario(), request.getPassword(), "EMPLEADO");
        usuarioRepository.save(nuevoEmpleado);
        return ResponseEntity.ok("Empleado registrado correctamente");
    }
}