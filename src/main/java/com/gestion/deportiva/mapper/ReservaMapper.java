package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.MiReservaDTO;
import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.ReservaEstado;
import com.gestion.deportiva.util.Constantes;

@Component
public class ReservaMapper {

	public ReservaDTO modelToDTO(Reserva model) {
		ReservaDTO retVal = new ReservaDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setInstalacionSedeEmpresaId(model.getInstalacion().getSede().getEmpresa().getId());
		retVal.setInstalacionSedeEmpresaNombre(model.getInstalacion().getSede().getEmpresa().getNombre());
		retVal.setInstalacionSedeId(model.getInstalacion().getSede().getId());
		retVal.setInstalacionSedeNombre(model.getInstalacion().getSede().getNombre());
		retVal.setInstalacionId(model.getInstalacion().getId());
		retVal.setInstalacionNombre(model.getInstalacion().getNombre());
		retVal.setInstalacionInstalacionTipoId(model.getInstalacion().getInstalacionTipo().getId());
		retVal.setInstalacionInstalacionTipoNombre(model.getInstalacion().getInstalacionTipo().getNombre());
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

	public List<MiReservaDTO> listModelToListMiReservaDTO(List<Reserva> list) {
		List<MiReservaDTO> retVal = new ArrayList<>();
		for (Reserva bean : list) {
			retVal.add(modelToMiReservaDTO(bean));
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

	public List<ComboDTO> listModelToListComboDTO(List<Reserva> list) {
		return null;
	}

	public ReservaSolicitudDTO instalacionModelToReservaInstalacionDTO(Instalacion instalacion,
			ReservaSolicitudDTO dto) {
		ReservaSolicitudDTO retVal = new ReservaSolicitudDTO();
		retVal.setDuracion(dto.getDuracion());
		retVal.setFecha(dto.getFecha());
		retVal.setHora(dto.getHora());
		retVal.setHoraFin(dto.getHora().plusMinutes(dto.getDuracion()));
		retVal.setInstalacionId(instalacion.getId());
		retVal.setInstalacionNombre(instalacion.getNombre());
		retVal.setInstalacionSedeNombre(instalacion.getSede().getNombre());
		retVal.setInstalacionSedeEmpresaNombre(instalacion.getSede().getEmpresa().getNombre());
		retVal.setInstalacionInstalacionTipoNombre(instalacion.getInstalacionTipo().getNombre());
		return retVal;

	}

	public MiReservaDTO modelToMiReservaDTO(Reserva model) {
		MiReservaDTO retVal = new MiReservaDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setInstalacionSedeEmpresaId(model.getInstalacion().getSede().getEmpresa().getId());
		retVal.setInstalacionSedeEmpresaNombre(model.getInstalacion().getSede().getEmpresa().getNombre());
		retVal.setInstalacionSedeId(model.getInstalacion().getSede().getId());
		retVal.setInstalacionSedeNombre(model.getInstalacion().getSede().getNombre());
		retVal.setInstalacionId(model.getInstalacion().getId());
		retVal.setInstalacionNombre(model.getInstalacion().getNombre());
		retVal.setInstalacionInstalacionTipoId(model.getInstalacion().getInstalacionTipo().getId());
		retVal.setInstalacionInstalacionTipoNombre(model.getInstalacion().getInstalacionTipo().getNombre());
		retVal.setHoraInicio(model.getHoraInicio());
		retVal.setHoraFin(model.getHoraFin());
		retVal.setFecha(model.getFecha());
		retVal.setReservaEstadoId(model.getReservaEstado().getId());
		retVal.setReservaEstadoNombre(model.getReservaEstado().getNombre());
		retVal.setMostrarEliminar(model.getReservaEstado().getNombre().equalsIgnoreCase(Constantes.ReservaEstado.PENDIENTE));
		retVal.setMostrarCancelarPorUsuario(model.getReservaEstado().getNombre().equalsIgnoreCase(Constantes.ReservaEstado.APROBADA));
		retVal.setReservaEstadoCss(model.getReservaEstado().getNombre().equals(Constantes.ReservaEstado.APROBADA)
				? "bg-success-subtle text-success border-success-subtle"
				: model.getReservaEstado().getNombre().equals(Constantes.ReservaEstado.PENDIENTE)
						? "bg-warning-subtle text-warning border-warning-subtle"
						: "bg-secondary-subtle text-secondary border-secondary-subtle");

		return retVal;
	}

	public Page<MiReservaDTO> pageToPageMiReservaDTO(Page<Reserva> page) {
		return new PageImpl<MiReservaDTO>(listModelToListMiReservaDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

}
