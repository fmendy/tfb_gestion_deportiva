package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.dto.EmpresaRegistroDTO;
import com.gestion.deportiva.dto.filter.EmpresaFilter;
import com.gestion.deportiva.mapper.EmpresaMapper;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.repository.EmpresaRepository;
import com.gestion.deportiva.service.ImageStoreService;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceImplTest {

	@Mock
	private EmpresaRepository empresaRepository;

	@Mock
	private EmpresaMapper empresaMapper;

	@Mock
	private ImageStoreService imageStoreService;

	@Mock
	private ReservaService reservaService;

	@InjectMocks
	private EmpresaServiceImpl empresaService;

	private MockedStatic<SecurityUtil> securityUtilMockedStatic;

	@BeforeEach
	void setUp() {
		securityUtilMockedStatic = mockStatic(SecurityUtil.class);
	}

	@AfterEach
	void tearDown() {
		securityUtilMockedStatic.close();
	}

	@Test
	void buscarPorId() {
		Long id = 1L;
		Empresa empresa = new Empresa();
		empresa.setId(id);
		EmpresaDTO dto = new EmpresaDTO();

		when(empresaRepository.findByActivoTrueAndId(id)).thenReturn(empresa);
		when(empresaMapper.modelToDTO(empresa)).thenReturn(dto);

		EmpresaDTO resultado = empresaService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(empresaRepository).findByActivoTrueAndId(id);
		verify(empresaMapper).modelToDTO(empresa);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		Empresa empresa = new Empresa();
		EmpresaDTO dto = new EmpresaDTO();

		when(empresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(empresa);
		when(empresaMapper.modelToDTO(empresa)).thenReturn(dto);

		EmpresaDTO resultado = empresaService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(empresaRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(empresaMapper).modelToDTO(empresa);
	}

	@Test
	void guardarNuevoCuandoNoExiste() throws IOException {
		EmpresaDTO dto = new EmpresaDTO();
		dto.setId(1L);
		dto.setLogoBorrar(true);

		when(empresaRepository.findByActivoTrueAndId(1L)).thenReturn(null);
		when(empresaMapper.dtoToModel(any(EmpresaDTO.class), any(Empresa.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		Long id = empresaService.guardar(dto);

		verify(empresaRepository).findByActivoTrueAndId(1L);
		verify(empresaRepository).saveAndFlush(any(Empresa.class));
	}

	@Test
	void guardarExistenteCuandoYaExiste() {
		EmpresaDTO dto = new EmpresaDTO();
		dto.setId(2L);

		Empresa empresaExistente = new Empresa();
		empresaExistente.setId(2L);

		when(empresaRepository.findByActivoTrueAndId(2L)).thenReturn(empresaExistente);
		when(empresaMapper.dtoToModel(dto, empresaExistente)).thenReturn(empresaExistente);

		Long id = empresaService.guardar(dto);

		assertThat(id).isEqualTo(2L);
		verify(empresaRepository).saveAndFlush(empresaExistente);
	}

	@Test
	void obtenerPaginaPorFiltro() {
		EmpresaFilter filter = new EmpresaFilter();
		// Inicializamos las colecciones internas del filtro para evitar
		// NullPointerException si la Specification intenta evaluarlas
		filter.setListIds(List.of());

		Pageable pageable = PageRequest.of(0, 10);
		Empresa model = new Empresa();
		Page<Empresa> pageModel = new PageImpl<>(List.of(model));
		Page<EmpresaDTO> pageDto = new PageImpl<>(List.of(new EmpresaDTO()));

		when(empresaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageModel);
		when(empresaMapper.pageToPageDTO(pageModel)).thenReturn(pageDto);

		Page<EmpresaDTO> resultado = empresaService.getPageByFilter(filter, pageable);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getContent()).hasSize(1);
		verify(empresaRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		Empresa empresa = new Empresa();
		empresa.setActivo(true);

		when(empresaRepository.findByActivoTrueAndId(id)).thenReturn(empresa);
		when(reservaService.getListByFechaDesdeInstalacionSedeEmpresaIdAndReservaEstados(
				any(LocalDate.now(ZoneId.of("Europe/Madrid")).getClass()), eq(id), any())).thenReturn(List.of());

		empresaService.eliminar(id);

		assertThat(empresa.isActivo()).isFalse();
		verify(empresaRepository).saveAndFlush(empresa);
		verify(reservaService).cancelarReservasEmpresa(any());
	}

	@Test
	void buscarPorNombreEqualsIgnoreCase() {
		String nombre = "Empresa Test";
		Empresa empresa = new Empresa();
		EmpresaDTO dto = new EmpresaDTO();

		when(empresaRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre)).thenReturn(empresa);
		when(empresaMapper.modelToDTO(empresa)).thenReturn(dto);

		EmpresaDTO resultado = empresaService.findByNombreEqualsIgnoreCase(nombre);

		assertThat(resultado).isEqualTo(dto);
		verify(empresaRepository).findByActivoTrueAndNombreEqualsIgnoreCase(nombre);
	}

	@Test
	void obtenerListComboDTO() {
		List<Empresa> listaModel = List.of(new Empresa());
		List<ComboDTO> listaCombo = List.of(new ComboDTO());

		when(empresaRepository.findByActivoTrue()).thenReturn(listaModel);
		when(empresaMapper.listModelToListComboDTO(listaModel)).thenReturn(listaCombo);

		List<ComboDTO> resultado = empresaService.getListComboDTO();

		assertThat(resultado).isEqualTo(listaCombo);
		verify(empresaRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTO() {
		List<Empresa> listaModel = List.of(new Empresa());
		// Usamos un ArrayList mutable ya que Utils.sortByNombre intenta ordenar la
		// lista devuelta
		List<EmpresaDTO> listaDto = new ArrayList<>(List.of(new EmpresaDTO()));

		when(empresaRepository.findByActivoTrue()).thenReturn(listaModel);
		when(empresaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<EmpresaDTO> resultado = empresaService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(empresaRepository).findByActivoTrue();
		verify(empresaMapper).listModelToListDTO(listaModel);
	}

	@Test
	void obtenerListDTOConFiltro() {
		EmpresaFilter filter = new EmpresaFilter();

		filter.setListIds(List.of());

		List<Empresa> listaModel = List.of(new Empresa());
		List<EmpresaDTO> listaDto = new ArrayList<>(List.of(new EmpresaDTO()));

		when(empresaRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(empresaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<EmpresaDTO> resultado = empresaService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(empresaRepository).findAll(any(Specification.class));
		verify(empresaMapper).listModelToListDTO(listaModel);
	}

	@Test
	void canWriteYCanRead() {
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(any())).thenReturn(true);

		assertThat(empresaService.canWrite(1L)).isTrue();
		assertThat(empresaService.canRead(1L)).isTrue();
	}

	@Test
	void registrarEmpresa() {
		EmpresaRegistroDTO dto = new EmpresaRegistroDTO();
		dto.setNombre("Nueva Empresa");
		Empresa empresa = new Empresa();
		empresa.setId(10L);

		when(empresaMapper.registroEmpresaDTOToModel(dto)).thenReturn(empresa);
		when(empresaRepository.saveAndFlush(empresa)).thenReturn(empresa);

		Long id = empresaService.registrarEmpresa(dto);

		assertThat(id).isEqualTo(10L);
		verify(empresaRepository).saveAndFlush(empresa);
	}

	@Test
	void getListDTOParaInstalacion() {
		List<Empresa> listaModel = List.of(new Empresa());
		List<EmpresaDTO> listaDto = new ArrayList(List.of(new EmpresaDTO()));

		when(empresaRepository.findByActivoTrue()).thenReturn(listaModel);
		when(empresaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<EmpresaDTO> resultado = empresaService.getListDTOParaInstalacion();

		assertThat(resultado).isNotNull();
		verify(empresaRepository).findByActivoTrue();
		verify(empresaMapper).listModelToListDTO(listaModel);
	}

	@Test
	void getListDTOParaEmpleado() {
		List<Empresa> listaModel = List.of(new Empresa());
		List<EmpresaDTO> listaDto = new ArrayList<>(List.of(new EmpresaDTO()));

		when(empresaRepository.findByActivoTrue()).thenReturn(listaModel);
		when(empresaMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<EmpresaDTO> resultado = empresaService.getListDTOParaEmpleado();

		assertThat(resultado).isNotNull();
		verify(empresaRepository).findByActivoTrue();
		verify(empresaMapper).listModelToListDTO(listaModel);
	}
}