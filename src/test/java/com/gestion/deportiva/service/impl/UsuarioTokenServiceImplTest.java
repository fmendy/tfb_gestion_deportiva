package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.UsuarioTokenDTO;
import com.gestion.deportiva.dto.filter.UsuarioTokenFilter;
import com.gestion.deportiva.mapper.UsuarioTokenMapper;
import com.gestion.deportiva.model.UsuarioToken;
import com.gestion.deportiva.repository.UsuarioTokenRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioTokenServiceImplTest {

	@Mock
	private UsuarioTokenRepository usuarioTokenRepository;

	@Mock
	private UsuarioTokenMapper usuarioTokenMapper;

	@InjectMocks
	private UsuarioTokenServiceImpl usuarioTokenService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		UsuarioToken model = new UsuarioToken();
		model.setId(id);
		UsuarioTokenDTO dto = new UsuarioTokenDTO();

		when(usuarioTokenRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(usuarioTokenMapper.modelToDTO(model)).thenReturn(dto);

		UsuarioTokenDTO resultado = usuarioTokenService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioTokenRepository).findByActivoTrueAndId(id);
		verify(usuarioTokenMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		UsuarioToken model = new UsuarioToken();
		UsuarioTokenDTO dto = new UsuarioTokenDTO();

		when(usuarioTokenRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(usuarioTokenMapper.modelToDTO(model)).thenReturn(dto);

		UsuarioTokenDTO resultado = usuarioTokenService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(usuarioTokenRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(usuarioTokenMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		UsuarioTokenDTO dto = new UsuarioTokenDTO();
		dto.setUuid("uuid-nuevo");

		when(usuarioTokenRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(usuarioTokenMapper.dtoToModel(any(UsuarioTokenDTO.class), any(UsuarioToken.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		Long id = usuarioTokenService.guardar(dto);

		verify(usuarioTokenRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(usuarioTokenRepository).saveAndFlush(any(UsuarioToken.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		UsuarioTokenDTO dto = new UsuarioTokenDTO();
		dto.setUuid("uuid-existente");

		UsuarioToken modelExistente = new UsuarioToken();
		modelExistente.setId(10L);

		when(usuarioTokenRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente"))
				.thenReturn(modelExistente);
		when(usuarioTokenMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = usuarioTokenService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(usuarioTokenRepository).saveAndFlush(modelExistente);
	}

	@Test
	void crearToken() {
		Long usuarioId = 1L;

		when(usuarioTokenRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(List.of());
		when(usuarioTokenRepository.saveAndFlush(any(UsuarioToken.class))).thenAnswer(invocation -> {
			UsuarioToken ut = invocation.getArgument(0);
			ut.setId(55L);
			return ut;
		});

		String uuid = usuarioTokenService.crearToken(usuarioId);

		assertThat(uuid).isNotNull();
		verify(usuarioTokenRepository).findByActivoTrueAndUsuarioId(usuarioId);
		verify(usuarioTokenRepository).saveAndFlush(any(UsuarioToken.class));
	}

	@Test
	void desactivarTokensByUsuarioId() {
		Long usuarioId = 1L;
		UsuarioToken token = new UsuarioToken();
		token.setActivo(true);

		when(usuarioTokenRepository.findByActivoTrueAndUsuarioId(usuarioId)).thenReturn(List.of(token));

		usuarioTokenService.desactivarTokensByUsuarioId(usuarioId);

		assertThat(token.isActivo()).isFalse();
		verify(usuarioTokenRepository).findByActivoTrueAndUsuarioId(usuarioId);
	}

	@Test
	void getTokenActivoByUuidValido() {
		String uuid = "uuid-valido";
		UsuarioToken token = new UsuarioToken();
		token.setFechaCreacion(LocalDateTime.now());

		when(usuarioTokenRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(token);

		UsuarioToken resultado = usuarioTokenService.getTokenActivoByUuid(uuid);

		assertThat(resultado).isEqualTo(token);
		verify(usuarioTokenRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
	}

	@Test
	void getTokenActivoByUuidInvalidoOExpirado() {
		String uuid = "uuid-expirado";
		UsuarioToken token = new UsuarioToken();
		token.setFechaCreacion(LocalDateTime.now().minusMinutes(10));

		when(usuarioTokenRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(token);

		UsuarioToken resultado = usuarioTokenService.getTokenActivoByUuid(uuid);

		assertThat(resultado).isNull();
		verify(usuarioTokenRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
	}

	@Test
	void getTokenActivoByUuidNull() {
		String uuid = "uuid-null";

		when(usuarioTokenRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(null);

		UsuarioToken resultado = usuarioTokenService.getTokenActivoByUuid(uuid);

		assertThat(resultado).isNull();
		verify(usuarioTokenRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
	}

	@Test
	void isValidTokenTrue() {
		UsuarioToken token = new UsuarioToken();
		token.setFechaCreacion(LocalDateTime.now());

		Boolean valido = usuarioTokenService.isValidToken(token, 5L);

		assertThat(valido).isTrue();
	}

	@Test
	void isValidTokenFalseNull() {
		Boolean valido = usuarioTokenService.isValidToken(null, 5L);

		assertThat(valido).isFalse();
	}

	@Test
	void obtenerPaginaPorFiltro() {
		UsuarioTokenFilter filter = new UsuarioTokenFilter();
		Pageable pageable = PageRequest.of(0, 10);
		UsuarioToken model = new UsuarioToken();
		Page<UsuarioToken> pageModel = new PageImpl<>(List.of(model));
		Page<UsuarioTokenDTO> pageDto = new PageImpl<>(List.of(new UsuarioTokenDTO()));

		when(usuarioTokenRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(usuarioTokenMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<UsuarioTokenDTO> resultado = usuarioTokenService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(usuarioTokenRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		UsuarioToken model = new UsuarioToken();
		model.setActivo(true);

		when(usuarioTokenRepository.findByActivoTrueAndId(id)).thenReturn(model);

		usuarioTokenService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(usuarioTokenRepository).saveAndFlush(model);
	}

	@Test
	void obtenerListDTO() {
		List<UsuarioToken> listaModel = List.of(new UsuarioToken());
		List<UsuarioTokenDTO> listaDto = List.of(new UsuarioTokenDTO());

		when(usuarioTokenRepository.findByActivoTrue()).thenReturn(listaModel);
		when(usuarioTokenMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<UsuarioTokenDTO> resultado = usuarioTokenService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(usuarioTokenRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		UsuarioTokenFilter filter = new UsuarioTokenFilter();
		List<UsuarioToken> listaModel = List.of(new UsuarioToken());
		List<UsuarioTokenDTO> listaDto = List.of(new UsuarioTokenDTO());

		when(usuarioTokenRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(usuarioTokenMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<UsuarioTokenDTO> resultado = usuarioTokenService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(usuarioTokenRepository).findAll(any(Specification.class));
	}

	@Test
	void canWriteYCanRead() {
		assertThat(usuarioTokenService.canWrite(1L)).isTrue();
		assertThat(usuarioTokenService.canRead(1L)).isTrue();
	}
}