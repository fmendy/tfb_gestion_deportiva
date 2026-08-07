package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.UsuarioSedeDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioSede;

class UsuarioSedeMapperTest {

	private UsuarioSedeMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new UsuarioSedeMapper();
	}

	@Test
	void modelToDTO() {
		Empresa empresa = new Empresa();
		empresa.setId(1L);
		empresa.setNombre("Empresa Global");

		Sede sede = new Sede();
		sede.setId(2L);
		sede.setNombre("Sede Sur");
		sede.setEmpresa(empresa);

		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNombre("Lucía Gómez");

		UsuarioSede model = new UsuarioSede();
		model.setId(15L);
		model.setUuid("uuid-usuario-sede-15");
		model.setSede(sede);
		model.setUsuario(usuario);

		UsuarioSedeDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(15L);
		assertThat(dto.getUuid()).isEqualTo("uuid-usuario-sede-15");
		assertThat(dto.getEmpresaId()).isEqualTo(1L);
		assertThat(dto.getEmpresaNombre()).isEqualTo("Empresa Global");
		assertThat(dto.getSedeId()).isEqualTo(2L);
		assertThat(dto.getSedeNombre()).isEqualTo("Sede Sur");
		assertThat(dto.getUsuarioId()).isEqualTo(3L);
		assertThat(dto.getUsuarioNombre()).isEqualTo("Lucía Gómez");
	}

	@Test
	void listModelToListDTO() {
		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa");

		Sede sede = new Sede();
		sede.setNombre("Sede");
		sede.setEmpresa(empresa);

		Usuario usuario = new Usuario();
		usuario.setNombre("Usuario");

		UsuarioSede model = new UsuarioSede();
		model.setId(1L);
		model.setSede(sede);
		model.setUsuario(usuario);

		List<UsuarioSedeDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void pageToPageDTO() {
		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa");

		Sede sede = new Sede();
		sede.setNombre("Sede");
		sede.setEmpresa(empresa);

		Usuario usuario = new Usuario();
		usuario.setNombre("Usuario");

		UsuarioSede model = new UsuarioSede();
		model.setId(1L);
		model.setSede(sede);
		model.setUsuario(usuario);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<UsuarioSede> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<UsuarioSedeDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getId()).isEqualTo(1L);
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		UsuarioSedeDTO dto = new UsuarioSedeDTO();
		dto.setId(5L);
		dto.setUuid("uuid-new");
		dto.setSedeId(2L);
		dto.setUsuarioId(3L);

		UsuarioSede model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getSede()).isNotNull();
		assertThat(model.getSede().getId()).isEqualTo(2L);
		assertThat(model.getUsuario()).isNotNull();
		assertThat(model.getUsuario().getId()).isEqualTo(3L);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		UsuarioSede model = new UsuarioSede();
		model.setId(1L);
		model.setUuid("uuid-old");

		UsuarioSedeDTO dto = new UsuarioSedeDTO();
		dto.setId(2L);
		dto.setUuid("uuid-updated");
		dto.setSedeId(6L);
		dto.setUsuarioId(7L);

		UsuarioSede resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getSede().getId()).isEqualTo(6L);
		assertThat(resultado.getUsuario().getId()).isEqualTo(7L);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		UsuarioSede model = new UsuarioSede();
		model.setUuid("uuid-original");

		UsuarioSedeDTO dto = new UsuarioSedeDTO();
		dto.setUuid("");
		dto.setSedeId(1L);
		dto.setUsuarioId(1L);

		UsuarioSede resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		Sede sede = new Sede();
		sede.setNombre("Sede Principal");

		Usuario usuario = new Usuario();
		usuario.setNombre("Mario Bros");

		UsuarioSede model = new UsuarioSede();
		model.setId(3L);
		model.setSede(sede);
		model.setUsuario(usuario);

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(3L);
		assertThat(combos.get(0).getValue()).isEqualTo("Mario Bros - Sede Principal");
	}
}