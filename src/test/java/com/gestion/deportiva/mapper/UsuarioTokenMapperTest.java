package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.UsuarioTokenDTO;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioToken;

class UsuarioTokenMapperTest {

	private UsuarioTokenMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new UsuarioTokenMapper();
	}

	@Test
	void modelToDTO() {
		Usuario usuario = new Usuario();
		usuario.setId(10L);
		usuario.setNombre("Elena Rivas");

		UsuarioToken model = new UsuarioToken();
		model.setId(1L);
		model.setUuid("uuid-token-123");
		model.setUsuario(usuario);

		UsuarioTokenDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getUuid()).isEqualTo("uuid-token-123");
		assertThat(dto.getUsuarioId()).isEqualTo(10L);
		assertThat(dto.getUsuarioNombre()).isEqualTo("Elena Rivas");
	}

	@Test
	void listModelToListDTO() {
		Usuario usuario = new Usuario();
		usuario.setNombre("Usuario");

		UsuarioToken model = new UsuarioToken();
		model.setId(1L);
		model.setUsuario(usuario);

		List<UsuarioTokenDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void pageToPageDTO() {
		Usuario usuario = new Usuario();
		usuario.setNombre("Usuario");

		UsuarioToken model = new UsuarioToken();
		model.setId(1L);
		model.setUsuario(usuario);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<UsuarioToken> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<UsuarioTokenDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getId()).isEqualTo(1L);
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		UsuarioTokenDTO dto = new UsuarioTokenDTO();
		dto.setId(5L);
		dto.setUuid("uuid-new");
		dto.setUsuarioId(2L);

		UsuarioToken model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getUsuario()).isNotNull();
		assertThat(model.getUsuario().getId()).isEqualTo(2L);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		UsuarioToken model = new UsuarioToken();
		model.setId(1L);
		model.setUuid("uuid-old");

		UsuarioTokenDTO dto = new UsuarioTokenDTO();
		dto.setId(2L);
		dto.setUuid("uuid-updated");
		dto.setUsuarioId(6L);

		UsuarioToken resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getUsuario().getId()).isEqualTo(6L);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		UsuarioToken model = new UsuarioToken();
		model.setUuid("uuid-original");

		UsuarioTokenDTO dto = new UsuarioTokenDTO();
		dto.setUuid("");
		dto.setUsuarioId(1L);

		UsuarioToken resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		Usuario usuario = new Usuario();
		usuario.setNombre("Carlos Sainz");

		UsuarioToken model = new UsuarioToken();
		model.setId(3L);
		model.setUuid("uuid-token-abc");
		model.setUsuario(usuario);

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(3L);
		assertThat(combos.get(0).getValue()).isEqualTo("Carlos Sainz - uuid-token-abc");
	}
}