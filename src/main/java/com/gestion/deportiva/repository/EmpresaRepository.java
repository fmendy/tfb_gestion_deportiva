package com.gestion.deportiva.repository;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.Empresa;

@Repository
public interface EmpresaRepository extends MaestraRepository<Empresa, Long> {

	Empresa findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(String email, Long id);
}
