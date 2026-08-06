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
		String listEmpresasIds = filter.getListEmpresaIds().toString().replace("[", "").replace("]", "");
		Utils.appendParam(url, "listEmpresaIds", listEmpresasIds);
		String listSedesIds = filter.getListSedeIds().toString().replace("[", "").replace("]", "");
		Utils.appendParam(url, "listSedeIds", listSedesIds);
		String listInstalacionIds = filter.getListInstalacionIds().toString().replace("[", "").replace("]", "");
		Utils.appendParam(url, "listInstalacionIds", listInstalacionIds);
		Utils.appendParam(url, "listIds", filter.getListSedeIds());
		Utils.appendParam(url, "empresaId", filter.getEmpresaId());
		Utils.appendParam(url, "instalacionId", filter.getInstalacionId());
		Utils.appendParam(url, "rolId", filter.getRolId());

	}

}
