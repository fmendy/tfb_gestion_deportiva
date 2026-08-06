package com.gestion.deportiva.repository;

import java.time.LocalDate;
import java.util.List;

import com.gestion.deportiva.model.Sancion;

public interface SancionRepository extends BaseEntityRepository<Sancion, Long> {

	Sancion findByActivoTrueAndReservaId(Long reservaId);

	List<Sancion> findByActivoTrueAndUsuarioId(Long usuarioId);

	boolean existsByActivoTrueAndUsuarioIdAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(Long usuarioId,
			LocalDate fechaInicioMax, LocalDate fechaFinMin);

}
