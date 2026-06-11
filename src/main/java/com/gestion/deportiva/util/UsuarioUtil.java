package com.gestion.deportiva.util;

import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.UsuarioFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuarioUtil {

	public String cleanUrlPageFilter(UsuarioFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (StringUtils.hasText(filter.getNombre())) {
				retVal = retVal + "&nombre=" + filter.getNombre();
			}

			if (StringUtils.hasText(filter.getDemarcacionUuid())) {
				retVal = retVal + "&demarcacionUuid=" + filter.getDemarcacionUuid();
			}

			if (StringUtils.hasText(filter.getRolUuid())) {
				retVal = retVal + "&rolUuid=" + filter.getRolUuid();
			}
			if (StringUtils.hasText(filter.getEmail())) {
				retVal = retVal + "&email=" + filter.getEmail();
			}
		}
		return retVal;
	}

}
