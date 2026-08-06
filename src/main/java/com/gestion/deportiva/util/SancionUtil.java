package com.gestion.deportiva.util;

import com.gestion.deportiva.dto.filter.SancionFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SancionUtil {

	public String cleanUrlPageFilter(SancionFilter filter, String url) {
		StringBuilder retVal = new StringBuilder(url);

		if (!url.contains("?")) {
			retVal.append("?");
		}

		if (filter != null) {
			appendFilterParams(retVal, filter);
		}

		return retVal.toString();
	}

	private void appendFilterParams(StringBuilder url, SancionFilter filter) {
		Utils.appendParam(url, "reservaId", filter.getReservaId());
		Utils.appendParam(url, "usuarioId", filter.getUsuarioId());
		Utils.appendParam(url, "sancionTipoId", filter.getSancionTipoId());
		Utils.appendParam(url, "usuarioNombre", filter.getUsuarioNombre());
		Utils.appendParam(url, "fechaFinDesde", filter.getFechaFinDesde());
		Utils.appendParam(url, "fechaFinHasta", filter.getFechaFinHasta());
		Utils.appendParam(url, "fechaInicioDesde", filter.getFechaInicioDesde());
		Utils.appendParam(url, "fechaInicioHasta", filter.getFechaInicioHasta());
	}

}
