package com.gestion.deportiva.util;

import com.gestion.deportiva.dto.filter.InstalacionHorarioEspecialFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InstalacionHorarioEspecialUtil {

	public String cleanUrlPageFilter(InstalacionHorarioEspecialFilter filter, String url) {
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
			if (filter.getFechaDesde() != null) {
				retVal = retVal + "&fechaDesde=" + filter.getFechaDesde();
			}

			if (filter.getFechaHasta() != null) {
				retVal = retVal + "&fechaHasta=" + filter.getFechaHasta();
			}
			
			if (filter.getCerrado() != null) {
				retVal = retVal + "&cerrado=" + filter.getCerrado();
			}
		}
		return retVal;
	}

}
