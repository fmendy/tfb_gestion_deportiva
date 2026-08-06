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
import com.gestion.deportiva.dto.MunicipioDTO;
import com.gestion.deportiva.dto.filter.MunicipioFilter;
import com.gestion.deportiva.mapper.MunicipioMapper;
import com.gestion.deportiva.model.Municipio;
import com.gestion.deportiva.model.Provincia;
import com.gestion.deportiva.repository.MunicipioRepository;
import com.gestion.deportiva.repository.ProvinciaRepository;

@ExtendWith(MockitoExtension.class)
class MunicipioServiceImplTest {

	@Mock
	private MunicipioRepository municipioRepository;

	@Mock
	private ProvinciaRepository provinciaRepository;

	@Mock
	private MunicipioMapper municipioMapper;

	@InjectMocks
	private MunicipioServiceImpl municipioService;

	@Test
	void buscarPorId() {
		Long id = 1L;
		Municipio model = new Municipio();
		model.setId(id);
		MunicipioDTO dto = new MunicipioDTO();

		when(municipioRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(municipioMapper.modelToDTO(model)).thenReturn(dto);

		MunicipioDTO resultado = municipioService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(municipioRepository).findByActivoTrueAndId(id);
		verify(municipioMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		Municipio model = new Municipio();
		MunicipioDTO dto = new MunicipioDTO();

		when(municipioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(municipioMapper.modelToDTO(model)).thenReturn(dto);

		MunicipioDTO resultado = municipioService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(municipioRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(municipioMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExiste() {
		MunicipioDTO dto = new MunicipioDTO();
		dto.setUuid("uuid-nuevo");
		dto.setProvinciaUuid("prov-uuid");

		Provincia provincia = new Provincia();

		when(municipioRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(provinciaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("prov-uuid")).thenReturn(provincia);
		when(municipioMapper.dtoToModel(any(MunicipioDTO.class), any(Municipio.class), any(Provincia.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		municipioService.guardar(dto);

		verify(municipioRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(provinciaRepository).findByActivoTrueAndUuidEqualsIgnoreCase("prov-uuid");
		verify(municipioRepository).saveAndFlush(any(Municipio.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		MunicipioDTO dto = new MunicipioDTO();
		dto.setUuid("uuid-existente");
		dto.setProvinciaUuid("prov-uuid");

		Municipio modelExistente = new Municipio();
		modelExistente.setId(10L);
		Provincia provincia = new Provincia();

		when(municipioRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-existente")).thenReturn(modelExistente);
		when(provinciaRepository.findByActivoTrueAndUuidEqualsIgnoreCase("prov-uuid")).thenReturn(provincia);
		when(municipioMapper.dtoToModel(dto, modelExistente, provincia)).thenReturn(modelExistente);

		Long id = municipioService.guardar(dto);

		assertThat(id).isEqualTo(10L);
		verify(municipioRepository).saveAndFlush(modelExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		MunicipioFilter filter = new MunicipioFilter();
		Pageable pageable = PageRequest.of(0, 10);
		Municipio model = new Municipio();
		Page<Municipio> pageModel = new PageImpl<>(List.of(model));
		Page<MunicipioDTO> pageDto = new PageImpl<>(List.of(new MunicipioDTO()));

		when(municipioRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(municipioMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<MunicipioDTO> resultado = municipioService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(municipioRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		Municipio model = new Municipio();
		model.setActivo(true);

		when(municipioRepository.findByActivoTrueAndId(id)).thenReturn(model);

		municipioService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(municipioRepository).saveAndFlush(model);
	}

	@Test
	void buscarPorNombreEqualsIgnoreCase() {
		String nombre = "Madrid";
		Municipio model = new Municipio();
		MunicipioDTO dto = new MunicipioDTO();

		when(municipioRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre)).thenReturn(model);
		when(municipioMapper.modelToDTO(model)).thenReturn(dto);

		MunicipioDTO resultado = municipioService.findByNombreEqualsIgnoreCase(nombre);

		assertThat(resultado).isEqualTo(dto);
		verify(municipioRepository).findByActivoTrueAndNombreEqualsIgnoreCase(nombre);
		verify(municipioMapper).modelToDTO(model);
	}

	@Test
	void obtenerListComboDTO() {
		List<Municipio> listaModel = List.of(new Municipio());
		List<ComboDTO> listaComboDto = List.of(new ComboDTO());

		when(municipioRepository.findByActivoTrue()).thenReturn(listaModel);
		when(municipioMapper.listModelToListComboDTO(listaModel)).thenReturn(listaComboDto);

		List<ComboDTO> resultado = municipioService.getListComboDTO();

		assertThat(resultado).isNotNull();
		verify(municipioRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTO() {
		List<Municipio> listaModel = List.of(new Municipio());
		List<MunicipioDTO> listaDto = List.of(new MunicipioDTO());

		when(municipioRepository.findByActivoTrue()).thenReturn(listaModel);
		when(municipioMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		verify(municipioRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTOConSedes() {
		List<Municipio> listaModel = List.of(new Municipio());
		List<MunicipioDTO> listaDto = List.of(new MunicipioDTO());

		when(municipioRepository.findByActivoTrueAndSede()).thenReturn(listaModel);
		when(municipioMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		verify(municipioRepository).findByActivoTrueAndSede();
	}

	@Test
	void obtenerListDTOConFiltro() {
		List<Municipio> listaModel = List.of(new Municipio());
		List<MunicipioDTO> listaDto = List.of(new MunicipioDTO());

		when(municipioRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(municipioMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		verify(municipioRepository).findAll(any(Specification.class));
	}

	@Test
	void getListDTOByComunidadAutonomaIdOrProvinciaIdConProvinciaId() {
		Long provinciaId = 5L;
		List<Municipio> listaModel = List.of(new Municipio());
		List<MunicipioDTO> listaDto = List.of(new MunicipioDTO());

		when(municipioRepository.findByActivoTrueAndProvinciaId(provinciaId)).thenReturn(listaModel);
		when(municipioMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<MunicipioDTO> resultado = municipioService.getListDTOByComunidadAutonomaIdOrProvinciaId(null, provinciaId);

		assertThat(resultado).isNotNull();
		verify(municipioRepository).findByActivoTrueAndProvinciaId(provinciaId);
	}

	@Test
	void getListDTOByComunidadAutonomaIdOrProvinciaIdConComunidadId() {
		Long comunidadId = 2L;
		List<Municipio> listaModel = List.of(new Municipio());
		List<MunicipioDTO> listaDto = List.of(new MunicipioDTO());

		when(municipioRepository.findByActivoTrueAndProvinciaComunidadAutonomaId(comunidadId)).thenReturn(listaModel);
		when(municipioMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<MunicipioDTO> resultado = municipioService.getListDTOByComunidadAutonomaIdOrProvinciaId(comunidadId, null);

		assertThat(resultado).isNotNull();
		verify(municipioRepository).findByActivoTrueAndProvinciaComunidadAutonomaId(comunidadId);
	}

	@Test
	void canWriteYCanRead() {
		assertThat(municipioService.canWrite(1L)).isTrue();
		assertThat(municipioService.canRead(1L)).isTrue();
	}
}