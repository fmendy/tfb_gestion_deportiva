package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.filter.ReservaFilter;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.ReservaEstado;
import com.gestion.deportiva.model.Instalacion;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReservaUtil {

	public ReservaDTO modelToDTO(Reserva model) {
		ReservaDTO retVal = new ReservaDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setEmpresaId(model.getInstalacion().getSede().getEmpresa().getId());
		retVal.setEmpresaNombre(model.getInstalacion().getSede().getEmpresa().getNombre());
		retVal.setSedeId(model.getInstalacion().getSede().getId());
		retVal.setSedeNombre(model.getInstalacion().getSede().getNombre());
		retVal.setInstalacionId(model.getInstalacion().getId());
		retVal.setInstalacionNombre(model.getInstalacion().getNombre());
		retVal.setHoraInicio(model.getHoraInicio());
		retVal.setHoraFin(model.getHoraFin());
		retVal.setFecha(model.getFecha());
		retVal.setReservaEstadoId(model.getReservaEstado().getId());
		retVal.setReservaEstadoNombre(model.getReservaEstado().getNombre());

		return retVal;
	}

	public List<ReservaDTO> listModelToListDTO(List<Reserva> list) {
		List<ReservaDTO> retVal = new ArrayList<>();
		for (Reserva bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<ReservaDTO> pageToPageDTO(Page<Reserva> page) {
		return new PageImpl<ReservaDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public Reserva dtoToModel(ReservaDTO dto, Reserva model) {
		if (model == null) {
			model = new Reserva();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);

		model.setFecha(dto.getFecha());
		model.setHoraInicio(dto.getHoraInicio());
		model.setHoraFin(dto.getHoraFin());
		model.setInstalacion(new Instalacion(dto.getInstalacionId()));
		model.setReservaEstado(new ReservaEstado(dto.getReservaEstadoId()));

		return model;
	}

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

	public List<ComboDTO> listModelToListComboDTO(List<Reserva> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getHoraFin() + " - " + bean.getHoraFin()))
				.toList();
	}

}
