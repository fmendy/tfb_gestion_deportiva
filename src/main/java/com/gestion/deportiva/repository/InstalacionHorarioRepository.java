package com.gestion.deportiva.repository;

import java.util.List;

import com.gestion.deportiva.model.InstalacionHorario;

public interface InstalacionHorarioRepository extends BaseEntityRepository<InstalacionHorario, Long> {

	List<InstalacionHorario> findByActivoTrueAndInstalacionId(Long instalacionId);

	List<InstalacionHorario> findByActivoTrueAndInstalacionIdAndDiaSemana(Long instalacionId, Long diaSemana);

}
