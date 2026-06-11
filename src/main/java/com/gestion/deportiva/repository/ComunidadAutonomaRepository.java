package com.gestion.deportiva.repository;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.ComunidadAutonoma;


@Repository
public interface ComunidadAutonomaRepository extends MaestraRepository<ComunidadAutonoma, Long> {

	ComunidadAutonoma findByActivoTrueAndCodigoIne(Long codigoIne);
	
	ComunidadAutonoma findByActivoTrueAndCodigoIneAndUuid(Long codigoIne, String uuid);
}
