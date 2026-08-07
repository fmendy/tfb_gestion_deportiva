package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.UsuarioEmpresaDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioEmpresa;

class UsuarioEmpresaMapperTest {

	private UsuarioEmpresaMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new UsuarioEmpresaMapper();
	}

	@Test
	void modelToDTO() {
		Empresa empresa = new Empresa();
		empresa.setId(10L);
		empresa.setNombre("Empresa Deportiva");

		Usuario usuario = new Usuario();
		usuario.setId(20L);
		usuario.setNombre("Carlos Gómez");

		UsuarioEmpresa model = new UsuarioEmpresa();
		model.setId(1L);
		model.setUuid("uuid-usuario-empresa-1");
		model.setEmpresa(empresa);
		model.setUsuario(usuario);

		UsuarioEmpresaDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getUuid()).isEqualTo("uuid-usuario-empresa-1");
		assertThat(dto.getEmpresaId()).isEqualTo(10L);
		assertThat(dto.getEmpresaNombre()).isEqualTo("Empresa Deportiva");
		assertThat(dto.getUsuarioId()).isEqualTo(20L);
		assertThat(dto.getUsuarioNombre()).isEqualTo("Carlos Gómez");
	}

	@Test
	void listModelToListDTO() {
		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa");

		Usuario usuario = new Usuario();
		usuario.setNombre("Usuario");

		UsuarioEmpresa model = new UsuarioEmpresa();
		model.setId(1L);
		model.setEmpresa(empresa);
		model.setUsuario(usuario);

		List<UsuarioEmpresaDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void pageToPageDTO() {
		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa");

		Usuario usuario = new Usuario();
		usuario.setNombre("Usuario");

		UsuarioEmpresa model = new UsuarioEmpresa();
		model.setId(1L);
		model.setEmpresa(empresa);
		model.setUsuario(usuario);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<UsuarioEmpresa> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<UsuarioEmpresaDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getId()).isEqualTo(1L);
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		UsuarioEmpresaDTO dto = new UsuarioEmpresaDTO();
		dto.setId(5L);
		dto.setUuid("uuid-new");
		dto.setEmpresaId(2L);
		dto.setUsuarioId(3L);

		UsuarioEmpresa model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getEmpresa()).isNotNull();
		assertThat(model.getEmpresa().getId()).isEqualTo(2L);
		assertThat(model.getUsuario()).isNotNull();
		assertThat(model.getUsuario().getId()).isEqualTo(3L);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		UsuarioEmpresa model = new UsuarioEmpresa();
		model.setId(1L);
		model.setUuid("uuid-old");

		UsuarioEmpresaDTO dto = new UsuarioEmpresaDTO();
		dto.setId(2L);
		dto.setUuid("uuid-updated");
		dto.setEmpresaId(6L);
		dto.setUsuarioId(7L);

		UsuarioEmpresa resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getEmpresa().getId()).isEqualTo(6L);
		assertThat(resultado.getUsuario().getId()).isEqualTo(7L);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		UsuarioEmpresa model = new UsuarioEmpresa();
		model.setUuid("uuid-original");

		UsuarioEmpresaDTO dto = new UsuarioEmpresaDTO();
		dto.setUuid("");
		dto.setEmpresaId(1L);
		dto.setUsuarioId(1L);

		UsuarioEmpresa resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		Empresa empresa = new Empresa();
		empresa.setNombre("Club Deportivo");

		Usuario usuario = new Usuario();
		usuario.setNombre("Ana López");

		UsuarioEmpresa model = new UsuarioEmpresa();
		model.setId(3L);
		model.setEmpresa(empresa);
		model.setUsuario(usuario);

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(3L);
		assertThat(combos.get(0).getValue()).isEqualTo("Ana López - Club Deportivo");
	}
}