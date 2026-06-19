package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.UsuarioSede;

@Repository
public interface UsuarioSedeRepository extends BaseEntityRepository<UsuarioSede, Long> {

	List<UsuarioSede> findByActivoTrueAndUsuarioId(Long id);

}
