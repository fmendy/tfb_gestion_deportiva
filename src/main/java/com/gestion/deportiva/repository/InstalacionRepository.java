package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.Instalacion;

@Repository
public interface InstalacionRepository extends MaestraRepository<Instalacion, Long> {

	List<Instalacion> findByActivoTrueAndIdIn(List<Long> ids);
}
