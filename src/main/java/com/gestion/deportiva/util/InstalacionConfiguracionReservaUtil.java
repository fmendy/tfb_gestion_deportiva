package com.gestion.deportiva.util;

import com.gestion.deportiva.dto.filter.InstalacionConfiguracionReservaFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InstalacionConfiguracionReservaUtil {

	public String cleanUrlPageFilter(InstalacionConfiguracionReservaFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (filter.getEmpresaId() != null) {
				retVal = retVal + "&empresaId=" + filter.getEmpresaId();
			}

			if (filter.getSedeId() != null) {
				retVal = retVal + "&sedeId=" + filter.getSedeId();
			}

			if (filter.getInstalacionId() != null) {
				retVal = retVal + "&instalacionId=" + filter.getInstalacionId();
			}
		}
		return retVal;
	}

}
