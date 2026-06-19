package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.Instalacion;

@Repository
public interface InstalacionRepository extends MaestraRepository<Instalacion, Long> {

	List<Instalacion> findByActivoTrueAndIdIn(List<Long> ids);

	List<Instalacion> findByActivoTrueAndSedeEmpresaIdIn(List<Long> listEmpresaIds);

	List<Instalacion> findByActivoTrueAndSedeIdIn(List<Long> listSedeId);
	
	@Query("SELECT i.id FROM Instalacion i WHERE i.activo = true AND i.sede.activo = true AND i.sede.empresa.activo = true AND i.sede.empresa.id IN :ids")
	List<Long> findListIdsByListEmpresasIdsIn(@Param("ids") List<Long> ids);
	
	@Query("SELECT i.id FROM Instalacion i WHERE i.activo = true AND i.sede.activo = true AND i.sede.id IN :ids")
	List<Long> findListIdsByListSedesIdsIn(@Param("ids") List<Long> ids);
}
