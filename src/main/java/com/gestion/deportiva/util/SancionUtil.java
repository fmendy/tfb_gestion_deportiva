package com.gestion.deportiva.util;

import com.cloudinary.utils.StringUtils;
import com.gestion.deportiva.dto.filter.SancionFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SancionUtil {

	public String cleanUrlPageFilter(SancionFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (filter.getReservaId() != null) {
				retVal = retVal + "&reservaId=" + filter.getReservaId();
			}

			if (filter.getUsuarioId() != null) {
				retVal = retVal + "&usuarioId=" + filter.getUsuarioId();
			}

			if (filter.getSancionTipoId() != null) {
				retVal = retVal + "&sancionTipoId=" + filter.getSancionTipoId();
			}

			if (!StringUtils.isBlank(filter.getUsuarioNombre())) {
				retVal = retVal + "&usuarioNombre=" + filter.getUsuarioNombre();
			}

			if (filter.getFechaFinDesde() != null) {
				retVal = retVal + "&fechaFinDesde=" + filter.getFechaFinDesde();
			}

			if (filter.getFechaFinHasta() != null) {
				retVal = retVal + "&fechaFinHasta=" + filter.getFechaFinHasta();
			}

			if (filter.getFechaInicioDesde() != null) {
				retVal = retVal + "&fechaInicioDesde=" + filter.getFechaInicioDesde();
			}

			if (filter.getFechaInicioHasta() != null) {
				retVal = retVal + "&fechaInicioHasta=" + filter.getFechaInicioHasta();
			}

		}
		return retVal;
	}

}
