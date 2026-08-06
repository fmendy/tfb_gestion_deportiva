package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.gestion.deportiva.dto.ProvinciaDTO;
import com.gestion.deportiva.dto.filter.ProvinciaFilter;
import com.gestion.deportiva.mapper.ProvinciaMapper;
import com.gestion.deportiva.model.ComunidadAutonoma;
import com.gestion.deportiva.model.Provincia;
import com.gestion.deportiva.repository.ComunidadAutonomaRepository;
import com.gestion.deportiva.repository.ProvinciaRepository;

@ExtendWith(MockitoExtension.class)
class ProvinciaServiceImplTest {

	@Mock
	private ProvinciaRepository provinciaRepository;

	@Mock
	private ComunidadAutonomaRepository comunidadAutonomaRepository;

	@Mock
	private ProvinciaMapper provinciaMapper;

	@InjectMocks
	private ProvinciaServiceImpl provinciaService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		Provincia model = new Provincia();
		model.setId(id);
		ProvinciaDTO dto = new ProvinciaDTO();

		when(provinciaRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(provinciaMapper.modelToDTO(model)).thenReturn(dto);

		ProvinciaDTO resultado = provinciaService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(provinciaRepository).findByActivoTrueAndId(id);
		verify(provinciaMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		Provincia model = new Provincia();
		ProvinciaDTO dto = new ProvinciaDTO();

		when(provinciaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(provinciaMapper.modelToDTO(model)).thenReturn(dto);

		ProvinciaDTO resultado = provinciaService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(provinciaRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(provinciaMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		ProvinciaDTO dto = new ProvinciaDTO();
		dto.setUuid("uuid-nuevo");
		dto.setComunidadAutonomaUuid("com-uuid");

		ComunidadAutonoma comunidad = new ComunidadAutonoma();

		when(provinciaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(comunidadAutonomaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("com-uuid")).thenReturn(comunidad);
		when(provinciaMapper.dtoToModel(any(ProvinciaDTO.class), any(Provincia.class), any(ComunidadAutonoma.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		Long id = provinciaService.guardar(dto);

		verify(provinciaRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(comunidadAutonomaRepository).findByActivoTrueAndUuidEqualsIgnoreCase("com-uuid");
		verify(provinciaRepository).saveAndFlush(any(Provincia.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		ProvinciaDTO dto = new ProvinciaDTO();
		dto.setUuid("uuid-existente");
		dto.setComunidadAutonomaUuid("com-uuid");

		Provincia modelExistente = new Provincia();
		modelExistente.setId(10L);
		ComunidadAutonoma comunidad = new ComunidadAutonoma();

		when(provinciaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente")).thenReturn(modelExistente);
		when(comunidadAutonomaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("com-uuid")).thenReturn(comunidad);
		when(provinciaMapper.dtoToModel(dto, modelExistente, comunidad)).thenReturn(modelExistente);

		Long id = provinciaService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(provinciaRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		ProvinciaFilter filter = new ProvinciaFilter();
		Pageable pageable = PageRequest.of(0, 10);
		Provincia model = new Provincia();
		Page<Provincia> pageModel = new PageImpl<>(List.of(model));
		Page<ProvinciaDTO> pageDto = new PageImpl<>(List.of(new ProvinciaDTO()));

		when(provinciaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(provinciaMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<ProvinciaDTO> resultado = provinciaService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(provinciaRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		Provincia model = new Provincia();
		model.setActivo(true);

		when(provinciaRepository.findByActivoTrueAndId(id)).thenReturn(model);

		provinciaService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(provinciaRepository).saveAndFlush(model);
	}

	@Test
	void buscarPorNombreEqualsIgnoreCase() {
		String nombre = "Madrid";
		Provincia model = new Provincia();
		ProvinciaDTO dto = new ProvinciaDTO();

		when(provinciaRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre)).thenReturn(model);
		when(provinciaMapper.modelToDTO(model)).thenReturn(dto);

		ProvinciaDTO resultado = provinciaService.findByNombreEqualsIgnoreCase(nombre);

		assertThat(resultado).isEqualTo(dto);
		verify(provinciaRepository).findByActivoTrueAndNombreEqualsIgnoreCase(nombre);
		verify(provinciaMapper).modelToDTO(model);
	}

	@Test
	void obtenerListComboDTO() {
		List<Provincia> listaModel = List.of(new Provincia());
		List<ComboDTO> listaComboDto = List.of(new ComboDTO());

		when(provinciaRepository.findByActivoTrue()).thenReturn(listaModel);
		when(provinciaMapper.listModelToListComboDTO(listaModel)).thenReturn(listaComboDto);

		List<ComboDTO> resultado = provinciaService.getListComboDTO();

		assertThat(resultado).isNotNull();
		verify(provinciaRepository).findByActivoTrue();
	}

	@Test
	void getListDTOByComunidadAutonomaIdConId() {
		Long comunidadId = 3L;
		List<Provincia> listaModel = List.of(new Provincia());
		List<ProvinciaDTO> listaDto = List.of(new ProvinciaDTO());

		when(provinciaRepository.findByActivoTrueAndComunidadAutonomaId(comunidadId)).thenReturn(listaModel);
		when(provinciaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<ProvinciaDTO> resultado = provinciaService.getListDTOByComunidadAutonomaId(comunidadId);

		assertThat(resultado).isNotNull();
		verify(provinciaRepository).findByActivoTrueAndComunidadAutonomaId(comunidadId);
	}

	@Test
	void getListDTOByComunidadAutonomaIdSinId() {
		Long comunidadAutonomaId = null;
		List<Provincia> listaModel = List.of(new Provincia());
		List<ProvinciaDTO> listaDto = new ArrayList<>(List.of(new ProvinciaDTO()));

		when(provinciaRepository.findByActivoTrue()).thenReturn(listaModel);
		when(provinciaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<ProvinciaDTO> resultado = provinciaService.getListDTOByComunidadAutonomaId(comunidadAutonomaId);

		assertThat(resultado).isNotNull();
		verify(provinciaRepository).findByActivoTrue();
		verify(provinciaMapper).listModelToListDTO(listaModel);
	}

	@Test
	void obtenerListDTO() {
		List<Provincia> listaModel = List.of(new Provincia());
		List<ProvinciaDTO> listaDto = new ArrayList(List.of(new ProvinciaDTO()));

		when(provinciaRepository.findByActivoTrue()).thenReturn(listaModel);
		when(provinciaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<ProvinciaDTO> resultado = provinciaService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(provinciaRepository).findByActivoTrue();
		verify(provinciaMapper).listModelToListDTO(listaModel);
	}

	@Test
	void obtenerListDTOConFiltro() {
		ProvinciaFilter filter = new ProvinciaFilter();
		List<Provincia> listaModel = List.of(new Provincia());
		// Usamos un ArrayList mutable ya que Utils.sortByNombre intenta ordenar la
		// lista devuelta
		List<ProvinciaDTO> listaDto = new ArrayList<>(List.of(new ProvinciaDTO()));

		when(provinciaRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(provinciaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<ProvinciaDTO> resultado = provinciaService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(provinciaRepository).findAll(any(Specification.class));
		verify(provinciaMapper).listModelToListDTO(listaModel);
	}

	@Test
	void canWriteYCanRead() {
		assertThat(provinciaService.canWrite(1L)).isTrue();
		assertThat(provinciaService.canRead(1L)).isTrue();
	}
}