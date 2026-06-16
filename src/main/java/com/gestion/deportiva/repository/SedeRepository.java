package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.Sede;

@Repository
public interface SedeRepository extends MaestraRepository<Sede, Long> {

	List<Sede> findByActivoTrueAndIdIn(List<Long> ids);
	
	List<Sede> findByActivoTrueAndEmpresaIdIn(List<Long> ids);
	
}
