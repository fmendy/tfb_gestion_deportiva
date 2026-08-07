package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.RolDTO;
import com.gestion.deportiva.model.Rol;

class RolMapperTest {

	private RolMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new RolMapper();
	}

	@Test
	void modelToDTO() {
		Rol model = new Rol();
		model.setId(1L);
		model.setNombre("ADMINISTRADOR");
		model.setUuid("uuid-rol-admin");

		RolDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getNombre()).isEqualTo("ADMINISTRADOR");
		assertThat(dto.getUuid()).isEqualTo("uuid-rol-admin");
	}

	@Test
	void listModelToListDTO() {
		Rol model1 = new Rol();
		model1.setId(1L);
		model1.setNombre("ADMIN");

		Rol model2 = new Rol();
		model2.setId(2L);
		model2.setNombre("USER");

		List<RolDTO> dtos = mapper.listModelToListDTO(List.of(model1, model2));

		assertThat(dtos).hasSize(2);
		assertThat(dtos.get(0).getNombre()).isEqualTo("ADMIN");
		assertThat(dtos.get(1).getNombre()).isEqualTo("USER");
	}

	@Test
	void pageToPageDTO() {
		Rol model = new Rol();
		model.setId(1L);
		model.setNombre("MODERATOR");

		PageRequest pageable = PageRequest.of(0, 10);
		Page<Rol> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<RolDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getNombre()).isEqualTo("MODERATOR");
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		RolDTO dto = new RolDTO();
		dto.setId(5L);
		dto.setUuid("uuid-new-role");
		dto.setNombre("GESTIOR");

		Rol model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUuid()).isEqualTo("uuid-new-role");
		assertThat(model.getNombre()).isEqualTo("GESTIOR");
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		Rol model = new Rol();
		model.setId(1L);
		model.setUuid("uuid-old");
		model.setNombre("OLD_ROLE");

		RolDTO dto = new RolDTO();
		dto.setId(2L);
		dto.setUuid("uuid-updated");
		dto.setNombre("UPDATED_ROLE");

		Rol resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getNombre()).isEqualTo("UPDATED_ROLE");
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		Rol model = new Rol();
		model.setUuid("uuid-original");

		RolDTO dto = new RolDTO();
		dto.setUuid("");

		Rol resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		Rol model = new Rol();
		model.setId(3L);
		model.setNombre("OPERADOR");

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(3L);
		assertThat(combos.get(0).getValue()).isEqualTo("OPERADOR");
	}
}