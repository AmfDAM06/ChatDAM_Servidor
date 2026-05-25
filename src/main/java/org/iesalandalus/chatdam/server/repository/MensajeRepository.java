package org.iesalandalus.chatdam.server.repository;

import org.iesalandalus.chatdam.server.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    List<Mensaje> findTop10ByOrderByFechaDesc();

}