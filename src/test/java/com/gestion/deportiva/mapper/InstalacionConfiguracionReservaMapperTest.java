package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionConfiguracionReservaDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionConfiguracionReserva;
import com.gestion.deportiva.model.Sede;

class InstalacionConfiguracionReservaMapperTest {

	private InstalacionConfiguracionReservaMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new InstalacionConfiguracionReservaMapper();
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

		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setId(10L);
		model.setUuid("uuid-config-10");
		model.setInstalacion(instalacion);
		model.setDuracionMax(120L);
		model.setDuracionMin(30L);
		model.setIntervaloHorario(60L);

		InstalacionConfiguracionReservaDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(10L);
		assertThat(dto.getUuid()).isEqualTo("uuid-config-10");
		assertThat(dto.getEmpresaId()).isEqualTo(1L);
		assertThat(dto.getEmpresaNombre()).isEqualTo("Empresa Central");
		assertThat(dto.getSedeId()).isEqualTo(2L);
		assertThat(dto.getSedeNombre()).isEqualTo("Sede Norte");
		assertThat(dto.getInstalacionId()).isEqualTo(3L);
		assertThat(dto.getInstalacionNombre()).isEqualTo("Pista de Tenis");
		assertThat(dto.getDuracionMax()).isEqualTo(120);
		assertThat(dto.getDuracionMin()).isEqualTo(30);
		assertThat(dto.getIntervaloHorario()).isEqualTo(60);
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
		instalacion.setNombre("Instalacion 1");
		instalacion.setSede(sede);

		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setId(1L);
		model.setInstalacion(instalacion);

		List<InstalacionConfiguracionReservaDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getInstalacionNombre()).isEqualTo("Instalacion 1");
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
		instalacion.setNombre("Instalacion Page");
		instalacion.setSede(sede);

		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setId(1L);
		model.setInstalacion(instalacion);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<InstalacionConfiguracionReserva> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<InstalacionConfiguracionReservaDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getInstalacionNombre()).isEqualTo("Instalacion Page");
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		InstalacionConfiguracionReservaDTO dto = new InstalacionConfiguracionReservaDTO();
		dto.setId(15L);
		dto.setUuid("uuid-new");
		dto.setInstalacionId(5L);
		dto.setDuracionMax(90L);
		dto.setDuracionMin(15L);
		dto.setIntervaloHorario(30L);

		InstalacionConfiguracionReserva model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(15L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getInstalacion()).isNotNull();
		assertThat(model.getInstalacion().getId()).isEqualTo(5L);
		assertThat(model.getDuracionMax()).isEqualTo(90);
		assertThat(model.getDuracionMin()).isEqualTo(15);
		assertThat(model.getIntervaloHorario()).isEqualTo(30);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setId(10L);
		model.setUuid("uuid-old");
		model.setDuracionMax(60L);

		InstalacionConfiguracionReservaDTO dto = new InstalacionConfiguracionReservaDTO();
		dto.setId(11L);
		dto.setUuid("uuid-updated");
		dto.setInstalacionId(8L);
		dto.setDuracionMax(120L);

		InstalacionConfiguracionReserva resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(11L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getInstalacion().getId()).isEqualTo(8L);
		assertThat(resultado.getDuracionMax()).isEqualTo(120);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setUuid("uuid-original");

		InstalacionConfiguracionReservaDTO dto = new InstalacionConfiguracionReservaDTO();
		dto.setUuid("");
		dto.setInstalacionId(1L);

		InstalacionConfiguracionReserva resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		Instalacion instalacion = new Instalacion();
		instalacion.setId(1L);
		instalacion.setNombre("Campo de Fútbol");

		InstalacionConfiguracionReserva model = new InstalacionConfiguracionReserva();
		model.setId(1L);
		model.setInstalacion(instalacion);

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);

	}
}