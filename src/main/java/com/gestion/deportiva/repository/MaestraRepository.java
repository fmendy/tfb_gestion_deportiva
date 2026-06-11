package com.gestion.deportiva.repository;


import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import com.gestion.deportiva.model.Maestra;




@NoRepositoryBean
public interface MaestraRepository<T extends Maestra, ID extends Serializable>
		extends BaseEntityRepository<T, Long>, JpaSpecificationExecutor<T>  {

	T findByActivoTrueAndNombreContainsIgnoreCase(String nombre);
	
	T findByActivoTrueAndNombreEqualsIgnoreCase(String nombre);

	T findByActivoTrueAndNombreIgnoreCase(String nombre);

	T findByActivoTrueAndNombreEqualsIgnoreCaseAndIdNot(String nombre, Long id);
	
	List<T> findByActivoTrueOrderByNombreAsc();
	
	List<T> findByActivoTrueOrderByNombreDesc();
	
	@Query("SELECT t.nombre FROM #{#entityName} t WHERE t.activo = true AND t.id = :id")
    String findNombreByActivoTrueAndId(@Param("id") Long id);

}