package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.SancionDTO;
import com.gestion.deportiva.dto.filter.SancionFilter;
import com.gestion.deportiva.model.Sancion;
import com.gestion.deportiva.model.SancionTipo;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.Empresa;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SancionUtil {

	public SancionDTO modelToDTO(Sancion model) {
		SancionDTO retVal = new SancionDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setFechaFin(model.getFechaFin());
		retVal.setFechaInicio(model.getFechaInicio());
		retVal.setDescripcion(model.getDescripcion());
		retVal.setEmpresaId(model.getEmpresa().getId());
		retVal.setUsuarioId(model.getUsuario().getId());
		retVal.setSancionTipoId(model.getSancionTipo().getId());
		retVal.setEmpresaNombre(model.getEmpresa().getNombre());
		retVal.setUsuarioNombre(model.getUsuario().getNombre());
		retVal.setSancionTipoNombre(model.getSancionTipo().getNombre());

		return retVal;
	}

	public List<SancionDTO> listModelToListDTO(List<Sancion> list) {
		List<SancionDTO> retVal = new ArrayList<>();
		for (Sancion bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<SancionDTO> pageToPageDTO(Page<Sancion> page) {
		return new PageImpl<SancionDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public Sancion dtoToModel(SancionDTO dto, Sancion model) {
		if (model == null) {
			model = new Sancion();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);

		model.setFechaFin(dto.getFechaFin());
		model.setFechaInicio(dto.getFechaInicio());
		model.setDescripcion(dto.getDescripcion());
		model.setEmpresa(new Empresa(dto.getEmpresaId()));
		model.setUsuario(new Usuario(dto.getUsuarioId()));
		model.setSancionTipo(new SancionTipo(dto.getSancionTipoId()));

		return model;
	}

	public String cleanUrlPageFilter(SancionFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (filter.getEmpresaId() != null) {
				retVal = retVal + "&empresaId=" + filter.getEmpresaId();
			}

			if (filter.getUsuarioId() != null) {
				retVal = retVal + "&usuarioId=" + filter.getUsuarioId();
			}

			if (filter.getSancionTipoId() != null) {
				retVal = retVal + "&sancionTipoId=" + filter.getSancionTipoId();
			}

		}
		return retVal;
	}

	public List<ComboDTO> listModelToListComboDTO(List<Sancion> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getSancionTipo().getNombre())).toList();
	}

}
