package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.InstalacionHorarioEspecial;

@Repository
public interface InstalacionHorarioEspecialRepository extends BaseEntityRepository<InstalacionHorarioEspecial, Long> {

	List<InstalacionHorarioEspecial> findByActivoTrueAndInstalacionId(Long instalacionId);
}
