package com.gestion.deportiva.util;

import com.gestion.deportiva.dto.filter.SedeFilter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SedeUtil {

	public String cleanUrlPageFilter(SedeFilter filter, String url) {
		StringBuilder retVal = new StringBuilder(url);

		if (!url.contains("?")) {
			retVal.append("?");
		}

		if (filter != null) {
			appendFilterParams(retVal, filter);
		}

		return retVal.toString();
	}

	private void appendFilterParams(StringBuilder url, SedeFilter filter) {
		Utils.appendParam(url, "nombre", filter.getNombre());
		String listEmpresasIds = filter.getListEmpresaIds().toString().replace("[", "").replace("]", "");
		Utils.appendParam(url, "listEmpresaIds", listEmpresasIds);
		Utils.appendParam(url, "listIds", filter.getListIds());
		Utils.appendParam(url, "empresaId", filter.getEmpresaId());
		Utils.appendParam(url, "comunidadAutonomaId", filter.getComunidadAutonomaId());
		Utils.appendParam(url, "provinciaId", filter.getProvinciaId());
		Utils.appendParam(url, "municipioId", filter.getMunicipioId());
	}

}
