package com.gestion.deportiva.util;

import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.EmpleadoFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmpleadoUtil {

	public String cleanUrlPageFilter(EmpleadoFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (StringUtils.hasText(filter.getEmail())) {
				retVal = retVal + "&email=" + filter.getEmail();
			}

			if (StringUtils.hasText(filter.getNombre())) {
				retVal = retVal + "&nombre=" + filter.getNombre();
			}

			if (filter.getListEmpresaIds() != null && !filter.getListEmpresaIds().isEmpty()) {
				retVal = retVal + "&listEmpresaIds=" + filter.getListEmpresaIds();
				retVal.replace("[", "");
			}

			if (filter.getListSedeIds() != null && !filter.getListSedeIds().isEmpty()) {
				retVal = retVal + "&listSedeIds=" + filter.getListSedeIds();
			}

			if (filter.getListInstalacionIds() != null && !filter.getListInstalacionIds().isEmpty()) {
				retVal = retVal + "&listInstalacionIds=" + filter.getListInstalacionIds();
			}

			if (filter.getEmpresaId() != null) {
				retVal = retVal + "&empresaId=" + filter.getEmpresaId();
			}

			if (filter.getSedeId() != null) {
				retVal = retVal + "&sedeId=" + filter.getSedeId();
			}

			if (filter.getInstalacionId() != null) {
				retVal = retVal + "&instalacionId=" + filter.getInstalacionId();
			}

			if (filter.getRolId() != null) {
				retVal = retVal + "&rolId=" + filter.getRolId();
			}
			retVal = retVal.replace("[", "").replace("]", "");

		}
		return retVal;
	}

}
