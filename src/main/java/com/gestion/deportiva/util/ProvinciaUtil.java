package com.gestion.deportiva.util;

import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.ProvinciaFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProvinciaUtil {

	public String cleanUrlPageFilter(ProvinciaFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (StringUtils.hasText(filter.getNombre())) {
				retVal = retVal + "&nombre=" + filter.getNombre();
			}
			if (StringUtils.hasText(filter.getComunidadAutonomaUuid())) {
				retVal = retVal + "&comunidadAutonomaUuid=" + filter.getComunidadAutonomaUuid();
			}
			if (filter.getCodigoIne() != null) {
				retVal = retVal + "&codigoIne=" + filter.getCodigoIne();
			}
		}
		return retVal;
	}

}
