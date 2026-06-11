package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.Provincia;


@Repository
public interface ProvinciaRepository extends MaestraRepository<Provincia, Long> {

	Provincia findByActivoTrueAndCodigoIne(Long codigoIne);
	
	List<Provincia> findByActivoTrueAndComunidadAutonoma_Uuid(String comunidadAutonomaUuid);
}
