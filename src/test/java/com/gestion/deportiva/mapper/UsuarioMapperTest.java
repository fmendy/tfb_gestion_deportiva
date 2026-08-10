package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.EmpresaRegistroDTO;
import com.gestion.deportiva.dto.MiPerfilDTO;
import com.gestion.deportiva.dto.MiPerfilPasswordDTO;
import com.gestion.deportiva.dto.UsuarioDTO;
import com.gestion.deportiva.dto.UsuarioRegistroDTO;
import com.gestion.deportiva.model.Usuario;

@ExtendWith(MockitoExtension.class)
class UsuarioMapperTest {

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UsuarioRolMapper usuarioRolMapper;

	@InjectMocks
	private UsuarioMapper mapper;

	@BeforeEach
	void setUp() {
		// MockitoExtension initializes mocks and injects them into mapper
	}

	@Test
	void registroEmpresaDTOToModel() {
		EmpresaRegistroDTO dto = new EmpresaRegistroDTO();
		dto.setNombre("Empresa Admin");
		dto.setEmail("admin@empresa.com");
		dto.setPassword("plainPassword123");

		when(passwordEncoder.encode("plainPassword123")).thenReturn("encodedPassword123");

		Usuario model = mapper.registroEmpresaDTOToModel(dto);

		assertThat(model).isNotNull();
		assertThat(model.getNombre()).isEqualTo("Empresa Admin");
		assertThat(model.getEmail()).isEqualTo("admin@empresa.com");
		assertThat(model.getPassword()).isEqualTo("encodedPassword123");
	}

	@Test
	void modelToDTO() {
		Usuario model = new Usuario();
		model.setId(1L);
		model.setUuid("uuid-usuario-1");
		model.setEmail("user@test.com");
		model.setNombre("Juan Usuario");
		model.setListUsuarioRol(List.of()); // Avoids NullPointerException

		when(usuarioRolMapper.listModelToListDTO(model.getListUsuarioRol())).thenReturn(List.of());

		UsuarioDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getUuid()).isEqualTo("uuid-usuario-1");
		assertThat(dto.getEmail()).isEqualTo("user@test.com");
		assertThat(dto.getNombre()).isEqualTo("Juan Usuario");
		assertThat(dto.getListUsuarioRolDTO()).isEmpty();
	}

	@Test
	void listModelToListDTO() {
		Usuario model = new Usuario();
		model.setId(1L);
		model.setNombre("Usuario Test");
		model.setListUsuarioRol(List.of()); // Avoids NullPointerException

		when(usuarioRolMapper.listModelToListDTO(model.getListUsuarioRol())).thenReturn(List.of());

		List<UsuarioDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void pageToPageDTO() {
		Usuario model = new Usuario();
		model.setId(1L);
		model.setNombre("Usuario Page");
		model.setListUsuarioRol(List.of()); // Avoids NullPointerException

		when(usuarioRolMapper.listModelToListDTO(model.getListUsuarioRol())).thenReturn(List.of());

		PageRequest pageable = PageRequest.of(0, 10);
		Page<Usuario> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<UsuarioDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getId()).isEqualTo(1L);
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setId(5L);
		dto.setUuid("uuid-new");
		dto.setEmail("nuevo@test.com");
		dto.setNombre("Nuevo Nombre");

		Usuario model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getEmail()).isEqualTo("nuevo@test.com");
		assertThat(model.getNombre()).isEqualTo("Nuevo Nombre");
		assertThat(model.getPassword()).isEqualTo("");
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		Usuario model = new Usuario();
		model.setId(1L);
		model.setUuid("uuid-old");
		model.setEmail("old@test.com");
		model.setNombre("Old Name");
		model.setPassword("secret");

		UsuarioDTO dto = new UsuarioDTO();
		dto.setEmail("updated@test.com");
		dto.setNombre("Updated Name");

		Usuario resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(1L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-old");
		assertThat(resultado.getEmail()).isEqualTo("updated@test.com");
		assertThat(resultado.getNombre()).isEqualTo("Updated Name");
		assertThat(resultado.getPassword()).isEqualTo("secret");
	}

	@Test
	void registroDTOToModel() {
		UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
		dto.setEmail("register@test.com");
		dto.setNombre("Registered User");
		dto.setPassword("password123");

		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");

		Usuario model = mapper.registroDTOToModel(dto);

		assertThat(model).isNotNull();
		assertThat(model.getEmail()).isEqualTo("register@test.com");
		assertThat(model.getNombre()).isEqualTo("Registered User");
		assertThat(model.getPassword()).isEqualTo("encodedPassword123");
	}

	@Test
	void modelToMiPerfilDTO() {
		LocalDateTime now = LocalDateTime.now();
		Usuario model = new Usuario();
		model.setId(10L);
		model.setUuid("uuid-profile");
		model.setNombre("Profile User");
		model.setEmail("profile@test.com");
		model.setFechaCreacion(now);

		MiPerfilDTO dto = mapper.modelToMiPerfilDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(10L);
		assertThat(dto.getUuid()).isEqualTo("uuid-profile");
		assertThat(dto.getNombre()).isEqualTo("Profile User");
		assertThat(dto.getEmail()).isEqualTo("profile@test.com");
		assertThat(dto.getFechaAlta()).isEqualTo(now);
	}

	@Test
	void modelToMiPerfilPasswordDTO() {
		Usuario model = new Usuario();
		model.setId(15L);
		model.setUuid("uuid-pwd");

		MiPerfilPasswordDTO dto = mapper.modelToMiPerfilPasswordDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(15L);
		assertThat(dto.getUuid()).isEqualTo("uuid-pwd");
	}

	@Test
	void listModelToListComboDTO() {
		Usuario model = new Usuario();
		model.setId(3L);
		model.setNombre("Combo User");

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(3L);
		assertThat(combos.get(0).getValue()).isEqualTo("Combo User");
	}
}