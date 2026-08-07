package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionHorarioDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionHorario;
import com.gestion.deportiva.model.Sede;

class InstalacionHorarioMapperTest {

	private InstalacionHorarioMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new InstalacionHorarioMapper();
	}

	@Test
	void modelToDTO() {
		Empresa empresa = new Empresa();
		empresa.setId(1L);
		empresa.setNombre("Empresa Central");

		Sede sede = new Sede();
		sede.setId(2L);
		sede.setNombre("Sede Norte");
		sede.setEmpresa(empresa);

		Instalacion instalacion = new Instalacion();
		instalacion.setId(3L);
		instalacion.setNombre("Pista de Tenis");
		instalacion.setSede(sede);

		LocalTime horaInicio = LocalTime.of(8, 0);
		LocalTime horaFin = LocalTime.of(22, 0);

		InstalacionHorario model = new InstalacionHorario();
		model.setId(10L);
		model.setUuid("uuid-horario-10");
		model.setDiaSemana(1L);
		model.setInstalacion(instalacion);
		model.setHoraInicio(horaInicio);
		model.setHoraFin(horaFin);

		InstalacionHorarioDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(10L);
		assertThat(dto.getUuid()).isEqualTo("uuid-horario-10");
		assertThat(dto.getDiaSemana()).isEqualTo(1);
		assertThat(dto.getEmpresaId()).isEqualTo(1L);
		assertThat(dto.getEmpresaNombre()).isEqualTo("Empresa Central");
		assertThat(dto.getSedeId()).isEqualTo(2L);
		assertThat(dto.getSedeNombre()).isEqualTo("Sede Norte");
		assertThat(dto.getInstalacionId()).isEqualTo(3L);
		assertThat(dto.getInstalacionNombre()).isEqualTo("Pista de Tenis");
		assertThat(dto.getHoraInicio()).isEqualTo(horaInicio);
		assertThat(dto.getHoraFin()).isEqualTo(horaFin);
	}

	@Test
	void listModelToListDTO() {
		Empresa empresa = new Empresa();
		empresa.setId(1L);
		empresa.setNombre("Empresa");

		Sede sede = new Sede();
		sede.setId(1L);
		sede.setNombre("Sede");
		sede.setEmpresa(empresa);

		Instalacion instalacion = new Instalacion();
		instalacion.setId(1L);
		instalacion.setNombre("Instalacion");
		instalacion.setSede(sede);

		InstalacionHorario model = new InstalacionHorario();
		model.setId(1L);
		model.setInstalacion(instalacion);

		List<InstalacionHorarioDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void pageToPageDTO() {
		Empresa empresa = new Empresa();
		empresa.setId(1L);
		empresa.setNombre("Empresa");

		Sede sede = new Sede();
		sede.setId(1L);
		sede.setNombre("Sede");
		sede.setEmpresa(empresa);

		Instalacion instalacion = new Instalacion();
		instalacion.setId(1L);
		instalacion.setNombre("Instalacion");
		instalacion.setSede(sede);

		InstalacionHorario model = new InstalacionHorario();
		model.setId(1L);
		model.setInstalacion(instalacion);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<InstalacionHorario> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<InstalacionHorarioDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getId()).isEqualTo(1L);
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		LocalTime horaInicio = LocalTime.of(9, 0);
		LocalTime horaFin = LocalTime.of(21, 0);

		InstalacionHorarioDTO dto = new InstalacionHorarioDTO();
		dto.setId(5L);
		dto.setUuid("uuid-new");
		dto.setDiaSemana(2L);
		dto.setInstalacionId(4L);
		dto.setHoraInicio(horaInicio);
		dto.setHoraFin(horaFin);

		InstalacionHorario model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getDiaSemana()).isEqualTo(2);
		assertThat(model.getInstalacion()).isNotNull();
		assertThat(model.getInstalacion().getId()).isEqualTo(4L);
		assertThat(model.getHoraInicio()).isEqualTo(horaInicio);
		assertThat(model.getHoraFin()).isEqualTo(horaFin);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		InstalacionHorario model = new InstalacionHorario();
		model.setId(1L);
		model.setUuid("uuid-old");

		InstalacionHorarioDTO dto = new InstalacionHorarioDTO();
		dto.setId(2L);
		dto.setUuid("uuid-updated");
		dto.setDiaSemana(3L);
		dto.setInstalacionId(6L);

		InstalacionHorario resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getDiaSemana()).isEqualTo(3);
		assertThat(resultado.getInstalacion().getId()).isEqualTo(6L);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		InstalacionHorario model = new InstalacionHorario();
		model.setUuid("uuid-original");

		InstalacionHorarioDTO dto = new InstalacionHorarioDTO();
		dto.setUuid("");
		dto.setInstalacionId(1L);

		InstalacionHorario resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		InstalacionHorario model = new InstalacionHorario();
		model.setId(1L);
		model.setHoraFin(LocalTime.of(20, 0));

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(1L);
		assertThat(combos.get(0).getValue()).isEqualTo("20:00 - 20:00");
	}
}