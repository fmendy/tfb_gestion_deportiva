package com.gestion.deportiva.util;

import com.gestion.deportiva.dto.filter.SancionFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SancionUtil {

	public String cleanUrlPageFilter(SancionFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (filter.getEmpresaId() != null) {
				retVal = retVal + "&empresaId=" + filter.getEmpresaId();
			}

			if (filter.getUsuarioId() != null) {
				retVal = retVal + "&usuarioId=" + filter.getUsuarioId();
			}

			if (filter.getSancionTipoId() != null) {
				retVal = retVal + "&sancionTipoId=" + filter.getSancionTipoId();
			}

		}
		return retVal;
	}

}
