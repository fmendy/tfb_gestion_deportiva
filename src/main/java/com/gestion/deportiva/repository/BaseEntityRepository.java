package com.gestion.deportiva.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.gestion.deportiva.model.BaseEntity;



@NoRepositoryBean
public interface BaseEntityRepository<T extends BaseEntity, ID extends Serializable>
		extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

	List<T> findByActivoTrue();
	
	List<T> findByActivoTrueAndUsuarioCreacion_Id(Long usuarioCreacionId);

	T findByActivoTrueAndId(Long id);
	
	T findByActivoTrueAndUuidEqualsIgnoreCase(String uuid);
}