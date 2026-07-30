package com.gestion.deportiva.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.InstalacionConfiguracionReserva;

@Repository
public interface InstalacionConfiguracionReservaRepository extends BaseEntityRepository<InstalacionConfiguracionReserva, Long> {

	InstalacionConfiguracionReserva findByActivoTrueAndInstalacionId(Long instalacionId);

	Collection<Long> findAllByActivoTrueAndIdIn(List<Long> list);
}
