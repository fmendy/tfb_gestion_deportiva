package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionHorarioBloqueadoDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioBloqueadoFilter;
import com.gestion.deportiva.model.InstalacionHorarioBloqueado;
import com.gestion.deportiva.model.Instalacion;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InstalacionHorarioBloqueadoUtil {

	public InstalacionHorarioBloqueadoDTO modelToDTO(InstalacionHorarioBloqueado model) {
		InstalacionHorarioBloqueadoDTO retVal = new InstalacionHorarioBloqueadoDTO();
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

		return retVal;
	}

	public List<InstalacionHorarioBloqueadoDTO> listModelToListDTO(List<InstalacionHorarioBloqueado> list) {
		List<InstalacionHorarioBloqueadoDTO> retVal = new ArrayList<>();
		for (InstalacionHorarioBloqueado bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<InstalacionHorarioBloqueadoDTO> pageToPageDTO(Page<InstalacionHorarioBloqueado> page) {
		return new PageImpl<InstalacionHorarioBloqueadoDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public InstalacionHorarioBloqueado dtoToModel(InstalacionHorarioBloqueadoDTO dto, InstalacionHorarioBloqueado model) {
		if (model == null) {
			model = new InstalacionHorarioBloqueado();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);

		model.setFecha(dto.getFecha());
		model.setHoraInicio(dto.getHoraInicio());
		model.setHoraFin(dto.getHoraFin());
		model.setInstalacion(new Instalacion(dto.getInstalacionId()));

		return model;
	}

	public String cleanUrlPageFilter(InstalacionHorarioBloqueadoFilter filter, String url) {
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
		}
		return retVal;
	}

	public List<ComboDTO> listModelToListComboDTO(List<InstalacionHorarioBloqueado> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getHoraFin() + " - " + bean.getHoraFin()))
				.toList();
	}

}
