package org.iesalandalus.chatdam.server.controller;

import org.iesalandalus.chatdam.server.model.Mensaje;
import org.iesalandalus.chatdam.server.repository.MensajeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        List<Mensaje> ultimos = mensajeRepository.findTop10ByOrderByFechaDesc();
        ultimos.sort(Comparator.comparing(Mensaje::getFecha));
        return ResponseEntity.ok(ultimos);
    }
}