package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.UsuarioEmpresa;

@Repository
public interface UsuarioEmpresaRepository extends BaseEntityRepository<UsuarioEmpresa, Long> {

	List<UsuarioEmpresa> findByActivoTrueAndUsuarioId(Long usuarioId);
}
