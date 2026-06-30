package com.gestion.deportiva.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.Reserva;

@Repository
public interface ReservaRepository extends BaseEntityRepository<Reserva, Long> {

	List<Reserva> findByInstalacionIdAndFechaAndReservaEstadoNombreIn(Long instalacionId, LocalDate fecha,
			List<String> listReservaEstadoNombres);

}
