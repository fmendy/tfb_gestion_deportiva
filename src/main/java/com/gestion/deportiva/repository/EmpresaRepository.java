package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.Empresa;

@Repository
public interface EmpresaRepository extends MaestraRepository<Empresa, Long> {

	Empresa findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(String email, Long id);

	Empresa findByActivoTrueAndCifEqualsIgnoreCaseAndIdNot(String cif, Long id);

	List<Empresa> findByActivoTrueAndIdIn(List<Long> ids);

}
