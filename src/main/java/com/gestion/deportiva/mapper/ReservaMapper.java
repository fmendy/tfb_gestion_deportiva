package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ReservaListadoDTO;
import com.gestion.deportiva.dto.ReservaDTO;
import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.model.ReservaEstado;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

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

	public List<ReservaListadoDTO> listModelToListReservaListadoDTO(List<Reserva> list) {
		List<ReservaListadoDTO> retVal = new ArrayList<>();
		for (Reserva bean : list) {
			retVal.add(modelToReservaListadoDTO(bean));
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

	public ReservaListadoDTO modelToReservaListadoDTO(Reserva model) {
		ReservaListadoDTO retVal = new ReservaListadoDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setUsuarioId(model.getUsuarioCreacion().getId());
		retVal.setUsuarioNombre(model.getUsuarioCreacion().getNombre());
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
		retVal.setMostrarEliminar(
				model.getReservaEstado().getNombre().equalsIgnoreCase(Constantes.ReservaEstado.PENDIENTE)
						&& model.getUsuarioCreacion().getId().equals(SecurityUtil.getCurrentUserId()));
		retVal.setMostrarAprobar(
				model.getReservaEstado().getNombre().equalsIgnoreCase(Constantes.ReservaEstado.PENDIENTE)
						&& (SecurityUtil.hasAnyAuthority(Constantes.Permiso.GESTION_GLOBAL,
								Constantes.Permiso.Reserva.GESTION_RESERVA_GLOBAL,
								Constantes.Permiso.Reserva.GESTION_RESERVA_EMPRESA,
								Constantes.Permiso.Reserva.GESTION_RESERVA_SEDE,
								Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION)));

		retVal.setMostrarDenegar(
				model.getReservaEstado().getNombre().equalsIgnoreCase(Constantes.ReservaEstado.PENDIENTE)
						&& (SecurityUtil.hasAnyAuthority(Constantes.Permiso.GESTION_GLOBAL,
								Constantes.Permiso.Reserva.GESTION_RESERVA_GLOBAL,
								Constantes.Permiso.Reserva.GESTION_RESERVA_EMPRESA,
								Constantes.Permiso.Reserva.GESTION_RESERVA_SEDE,
								Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION)));

		retVal.setMostrarCancelarPorUsuario(
				model.getReservaEstado().getNombre().equalsIgnoreCase(Constantes.ReservaEstado.APROBADA)
						&& model.getUsuarioCreacion().getId().equals(SecurityUtil.getCurrentUserId())
						&& (SecurityUtil.hasAnyAuthority(Constantes.Permiso.Reserva.GESTION_RESERVA_PROPIA)));

		retVal.setMostrarCancelarPorEmpresa(
				model.getReservaEstado().getNombre().equalsIgnoreCase(Constantes.ReservaEstado.APROBADA)
						&& (SecurityUtil.hasAnyAuthority(Constantes.Permiso.GESTION_GLOBAL,
								Constantes.Permiso.Reserva.GESTION_RESERVA_GLOBAL,
								Constantes.Permiso.Reserva.GESTION_RESERVA_EMPRESA,
								Constantes.Permiso.Reserva.GESTION_RESERVA_SEDE,
								Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION)));

		retVal.setMostrarCompletar(
				model.getReservaEstado().getNombre().equalsIgnoreCase(Constantes.ReservaEstado.APROBADA)
						&& (SecurityUtil.hasAnyAuthority(Constantes.Permiso.GESTION_GLOBAL,
								Constantes.Permiso.Reserva.GESTION_RESERVA_GLOBAL,
								Constantes.Permiso.Reserva.GESTION_RESERVA_EMPRESA,
								Constantes.Permiso.Reserva.GESTION_RESERVA_SEDE,
								Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION)));

		retVal.setMostrarIncompletar(
				model.getReservaEstado().getNombre().equalsIgnoreCase(Constantes.ReservaEstado.APROBADA)
						&& (SecurityUtil.hasAnyAuthority(Constantes.Permiso.GESTION_GLOBAL,
								Constantes.Permiso.Reserva.GESTION_RESERVA_GLOBAL,
								Constantes.Permiso.Reserva.GESTION_RESERVA_EMPRESA,
								Constantes.Permiso.Reserva.GESTION_RESERVA_SEDE,
								Constantes.Permiso.Reserva.GESTION_RESERVA_INSTALACION)));

		retVal.setMostrarSancionar(
				(model.getReservaEstado().getNombre().equalsIgnoreCase(Constantes.ReservaEstado.CANCELADA_POR_USUARIO)
						|| model.getReservaEstado().getNombre().equalsIgnoreCase(Constantes.ReservaEstado.COMPLETADA)
						|| model.getReservaEstado().getNombre().equalsIgnoreCase(Constantes.ReservaEstado.INCOMPLETADA))
						&& (SecurityUtil.hasAnyAuthority(Constantes.Permiso.GESTION_GLOBAL,
								Constantes.Permiso.Sancion.GESTION_SANCION_GLOBAL,
								Constantes.Permiso.Sancion.GESTION_SANCION_EMPRESA,
								Constantes.Permiso.Sancion.GESTION_SANCION_SEDE,
								Constantes.Permiso.Sancion.GESTION_SANCION_INSTALACION)));

		String estadoCss;
		switch (model.getReservaEstado().getNombre()) {
		case Constantes.ReservaEstado.APROBADA:
			estadoCss = "bg-success-subtle text-success border-success-subtle";
			break;

		case Constantes.ReservaEstado.PENDIENTE:
			estadoCss = "bg-warning-subtle text-warning border-warning-subtle";
			break;

		case Constantes.ReservaEstado.CANCELADA_POR_USUARIO:
		case Constantes.ReservaEstado.CANCELADA_POR_EMPRESA:
			estadoCss = "bg-danger-subtle text-danger border-danger-subtle";
			break;

		case Constantes.ReservaEstado.COMPLETADA:
			estadoCss = "bg-primary-subtle text-primary border-primary-subtle";
			break;

		case Constantes.ReservaEstado.INCOMPLETADA:
			estadoCss = "bg-dark-subtle text-dark border-dark-subtle";
			break;

		case Constantes.ReservaEstado.DENEGADA:
			estadoCss = "bg-secondary-subtle text-secondary border-secondary-subtle";
			break;

		default:
			estadoCss = "bg-light text-dark border";
			break;
		}

		retVal.setReservaEstadoCss(estadoCss);

		return retVal;
	}

	public Page<ReservaListadoDTO> pageToPageReservaListadoDTO(Page<Reserva> page) {
		return new PageImpl<ReservaListadoDTO>(listModelToListReservaListadoDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

}
