package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionHorarioEspecialDTO;
import com.gestion.deportiva.dto.filter.InstalacionHorarioEspecialFilter;
import com.gestion.deportiva.model.InstalacionHorarioEspecial;
import com.gestion.deportiva.model.Instalacion;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InstalacionHorarioEspecialUtil {

	public InstalacionHorarioEspecialDTO modelToDTO(InstalacionHorarioEspecial model) {
		InstalacionHorarioEspecialDTO retVal = new InstalacionHorarioEspecialDTO();
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
		retVal.setCerrado(model.getCerrado());

		return retVal;
	}

	public List<InstalacionHorarioEspecialDTO> listModelToListDTO(List<InstalacionHorarioEspecial> list) {
		List<InstalacionHorarioEspecialDTO> retVal = new ArrayList<>();
		for (InstalacionHorarioEspecial bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<InstalacionHorarioEspecialDTO> pageToPageDTO(Page<InstalacionHorarioEspecial> page) {
		return new PageImpl<InstalacionHorarioEspecialDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public InstalacionHorarioEspecial dtoToModel(InstalacionHorarioEspecialDTO dto, InstalacionHorarioEspecial model) {
		if (model == null) {
			model = new InstalacionHorarioEspecial();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);

		model.setFecha(dto.getFecha());
		model.setHoraInicio(dto.getHoraInicio());
		model.setHoraFin(dto.getHoraFin());
		model.setInstalacion(new Instalacion(dto.getInstalacionId()));
		model.setCerrado(dto.getCerrado());

		return model;
	}

	public String cleanUrlPageFilter(InstalacionHorarioEspecialFilter filter, String url) {
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

	public List<ComboDTO> listModelToListComboDTO(List<InstalacionHorarioEspecial> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getHoraFin() + " - " + bean.getHoraFin()))
				.toList();
	}

}
