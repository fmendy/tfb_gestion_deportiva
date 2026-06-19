package com.gestion.deportiva.util;

import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.MunicipioFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MunicipioUtil {

	public String cleanUrlPageFilter(MunicipioFilter filter, String url) {
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
			if (StringUtils.hasText(filter.getProvinciaUuid())) {
				retVal = retVal + "&provinciaUuid=" + filter.getProvinciaUuid();
			}
			if (filter.getCodigoIne() != null) {
				retVal = retVal + "&codigoIne=" + filter.getCodigoIne();
			}
			if (filter.getDc() != null) {
				retVal = retVal + "&dc=" + filter.getDc();
			}
		}
		return retVal;
	}

}
