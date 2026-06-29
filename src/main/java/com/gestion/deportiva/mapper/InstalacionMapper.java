package com.gestion.deportiva.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.InstalacionHorarioPublicoDTO;
import com.gestion.deportiva.dto.InstalacionPublicoDTO;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionHorario;
import com.gestion.deportiva.model.InstalacionHorarioEspecial;
import com.gestion.deportiva.model.InstalacionTipo;
import com.gestion.deportiva.model.Sede;

@Component
public class InstalacionMapper {

	public InstalacionDTO modelToDTO(Instalacion model) {
		InstalacionDTO retVal = new InstalacionDTO();
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setUuid(model.getUuid());
		retVal.setSedeId(model.getSede().getId());
		retVal.setSedeNombre(model.getSede().getNombre());
		retVal.setInstalacionTipoId(model.getInstalacionTipo().getId());
		retVal.setInstalacionTipoNombre(model.getInstalacionTipo().getNombre());
		retVal.setDescripcion(model.getDescripcion());
		retVal.setEmpresaId(model.getSede().getEmpresa().getId());
		retVal.setEmpresaNombre(model.getSede().getEmpresa().getNombre());
		retVal.setEmpresaSedeInstalacionNombre(model.getSede().getEmpresa().getNombre() + " - "
				+ model.getSede().getNombre() + " - " + model.getNombre());

		return retVal;
	}

	public List<InstalacionDTO> listModelToListDTO(List<Instalacion> list) {
		List<InstalacionDTO> retVal = new ArrayList<>();
		for (Instalacion bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<InstalacionDTO> pageToPageDTO(Page<Instalacion> page) {
		return new PageImpl<InstalacionDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public Instalacion dtoToModel(InstalacionDTO dto, Instalacion model) {
		if (model == null) {
			model = new Instalacion();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
		model.setNombre(dto.getNombre());
		model.setDescripcion(dto.getDescripcion());

		model.setSede(new Sede(dto.getSedeId()));
		model.setInstalacionTipo(new InstalacionTipo(dto.getInstalacionTipoId()));

		return model;
	}

	public List<ComboDTO> listModelToListComboDTO(List<Instalacion> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getNombre())).toList();
	}

	public InstalacionPublicoDTO toPublicDTO(Instalacion instalacion, List<InstalacionHorario> listHorarios,
			List<InstalacionHorarioEspecial> listHorariosEspeciales) {

		InstalacionPublicoDTO dto = new InstalacionPublicoDTO();

		dto.setId(instalacion.getId());
		dto.setNombre(instalacion.getNombre());
		dto.setDescripcion(instalacion.getDescripcion());
		dto.setInstalacionTipoNombre(instalacion.getInstalacionTipo().getNombre());
		dto.setSedeId(instalacion.getSede().getId());
		dto.setSedeNombre(instalacion.getSede().getNombre());
		dto.setSedeDireccion(instalacion.getSede().getDireccion());
		dto.setSedeMunicipioNombre(instalacion.getSede().getMunicipio().getNombre());
		dto.setSedeMunicipioProvinciaNombre(instalacion.getSede().getMunicipio().getProvincia().getNombre());
		dto.setSedeMunicipioProvinciaComunidadAutonomaNombre(
				instalacion.getSede().getMunicipio().getProvincia().getComunidadAutonoma().getNombre());
		dto.setEmpresaNombre(instalacion.getSede().getEmpresa().getNombre());

		// 1️⃣ Horario semanal base
		Map<Long, InstalacionHorario> mapaSemanal = listHorarios.stream()
				.collect(Collectors.toMap(InstalacionHorario::getDiaSemana, h -> h));

		List<InstalacionHorarioPublicoDTO> semanal = listHorarios.stream()
				.map(h -> new InstalacionHorarioPublicoDTO(null, h.getDiaSemana().intValue(), h.getHoraInicio(),
						h.getHoraFin(), false))
				.toList();

		dto.setHorarioSemanal(semanal);

		// 2️⃣ Map especiales por fecha
		Map<LocalDate, InstalacionHorarioEspecial> mapaEspeciales = listHorariosEspeciales.stream()
				.collect(Collectors.toMap(InstalacionHorarioEspecial::getFecha, e -> e));

		// 3️⃣ Generar calendario (ej: próximos 14 días)
		Map<LocalDate, InstalacionHorarioPublicoDTO> resultado = new LinkedHashMap<>();

		LocalDate hoy = LocalDate.now();

		// 👉 Inicio: 2 meses atrás
		LocalDate inicio = hoy.minusMonths(2);

		// 👉 Fin: final del año siguiente
		LocalDate fin = LocalDate.of(hoy.getYear() + 1, 12, 31);

		for (LocalDate fecha = inicio; !fecha.isAfter(fin); fecha = fecha.plusDays(1)) {

			int diaSemana = fecha.getDayOfWeek().getValue();

			if (mapaEspeciales.containsKey(fecha)) {

				InstalacionHorarioEspecial esp = mapaEspeciales.get(fecha);

				resultado.put(fecha, new InstalacionHorarioPublicoDTO(fecha, diaSemana, esp.getHoraInicio(),
						esp.getHoraFin(), esp.getCerrado()));

			} else {

				InstalacionHorario normal = mapaSemanal.get((long) diaSemana);

				if (normal != null) {
					resultado.put(fecha, new InstalacionHorarioPublicoDTO(fecha, diaSemana, normal.getHoraInicio(),
							normal.getHoraFin(), false));
				}
			}
		}

		dto.setHorarioCalculado(resultado);

		return dto;
	}
}
