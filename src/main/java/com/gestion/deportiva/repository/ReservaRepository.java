package com.gestion.deportiva.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.Reserva;

@Repository
public interface ReservaRepository extends BaseEntityRepository<Reserva, Long> {

	List<Reserva> findByInstalacionIdAndFechaAndReservaEstadoNombreIn(Long instalacionId, LocalDate fecha,
			List<String> listReservaEstadoNombres);

	List<Reserva> findByActivoTrueAndFechaAndInstalacionId(LocalDate fecha, Long instalacionId);

	List<Reserva> findByActivoTrueAndFechaAndInstalacionIdAndReservaEstadoNombreIn(LocalDate fecha, Long instalacionId,
			List<String> estadosOcupantes);

	List<Reserva> findByActivoTrueAndFechaAndUsuarioCreacionIdAndReservaEstadoNombreIn(LocalDate fecha, Long usuarioId,
			List<String> listReservaEstados);

}
