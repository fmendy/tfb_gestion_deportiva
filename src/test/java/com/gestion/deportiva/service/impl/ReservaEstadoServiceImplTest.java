package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ReservaEstadoDTO;
import com.gestion.deportiva.dto.filter.ReservaEstadoFilter;
import com.gestion.deportiva.mapper.ReservaEstadoMapper;
import com.gestion.deportiva.model.ReservaEstado;
import com.gestion.deportiva.repository.ReservaEstadoRepository;

@ExtendWith(MockitoExtension.class)
class ReservaEstadoServiceImplTest {

	@Mock
	private ReservaEstadoRepository reservaEstadoRepository;

	@Mock
	private ReservaEstadoMapper reservaEstadoMapper;

	@InjectMocks
	private ReservaEstadoServiceImpl reservaEstadoService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		ReservaEstado model = new ReservaEstado();
		model.setId(id);
		ReservaEstadoDTO dto = new ReservaEstadoDTO();

		when(reservaEstadoRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(reservaEstadoMapper.modelToDTO(model)).thenReturn(dto);

		ReservaEstadoDTO resultado = reservaEstadoService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(reservaEstadoRepository).findByActivoTrueAndId(id);
		verify(reservaEstadoMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		ReservaEstado model = new ReservaEstado();
		ReservaEstadoDTO dto = new ReservaEstadoDTO();

		when(reservaEstadoRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(reservaEstadoMapper.modelToDTO(model)).thenReturn(dto);

		ReservaEstadoDTO resultado = reservaEstadoService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(reservaEstadoRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(reservaEstadoMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		ReservaEstadoDTO dto = new ReservaEstadoDTO();
		dto.setUuid("uuid-nuevo");

		when(reservaEstadoRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(reservaEstadoMapper.dtoToModel(any(ReservaEstadoDTO.class), any(ReservaEstado.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		reservaEstadoService.guardar(dto);

		verify(reservaEstadoRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(reservaEstadoRepository).saveAndFlush(any(ReservaEstado.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		ReservaEstadoDTO dto = new ReservaEstadoDTO();
		dto.setUuid("uuid-existente");

		ReservaEstado modelExistente = new ReservaEstado();
		modelExistente.setId(10L);

		when(reservaEstadoRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente"))
				.thenReturn(modelExistente);
		when(reservaEstadoMapper.dtoToModel(dto, modelExistente)).thenReturn(modelExistente);

		Long id = reservaEstadoService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(reservaEstadoRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		ReservaEstadoFilter filter = new ReservaEstadoFilter();
		Pageable pageable = PageRequest.of(0, 10);
		ReservaEstado model = new ReservaEstado();
		Page<ReservaEstado> pageModel = new PageImpl<>(List.of(model));
		Page<ReservaEstadoDTO> pageDto = new PageImpl<>(List.of(new ReservaEstadoDTO()));

		when(reservaEstadoRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(reservaEstadoMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<ReservaEstadoDTO> resultado = reservaEstadoService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(reservaEstadoRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		ReservaEstado model = new ReservaEstado();
		model.setActivo(true);

		when(reservaEstadoRepository.findByActivoTrueAndId(id)).thenReturn(model);

		reservaEstadoService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(reservaEstadoRepository).saveAndFlush(model);
	}

	@Test
	void buscarPorNombreEqualsIgnoreCase() {
		String nombre = "Pendiente";
		ReservaEstado model = new ReservaEstado();
		ReservaEstadoDTO dto = new ReservaEstadoDTO();

		when(reservaEstadoRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre)).thenReturn(model);
		when(reservaEstadoMapper.modelToDTO(model)).thenReturn(dto);

		ReservaEstadoDTO resultado = reservaEstadoService.findByNombreEqualsIgnoreCase(nombre);

		assertThat(resultado).isEqualTo(dto);
		verify(reservaEstadoRepository).findByActivoTrueAndNombreEqualsIgnoreCase(nombre);
		verify(reservaEstadoMapper).modelToDTO(model);
	}

	@Test
	void obtenerListComboDTO() {
		List<ReservaEstado> listaModel = List.of(new ReservaEstado());
		List<ComboDTO> listaComboDto = List.of(new ComboDTO());

		when(reservaEstadoRepository.findByActivoTrue()).thenReturn(listaModel);
		when(reservaEstadoMapper.listModelToListComboDTO(listaModel)).thenReturn(listaComboDto);

		List<ComboDTO> resultado = reservaEstadoService.getListComboDTO();

		assertThat(resultado).isNotNull();
		verify(reservaEstadoRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTO() {
		List<ReservaEstado> listaModel = List.of(new ReservaEstado());
		List<ReservaEstadoDTO> listaDto = List.of(new ReservaEstadoDTO());

		when(reservaEstadoRepository.findByActivoTrue()).thenReturn(listaModel);
		when(reservaEstadoMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		verify(reservaEstadoRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConFiltro() {
		List<ReservaEstado> listaModel = List.of(new ReservaEstado());
		List<ReservaEstadoDTO> listaDto = List.of(new ReservaEstadoDTO());

		when(reservaEstadoRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(reservaEstadoMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		verify(reservaEstadoRepository).findAll(any(Specification.class));
	}

	@Test
	void canWriteYCanRead() {
		assertThat(reservaEstadoService.canWrite(1L)).isTrue();
		assertThat(reservaEstadoService.canRead(1L)).isTrue();
	}
}