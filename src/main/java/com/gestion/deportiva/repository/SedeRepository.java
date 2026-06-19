package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.Sede;

@Repository
public interface SedeRepository extends MaestraRepository<Sede, Long> {

	List<Sede> findByActivoTrueAndIdIn(List<Long> ids);
	
	List<Sede> findByActivoTrueAndEmpresaIdIn(List<Long> ids);
	
	@Query("SELECT s.id FROM Sede s WHERE s.activo = true AND s.empresa.activo = true AND s.empresa.id IN :ids")
	List<Long> findListIdsByListEmpresasIdsIn(@Param("ids") List<Long> ids);
	
}
