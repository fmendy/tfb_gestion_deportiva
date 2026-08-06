package com.gestion.deportiva.repository;

import com.gestion.deportiva.model.ComunidadAutonoma;


public interface ComunidadAutonomaRepository extends MaestraRepository<ComunidadAutonoma, Long> {

	ComunidadAutonoma findByActivoTrueAndCodigoIne(Long codigoIne);
	
	ComunidadAutonoma findByActivoTrueAndCodigoIneAndUuid(Long codigoIne, String uuid);
}
