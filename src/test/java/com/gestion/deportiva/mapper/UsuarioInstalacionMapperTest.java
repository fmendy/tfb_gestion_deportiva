package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.UsuarioInstalacionDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioInstalacion;

class UsuarioInstalacionMapperTest {

	private UsuarioInstalacionMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new UsuarioInstalacionMapper();
	}

	@Test
	void modelToDTO() {
		Empresa empresa = new Empresa();
		empresa.setId(1L);
		empresa.setNombre("Empresa Deportiva");

		Sede sede = new Sede();
		sede.setId(2L);
		sede.setNombre("Sede Central");
		sede.setEmpresa(empresa);

		Instalacion instalacion = new Instalacion();
		instalacion.setId(3L);
		instalacion.setNombre("Pista de Tenis");
		instalacion.setSede(sede);

		Usuario usuario = new Usuario();
		usuario.setId(4L);
		usuario.setNombre("Laura Martínez");

		UsuarioInstalacion model = new UsuarioInstalacion();
		model.setId(10L);
		model.setUuid("uuid-usuario-instalacion-1");
		model.setInstalacion(instalacion);
		model.setUsuario(usuario);

		UsuarioInstalacionDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(10L);
		assertThat(dto.getUuid()).isEqualTo("uuid-usuario-instalacion-1");
		assertThat(dto.getEmpresaId()).isEqualTo(1L);
		assertThat(dto.getEmpresaNombre()).isEqualTo("Empresa Deportiva");
		assertThat(dto.getSedeId()).isEqualTo(2L);
		assertThat(dto.getSedeNombre()).isEqualTo("Sede Central");
		assertThat(dto.getInstalacionId()).isEqualTo(3L);
		assertThat(dto.getInstalacionNombre()).isEqualTo("Pista de Tenis");
		assertThat(dto.getUsuarioId()).isEqualTo(4L);
		assertThat(dto.getUsuarioNombre()).isEqualTo("Laura Martínez");
	}

	@Test
	void listModelToListDTO() {
		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa");

		Sede sede = new Sede();
		sede.setNombre("Sede");
		sede.setEmpresa(empresa);

		Instalacion instalacion = new Instalacion();
		instalacion.setNombre("Instalacion");
		instalacion.setSede(sede);

		Usuario usuario = new Usuario();
		usuario.setNombre("Usuario");

		UsuarioInstalacion model = new UsuarioInstalacion();
		model.setId(1L);
		model.setInstalacion(instalacion);
		model.setUsuario(usuario);

		List<UsuarioInstalacionDTO> dtos = mapper.listModelToListDTO(List.of(model));

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

		Instalacion instalacion = new Instalacion();
		instalacion.setNombre("Instalacion");
		instalacion.setSede(sede);

		Usuario usuario = new Usuario();
		usuario.setNombre("Usuario");

		UsuarioInstalacion model = new UsuarioInstalacion();
		model.setId(1L);
		model.setInstalacion(instalacion);
		model.setUsuario(usuario);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<UsuarioInstalacion> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<UsuarioInstalacionDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getId()).isEqualTo(1L);
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		UsuarioInstalacionDTO dto = new UsuarioInstalacionDTO();
		dto.setId(5L);
		dto.setUuid("uuid-new");
		dto.setInstalacionId(2L);
		dto.setUsuarioId(3L);

		UsuarioInstalacion model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getInstalacion()).isNotNull();
		assertThat(model.getInstalacion().getId()).isEqualTo(2L);
		assertThat(model.getUsuario()).isNotNull();
		assertThat(model.getUsuario().getId()).isEqualTo(3L);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		UsuarioInstalacion model = new UsuarioInstalacion();
		model.setId(1L);
		model.setUuid("uuid-old");

		UsuarioInstalacionDTO dto = new UsuarioInstalacionDTO();
		dto.setId(2L);
		dto.setUuid("uuid-updated");
		dto.setInstalacionId(6L);
		dto.setUsuarioId(7L);

		UsuarioInstalacion resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getInstalacion().getId()).isEqualTo(6L);
		assertThat(resultado.getUsuario().getId()).isEqualTo(7L);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		UsuarioInstalacion model = new UsuarioInstalacion();
		model.setUuid("uuid-original");

		UsuarioInstalacionDTO dto = new UsuarioInstalacionDTO();
		dto.setUuid("");
		dto.setInstalacionId(1L);
		dto.setUsuarioId(1L);

		UsuarioInstalacion resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		Instalacion instalacion = new Instalacion();
		instalacion.setNombre("Campo de Fútbol");

		Usuario usuario = new Usuario();
		usuario.setNombre("Pedro Picapiedra");

		UsuarioInstalacion model = new UsuarioInstalacion();
		model.setId(3L);
		model.setInstalacion(instalacion);
		model.setUsuario(usuario);

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(3L);
		assertThat(combos.get(0).getValue()).isEqualTo("Pedro Picapiedra - Campo de Fútbol");
	}
}