package com.gestion.deportiva.util;

import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.ComunidadAutonomaFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ComunidadAutonomaUtil {

	public String cleanUrlPageFilter(ComunidadAutonomaFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (StringUtils.hasText(filter.getNombre())) {
				retVal = retVal + "&nombre=" + filter.getNombre();
			}
			if (filter.getCodigoIne() != null) {
				retVal = retVal + "&codigoIne=" + filter.getCodigoIne();
			}
		}
		return retVal;
	}

}
