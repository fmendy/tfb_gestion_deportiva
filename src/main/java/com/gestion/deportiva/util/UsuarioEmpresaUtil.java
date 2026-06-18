package com.gestion.deportiva.util;

import com.gestion.deportiva.dto.filter.UsuarioEmpresaFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuarioEmpresaUtil {

	public String cleanUrlPageFilter(UsuarioEmpresaFilter filter, String url) {
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

		}
		return retVal;
	}

}
