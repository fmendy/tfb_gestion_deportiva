package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionHorarioDTO;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionHorario;

@Component
public class InstalacionHorarioMapper {

	public InstalacionHorarioDTO modelToDTO(InstalacionHorario model) {
		InstalacionHorarioDTO retVal = new InstalacionHorarioDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setDiaSemana(model.getDiaSemana());
		retVal.setEmpresaId(model.getInstalacion().getSede().getEmpresa().getId());
		retVal.setEmpresaNombre(model.getInstalacion().getSede().getEmpresa().getNombre());
		retVal.setSedeId(model.getInstalacion().getSede().getId());
		retVal.setSedeNombre(model.getInstalacion().getSede().getNombre());
		retVal.setInstalacionId(model.getInstalacion().getId());
		retVal.setInstalacionNombre(model.getInstalacion().getNombre());
		retVal.setHoraInicio(model.getHoraInicio());
		retVal.setHoraFin(model.getHoraFin());

		return retVal;
	}

	public List<InstalacionHorarioDTO> listModelToListDTO(List<InstalacionHorario> list) {
		List<InstalacionHorarioDTO> retVal = new ArrayList<>();
		for (InstalacionHorario bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<InstalacionHorarioDTO> pageToPageDTO(Page<InstalacionHorario> page) {
		return new PageImpl<InstalacionHorarioDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public InstalacionHorario dtoToModel(InstalacionHorarioDTO dto, InstalacionHorario model) {
		if (model == null) {
			model = new InstalacionHorario();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);

		model.setDiaSemana(dto.getDiaSemana());
		model.setHoraInicio(dto.getHoraInicio());
		model.setHoraFin(dto.getHoraFin());
		model.setInstalacion(new Instalacion(dto.getInstalacionId()));

		return model;
	}

	public List<ComboDTO> listModelToListComboDTO(List<InstalacionHorario> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getHoraFin() + " - " + bean.getHoraFin()))
				.toList();
	}

}
