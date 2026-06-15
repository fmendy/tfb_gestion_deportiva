package com.gestion.deportiva.util;

import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.SedeFilter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SedeUtil {

	public String cleanUrlPageFilter(SedeFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (StringUtils.hasText(filter.getNombre())) {
				retVal = retVal + "&nombre=" + filter.getNombre();
			}
			if (filter.getListEmpresaIds() != null && !filter.getListEmpresaIds().isEmpty()) {
				retVal = retVal + "&listEmpresaIds=" + filter.getListEmpresaIds();
			}
			if (filter.getListIds() != null && !filter.getListIds().isEmpty()) {
				retVal = retVal + "&listIds=" + filter.getListIds();
			}
			
			if (filter.getEmpresaId() != null ) {
				retVal = retVal + "&empresaId=" + filter.getEmpresaId();
			}
			if (filter.getComunidadAutonomaId() != null ) {
				retVal = retVal + "&comunidadAutonomaId=" + filter.getComunidadAutonomaId();
			}
			if (filter.getProvinciaId() != null ) {
				retVal = retVal + "&provinciaId=" + filter.getProvinciaId();
			}
			if (filter.getMunicipioId() != null ) {
				retVal = retVal + "&municipioId=" + filter.getMunicipioId();
			}
		}
		return retVal;
	}

}
