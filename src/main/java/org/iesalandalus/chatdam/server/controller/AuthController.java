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
            // Devolvemos el rol en formato JSON puro para que el cliente lo lea
            return ResponseEntity.ok("{\"rol\":\"" + usuarioOpt.get().getRol() + "\"}");
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    @PostMapping("/empleados")
    public ResponseEntity<String> registrarEmpleado(@RequestBody LoginRequest request) {
        if (usuarioRepository.findByUsuario(request.getUsuario()).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }
        // El cliente ya enviará la contraseña cifrada, solo la guardamos con rol EMPLEADO
        Usuario nuevoEmpleado = new Usuario(request.getUsuario(), request.getPassword(), "EMPLEADO");
        usuarioRepository.save(nuevoEmpleado);
        return ResponseEntity.ok("Empleado registrado correctamente");
    }
}