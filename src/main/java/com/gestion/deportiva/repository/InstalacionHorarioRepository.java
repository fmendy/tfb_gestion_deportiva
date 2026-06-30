package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.InstalacionHorario;

@Repository
public interface InstalacionHorarioRepository extends BaseEntityRepository<InstalacionHorario, Long> {

	List<InstalacionHorario> findByActivoTrueAndInstalacionId(Long instalacionId);
	
	List<InstalacionHorario> findByActivoTrueAndInstalacionIdAndDiaSemana(Long instalacionId, Long diaSemana);

}
