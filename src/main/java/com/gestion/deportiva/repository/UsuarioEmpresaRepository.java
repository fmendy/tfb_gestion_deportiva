package com.gestion.deportiva.repository;

import java.util.List;

import com.gestion.deportiva.model.UsuarioEmpresa;

public interface UsuarioEmpresaRepository extends BaseEntityRepository<UsuarioEmpresa, Long> {

	List<UsuarioEmpresa> findByActivoTrueAndUsuarioId(Long usuarioId);
}
