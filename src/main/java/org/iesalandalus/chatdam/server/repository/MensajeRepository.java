package org.iesalandalus.chatdam.server.repository;

import org.iesalandalus.chatdam.server.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    // Este método le dice a MySQL: "Dame los 10 mensajes más recientes ordenados por fecha descendente"
    List<Mensaje> findTop10ByOrderByFechaDesc();
}