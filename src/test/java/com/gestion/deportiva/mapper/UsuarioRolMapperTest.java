package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.UsuarioRolDTO;
import com.gestion.deportiva.model.Rol;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioRol;

class UsuarioRolMapperTest {

	private UsuarioRolMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new UsuarioRolMapper();
	}

	@Test
	void modelToDTO() {
		Rol rol = new Rol();
		rol.setId(1L);
		rol.setNombre("ADMIN");
		rol.setUuid("uuid-rol-1");

		Usuario usuario = new Usuario();
		usuario.setId(2L);
		usuario.setNombre("Maria Perez");
		usuario.setUuid("uuid-usuario-2");

		UsuarioRol model = new UsuarioRol();
		model.setId(10L);
		model.setUuid("uuid-usuariorol-10");
		model.setRol(rol);
		model.setUsuario(usuario);

		UsuarioRolDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(10L);
		assertThat(dto.getUuid()).isEqualTo("uuid-usuariorol-10");
		assertThat(dto.getRolId()).isEqualTo(1L);
		assertThat(dto.getRolNombre()).isEqualTo("ADMIN");
		assertThat(dto.getRolUuid()).isEqualTo("uuid-rol-1");
		assertThat(dto.getUsuarioId()).isEqualTo(2L);
		assertThat(dto.getUsuarioNombre()).isEqualTo("Maria Perez");
		assertThat(dto.getUsuarioUuid()).isEqualTo("uuid-usuario-2");
	}

	@Test
	void listModelToListDTO() {
		Rol rol = new Rol();
		rol.setNombre("USER");

		Usuario usuario = new Usuario();
		usuario.setNombre("Juan");

		UsuarioRol model = new UsuarioRol();
		model.setId(1L);
		model.setRol(rol);
		model.setUsuario(usuario);

		List<UsuarioRolDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void pageToPageDTO() {
		Rol rol = new Rol();
		rol.setNombre("USER");

		Usuario usuario = new Usuario();
		usuario.setNombre("Juan");

		UsuarioRol model = new UsuarioRol();
		model.setId(1L);
		model.setRol(rol);
		model.setUsuario(usuario);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<UsuarioRol> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<UsuarioRolDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getId()).isEqualTo(1L);
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		UsuarioRolDTO dto = new UsuarioRolDTO();
		dto.setId(5L);
		dto.setUsuarioId(3L);
		dto.setRolId(4L);

		UsuarioRol model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUsuario()).isNotNull();
		assertThat(model.getUsuario().getId()).isEqualTo(3L);
		assertThat(model.getRol()).isNotNull();
		assertThat(model.getRol().getId()).isEqualTo(4L);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		UsuarioRol model = new UsuarioRol();
		model.setId(1L);

		UsuarioRolDTO dto = new UsuarioRolDTO();
		dto.setId(2L);
		dto.setUsuarioId(7L);
		dto.setRolId(8L);

		UsuarioRol resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUsuario().getId()).isEqualTo(7L);
		assertThat(resultado.getRol().getId()).isEqualTo(8L);
	}

	@Test
	void listModelToListComboDTO() {
		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of());

		assertThat(combos).isNull();
	}
}