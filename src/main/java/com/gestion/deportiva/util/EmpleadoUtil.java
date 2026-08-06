package com.gestion.deportiva.util;

import com.gestion.deportiva.dto.filter.EmpleadoFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmpleadoUtil {

	public String cleanUrlPageFilter(EmpleadoFilter filter, String url) {
		StringBuilder retVal = new StringBuilder(url);

		if (!url.contains("?")) {
			retVal.append("?");
		}

		if (filter != null) {
			appendFilterParams(retVal, filter);
		}

		return retVal.toString();
	}

	private void appendFilterParams(StringBuilder url, EmpleadoFilter filter) {
		Utils.appendParam(url, "email", filter.getEmail());
		Utils.appendParam(url, "nombre", filter.getNombre());
		Utils.appendParam(url, "listEmpresaIds", filter.getListEmpresaIds());
		Utils.appendParam(url, "listSedeIds", filter.getListSedeIds());
		Utils.appendParam(url, "listInstalacionIds", filter.getListInstalacionIds());
		Utils.appendParam(url, "listIds", filter.getListSedeIds());
		Utils.appendParam(url, "empresaId", filter.getEmpresaId());
		Utils.appendParam(url, "instalacionId", filter.getInstalacionId());
		Utils.appendParam(url, "rolId", filter.getRolId());

	}

}
