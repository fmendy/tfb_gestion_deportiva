package com.gestion.deportiva.repository;

import java.util.Collection;
import java.util.List;

import com.gestion.deportiva.model.InstalacionConfiguracionReserva;

public interface InstalacionConfiguracionReservaRepository
		extends BaseEntityRepository<InstalacionConfiguracionReserva, Long> {

	InstalacionConfiguracionReserva findByActivoTrueAndInstalacionId(Long instalacionId);

	Collection<Long> findAllByActivoTrueAndIdIn(List<Long> list);
}
