package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionConfiguracionReservaDTO;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionConfiguracionReserva;

@Component
public class InstalacionConfiguracionReservaMapper {

	public InstalacionConfiguracionReservaDTO modelToDTO(InstalacionConfiguracionReserva model) {
		InstalacionConfiguracionReservaDTO retVal = new InstalacionConfiguracionReservaDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setEmpresaId(model.getInstalacion().getSede().getEmpresa().getId());
		retVal.setEmpresaNombre(model.getInstalacion().getSede().getEmpresa().getNombre());
		retVal.setSedeId(model.getInstalacion().getSede().getId());
		retVal.setSedeNombre(model.getInstalacion().getSede().getNombre());
		retVal.setInstalacionId(model.getInstalacion().getId());
		retVal.setInstalacionNombre(model.getInstalacion().getNombre());
		retVal.setDuracionMax(model.getDuracionMax());
		retVal.setDuracionMin(model.getDuracionMin());
		retVal.setIntervaloHorario(model.getIntervaloHorario());

		return retVal;
	}

	public List<InstalacionConfiguracionReservaDTO> listModelToListDTO(List<InstalacionConfiguracionReserva> list) {
		List<InstalacionConfiguracionReservaDTO> retVal = new ArrayList<>();
		for (InstalacionConfiguracionReserva bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<InstalacionConfiguracionReservaDTO> pageToPageDTO(Page<InstalacionConfiguracionReserva> page) {
		return new PageImpl<InstalacionConfiguracionReservaDTO>(listModelToListDTO(page.getContent()),
				page.getPageable(), page.getTotalElements());
	}

	public InstalacionConfiguracionReserva dtoToModel(InstalacionConfiguracionReservaDTO dto,
			InstalacionConfiguracionReserva model) {
		if (model == null) {
			model = new InstalacionConfiguracionReserva();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
		model.setInstalacion(new Instalacion(dto.getInstalacionId()));
		model.setDuracionMax(dto.getDuracionMax());
		model.setDuracionMin(dto.getDuracionMin());
		model.setIntervaloHorario(dto.getIntervaloHorario());

		return model;
	}

	public List<ComboDTO> listModelToListComboDTO(List<InstalacionConfiguracionReserva> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getInstalacion().getNombre())).toList();
	}

}
