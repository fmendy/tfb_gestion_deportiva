package com.gestion.deportiva.repository;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.Municipio;


@Repository
public interface MunicipioRepository extends MaestraRepository<Municipio, Long> {

	Municipio findByActivoTrueAndCodigoIneAndProvincia_Uuid(Long codigoIne, String provinciaUuid);
}
