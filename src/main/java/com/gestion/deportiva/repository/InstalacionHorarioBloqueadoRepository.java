package com.gestion.deportiva.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.InstalacionHorarioBloqueado;

@Repository
public interface InstalacionHorarioBloqueadoRepository extends BaseEntityRepository<InstalacionHorarioBloqueado, Long> {

	List<InstalacionHorarioBloqueado> findByActivoTrueAndInstalacionIdAndFecha(Long instalacionId, LocalDate fecha);

}
