package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ComunidadAutonomaDTO;
import com.gestion.deportiva.model.ComunidadAutonoma;

class ComunidadAutonomaMapperTest {

	private ComunidadAutonomaMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ComunidadAutonomaMapper();
	}

	@Test
	void modelToDTO() {
		ComunidadAutonoma model = new ComunidadAutonoma();
		model.setId(1L);
		model.setNombre("Comunidad de Madrid");
		model.setUuid("uuid-madrid");
		model.setCodigoIne(13L);

		ComunidadAutonomaDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getNombre()).isEqualTo("Comunidad de Madrid");
		assertThat(dto.getUuid()).isEqualTo("uuid-madrid");
		assertThat(dto.getCodigoIne()).isEqualTo(13L);
	}

	@Test
	void listModelToListDTO() {
		ComunidadAutonoma model1 = new ComunidadAutonoma();
		model1.setId(1L);
		model1.setNombre("Madrid");

		ComunidadAutonoma model2 = new ComunidadAutonoma();
		model2.setId(2L);
		model2.setNombre("Cataluña");

		List<ComunidadAutonomaDTO> dtos = mapper.listModelToListDTO(List.of(model1, model2));

		assertThat(dtos).hasSize(2);
		assertThat(dtos.get(0).getNombre()).isEqualTo("Madrid");
		assertThat(dtos.get(1).getNombre()).isEqualTo("Cataluña");
	}

	@Test
	void pageToPageDTO() {
		ComunidadAutonoma model = new ComunidadAutonoma();
		model.setId(1L);
		model.setNombre("Andalucía");

		PageRequest pageable = PageRequest.of(0, 10);
		Page<ComunidadAutonoma> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<ComunidadAutonomaDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getNombre()).isEqualTo("Andalucía");
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		ComunidadAutonomaDTO dto = new ComunidadAutonomaDTO();
		dto.setId(10L);
		dto.setUuid("uuid-nuevo");
		dto.setNombre("Galicia");
		dto.setCodigoIne(12L);

		ComunidadAutonoma model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(10L);
		assertThat(model.getUuid()).isEqualTo("uuid-nuevo");
		assertThat(model.getNombre()).isEqualTo("Galicia");
		assertThat(model.getCodigoIne()).isEqualTo(12L);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		ComunidadAutonoma model = new ComunidadAutonoma();
		model.setId(5L);
		model.setUuid("uuid-antiguo");
		model.setNombre("Nombre Antiguo");
		model.setCodigoIne(1L);

		ComunidadAutonomaDTO dto = new ComunidadAutonomaDTO();
		dto.setId(6L);
		dto.setUuid("uuid-actualizado");
		dto.setNombre("Nombre Nuevo");
		dto.setCodigoIne(2L);

		ComunidadAutonoma resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(6L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-actualizado");
		assertThat(resultado.getNombre()).isEqualTo("Nombre Nuevo");
		assertThat(resultado.getCodigoIne()).isEqualTo(2L);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		ComunidadAutonoma model = new ComunidadAutonoma();
		model.setUuid("uuid-original");

		ComunidadAutonomaDTO dto = new ComunidadAutonomaDTO();
		dto.setUuid("");

		ComunidadAutonoma resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		ComunidadAutonoma model1 = new ComunidadAutonoma();
		model1.setId(1L);
		model1.setNombre("Aragón");

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model1));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(1L);
		assertThat(combos.get(0).getValue()).isEqualTo("Aragón");
	}
}