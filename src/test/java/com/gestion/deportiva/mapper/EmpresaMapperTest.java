package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.dto.EmpresaRegistroDTO;
import com.gestion.deportiva.model.Empresa;

class EmpresaMapperTest {

	private EmpresaMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new EmpresaMapper();
	}

	@Test
	void modelToDTO() {
		Empresa model = new Empresa();
		model.setId(1L);
		model.setNombre("Empresa Test");
		model.setUuid("uuid-empresa");
		model.setEmail("test@empresa.com");
		model.setUrl("https://empresa.com");
		model.setLogo("logo.png");
		model.setDescripcion("Descripción de prueba");
		model.setCif("B12345678");

		EmpresaDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getNombre()).isEqualTo("Empresa Test");
		assertThat(dto.getUuid()).isEqualTo("uuid-empresa");
		assertThat(dto.getEmail()).isEqualTo("test@empresa.com");
		assertThat(dto.getUrl()).isEqualTo("https://empresa.com");
		assertThat(dto.getLogoUrl()).isEqualTo("logo.png");
		assertThat(dto.getDescripcion()).isEqualTo("Descripción de prueba");
		assertThat(dto.getCif()).isEqualTo("B12345678");
	}

	@Test
	void listModelToListDTO() {
		Empresa model1 = new Empresa();
		model1.setId(1L);
		model1.setNombre("Empresa 1");

		Empresa model2 = new Empresa();
		model2.setId(2L);
		model2.setNombre("Empresa 2");

		List<EmpresaDTO> dtos = mapper.listModelToListDTO(List.of(model1, model2));

		assertThat(dtos).hasSize(2);
		assertThat(dtos.get(0).getNombre()).isEqualTo("Empresa 1");
		assertThat(dtos.get(1).getNombre()).isEqualTo("Empresa 2");
	}

	@Test
	void pageToPageDTO() {
		Empresa model = new Empresa();
		model.setId(1L);
		model.setNombre("Empresa Page");

		PageRequest pageable = PageRequest.of(0, 10);
		Page<Empresa> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<EmpresaDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getNombre()).isEqualTo("Empresa Page");
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		EmpresaDTO dto = new EmpresaDTO();
		dto.setId(10L);
		dto.setUuid("uuid-nuevo");
		dto.setNombre("Nueva Empresa");
		dto.setDescripcion("Desc");
		dto.setEmail("nuevo@empresa.com");
		dto.setUrl("https://nueva.com");
		dto.setLogoUrl("logo-nuevo.png");
		dto.setCif("A87654321");

		Empresa model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(10L);
		assertThat(model.getUuid()).isEqualTo("uuid-nuevo");
		assertThat(model.getNombre()).isEqualTo("Nueva Empresa");
		assertThat(model.getDescripcion()).isEqualTo("Desc");
		assertThat(model.getEmail()).isEqualTo("nuevo@empresa.com");
		assertThat(model.getUrl()).isEqualTo("https://nueva.com");
		assertThat(model.getLogo()).isEqualTo("logo-nuevo.png");
		assertThat(model.getCif()).isEqualTo("A87654321");
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		Empresa model = new Empresa();
		model.setId(5L);
		model.setUuid("uuid-antiguo");
		model.setNombre("Antiguo Nombre");

		EmpresaDTO dto = new EmpresaDTO();
		dto.setId(6L);
		dto.setUuid("uuid-actualizado");
		dto.setNombre("Nombre Actualizado");
		dto.setLogoUrl("logo-updated.png");

		Empresa resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(6L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-actualizado");
		assertThat(resultado.getNombre()).isEqualTo("Nombre Actualizado");
		assertThat(resultado.getLogo()).isEqualTo("logo-updated.png");
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		Empresa model = new Empresa();
		model.setUuid("uuid-original");

		EmpresaDTO dto = new EmpresaDTO();
		dto.setUuid("");

		Empresa resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		Empresa model = new Empresa();
		model.setId(1L);
		model.setNombre("Empresa Combo");

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(1L);
	}

	@Test
	void registroEmpresaDTOToModel() {
		EmpresaRegistroDTO dto = new EmpresaRegistroDTO();
		dto.setNombre("Empresa Registro");
		dto.setDescripcion("Reg Desc");
		dto.setEmail("reg@empresa.com");
		dto.setUrl("https://reg.com");
		dto.setLogo("reg-logo.png");
		dto.setCif("B98765432");

		Empresa model = mapper.registroEmpresaDTOToModel(dto);

		assertThat(model).isNotNull();
		assertThat(model.getNombre()).isEqualTo("Empresa Registro");
		assertThat(model.getDescripcion()).isEqualTo("Reg Desc");
		assertThat(model.getEmail()).isEqualTo("reg@empresa.com");
		assertThat(model.getUrl()).isEqualTo("https://reg.com");
		assertThat(model.getLogo()).isEqualTo("reg-logo.png");
		assertThat(model.getCif()).isEqualTo("B98765432");
	}
}