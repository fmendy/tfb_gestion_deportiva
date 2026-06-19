package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionHorarioBloqueadoDTO;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionHorarioBloqueado;

@Component
public class InstalacionHorarioBloqueadoMapper {

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

	public InstalacionHorarioBloqueado dtoToModel(InstalacionHorarioBloqueadoDTO dto,
			InstalacionHorarioBloqueado model) {
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

	public List<ComboDTO> listModelToListComboDTO(List<InstalacionHorarioBloqueado> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getHoraFin() + " - " + bean.getHoraFin()))
				.toList();
	}
}
