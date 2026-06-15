package com.gestion.deportiva.util;

import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.InstalacionFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InstalacionUtil {

	public String cleanUrlPageFilter(InstalacionFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (StringUtils.hasText(filter.getNombre())) {
				retVal = retVal + "&nombre=" + filter.getNombre();
			}
			if (filter.getEmpresaId() != null) {
				retVal = retVal + "&empresaId=" + filter.getEmpresaId();
			}
			if (filter.getSedeId() != null) {
				retVal = retVal + "&sedeId=" + filter.getSedeId();
			}
			if (filter.getInstalacionTipoId() != null) {
				retVal = retVal + "&instalacionTipoId=" + filter.getInstalacionTipoId();
			}
		}
		return retVal;
	}

}
