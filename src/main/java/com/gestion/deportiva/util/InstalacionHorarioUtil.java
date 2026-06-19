package com.gestion.deportiva.util;

import com.gestion.deportiva.dto.filter.InstalacionHorarioFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InstalacionHorarioUtil {

	public String cleanUrlPageFilter(InstalacionHorarioFilter filter, String url) {
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
			if (filter.getDiaSemana() != null) {
				retVal = retVal + "&diaSemana=" + filter.getDiaSemana();
			}
		}
		return retVal;
	}

}
