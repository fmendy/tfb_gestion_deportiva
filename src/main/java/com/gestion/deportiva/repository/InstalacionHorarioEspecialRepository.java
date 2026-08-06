package com.gestion.deportiva.repository;

import java.time.LocalDate;
import java.util.List;

import com.gestion.deportiva.model.InstalacionHorarioEspecial;

public interface InstalacionHorarioEspecialRepository extends BaseEntityRepository<InstalacionHorarioEspecial, Long> {

	List<InstalacionHorarioEspecial> findByActivoTrueAndInstalacionId(Long instalacionId);

	List<InstalacionHorarioEspecial> findByActivoTrueAndInstalacionIdAndFecha(Long instalacionId, LocalDate fecha);

	InstalacionHorarioEspecial findByActivoTrueAndCerradoTrueAndFecha(LocalDate fecha);
}
