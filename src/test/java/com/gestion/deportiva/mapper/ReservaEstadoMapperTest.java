package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ReservaEstadoDTO;
import com.gestion.deportiva.model.ReservaEstado;

class ReservaEstadoMapperTest {

	private ReservaEstadoMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ReservaEstadoMapper();
	}

	@Test
	void modelToDTO() {
		ReservaEstado model = new ReservaEstado();
		model.setId(1L);
		model.setNombre("CONFIRMADA");
		model.setUuid("uuid-estado-confirmada");

		ReservaEstadoDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getNombre()).isEqualTo("CONFIRMADA");
		assertThat(dto.getUuid()).isEqualTo("uuid-estado-confirmada");
	}

	@Test
	void listModelToListDTO() {
		ReservaEstado model1 = new ReservaEstado();
		model1.setId(1L);
		model1.setNombre("PENDIENTE");

		ReservaEstado model2 = new ReservaEstado();
		model2.setId(2L);
		model2.setNombre("CANCELADA");

		List<ReservaEstadoDTO> dtos = mapper.listModelToListDTO(List.of(model1, model2));

		assertThat(dtos).hasSize(2);
		assertThat(dtos.get(1).getNombre()).isEqualTo("CANCELADA");
	}

	@Test
	void pageToPageDTO() {
		ReservaEstado model = new ReservaEstado();
		model.setId(1L);
		model.setNombre("PAGADA");

		PageRequest pageable = PageRequest.of(0, 10);
		Page<ReservaEstado> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<ReservaEstadoDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getNombre()).isEqualTo("PAGADA");
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		ReservaEstadoDTO dto = new ReservaEstadoDTO();
		dto.setId(5L);
		dto.setUuid("uuid-nuevo-estado");
		dto.setNombre("RESERVADA");

		ReservaEstado model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUuid()).isEqualTo("uuid-nuevo-estado");
		assertThat(model.getNombre()).isEqualTo("RESERVADA");
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		ReservaEstado model = new ReservaEstado();
		model.setId(1L);
		model.setUuid("uuid-antiguo");
		model.setNombre("ESTADO_ANTIGUO");

		ReservaEstadoDTO dto = new ReservaEstadoDTO();
		dto.setId(2L);
		dto.setUuid("uuid-actualizado");
		dto.setNombre("ESTADO_ACTUALIZADO");

		ReservaEstado resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-actualizado");
		assertThat(resultado.getNombre()).isEqualTo("ESTADO_ACTUALIZADO");
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		ReservaEstado model = new ReservaEstado();
		model.setUuid("uuid-original");

		ReservaEstadoDTO dto = new ReservaEstadoDTO();
		dto.setUuid("");

		ReservaEstado resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		ReservaEstado model = new ReservaEstado();
		model.setId(3L);
		model.setNombre("FINALIZADA");

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getValue()).isEqualTo("FINALIZADA");
	}
}