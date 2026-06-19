package com.gestion.deportiva.util;

import com.gestion.deportiva.dto.filter.ReservaFilter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReservaUtil {

	public String cleanUrlPageFilter(ReservaFilter filter, String url) {
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

			if (filter.getReservaEstadoId() != null) {
				retVal = retVal + "&reservaEstadoId=" + filter.getReservaEstadoId();
			}

			if (filter.getUsuarioCreacionId() != null) {
				retVal = retVal + "&usuarioCreacionId=" + filter.getUsuarioCreacionId();
			}
		}
		return retVal;
	}

}
