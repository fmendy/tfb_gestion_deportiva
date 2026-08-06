package com.gestion.deportiva.util;

import com.gestion.deportiva.dto.filter.ReservaFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReservaUtil {

	public String cleanUrlPageFilter(ReservaFilter filter, String url) {
		StringBuilder retVal = new StringBuilder(url);

		if (!url.contains("?")) {
			retVal.append("?");
		}

		if (filter != null) {
			appendFilterParams(retVal, filter);
		}

		return retVal.toString();
	}

	private void appendFilterParams(StringBuilder url, ReservaFilter filter) {
		Utils.appendParam(url, "listEmpresaIds", filter.getListEmpresaIds());
		Utils.appendParam(url, "listSedeIds", filter.getListSedeIds());
		Utils.appendParam(url, "listInstalacionIds", filter.getListInstalacionIds());
		Utils.appendParam(url, "listIds", filter.getListSedeIds());
		Utils.appendParam(url, "empresaId", filter.getEmpresaId());
		Utils.appendParam(url, "instalacionId", filter.getInstalacionId());
		Utils.appendParam(url, "sedeId", filter.getSedeId());
		Utils.appendParam(url, "fechaDesde", filter.getFechaDesde());
		Utils.appendParam(url, "fechaHasta", filter.getFechaHasta());
		Utils.appendParam(url, "reservaEstadoId", filter.getReservaEstadoId());
		Utils.appendParam(url, "usuarioCreacionId", filter.getUsuarioCreacionId());

	}

}
