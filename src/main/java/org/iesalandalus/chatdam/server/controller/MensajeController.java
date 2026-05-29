package org.iesalandalus.chatdam.server.controller;

import org.iesalandalus.chatdam.server.model.Mensaje;
import org.iesalandalus.chatdam.server.repository.MensajeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MensajeController {

    private final MensajeRepository mensajeRepository;

    public MensajeController(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    @GetMapping("/mensajes")
    public ResponseEntity<List<Mensaje>> obtenerUltimosMensajes() {
        // Obtenemos los 10 últimos y los ordenamos cronológicamente
        List<Mensaje> ultimos = mensajeRepository.findTop10ByOrderByFechaDesc();
        ultimos.sort(Comparator.comparing(Mensaje::getFecha));
        return ResponseEntity.ok(ultimos);
    }

    @PostMapping("/mensajes")
    public ResponseEntity<String> guardarMensaje(@RequestBody Mensaje mensaje) {
        try {
            // Le asignamos la hora exacta del servidor justo antes de guardar
            mensaje.setFecha(LocalDateTime.now());

            // Guardamos en MySQL
            mensajeRepository.save(mensaje);
            return ResponseEntity.ok("Mensaje guardado correctamente en la nube");
        } catch (Exception e) {
            System.out.println("Error crítico al guardar en BD: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Fallo al guardar");
        }
    }
}