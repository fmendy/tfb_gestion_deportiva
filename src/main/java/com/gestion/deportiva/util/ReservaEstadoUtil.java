package com.gestion.deportiva.util;

import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.ReservaEstadoFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReservaEstadoUtil {

	public String cleanUrlPageFilter(ReservaEstadoFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (StringUtils.hasText(filter.getNombre())) {
				retVal = retVal + "&nombre=" + filter.getNombre();
			}
		}
		return retVal;
	}

}
