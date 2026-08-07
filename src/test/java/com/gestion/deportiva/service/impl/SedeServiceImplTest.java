package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.SedeDTO;
import com.gestion.deportiva.dto.SedePublicoDTO;
import com.gestion.deportiva.dto.filter.SedeFilter;
import com.gestion.deportiva.dto.filter.SedePublicoFilter;
import com.gestion.deportiva.mapper.InstalacionMapper;
import com.gestion.deportiva.mapper.SedeMapper;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.repository.SedeRepository;
import com.gestion.deportiva.service.ImageStoreService;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
class SedeServiceImplTest {

	@Mock
	private SedeRepository sedeRepository;

	@Mock
	private InstalacionRepository instalacionRepository;

	@Mock
	private InstalacionMapper instalacionMapper;

	@Mock
	private SedeMapper sedeMapper;

	@Mock
	private ImageStoreService imageStoreService;

	@Mock
	private ReservaService reservaService;

	@InjectMocks
	private SedeServiceImpl sedeService;
	
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
		Sede model = new Sede();
		model.setId(id);
		SedeDTO dto = new SedeDTO();

		when(sedeRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(sedeMapper.modelToDTO(model)).thenReturn(dto);

		SedeDTO resultado = sedeService.findById(id);

		assertThat(resultado).isEqualTo(dto);
		verify(sedeRepository).findByActivoTrueAndId(id);
		verify(sedeMapper).modelToDTO(model);
	}

	@Test
	void buscarPorUuid() {
		String uuid = "uuid-123";
		Sede model = new Sede();
		SedeDTO dto = new SedeDTO();

		when(sedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(sedeMapper.modelToDTO(model)).thenReturn(dto);

		SedeDTO resultado = sedeService.findByUuid(uuid);

		assertThat(resultado).isEqualTo(dto);
		verify(sedeRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		verify(sedeMapper).modelToDTO(model);
	}

	@Test
	void guardarNuevoCuandoNoExisteSinLogo() {
		SedeDTO dto = new SedeDTO();
		dto.setUuid("uuid-nuevo");

		when(sedeRepository.findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo")).thenReturn(null);
		when(sedeMapper.dtoToModel(any(SedeDTO.class), any(Sede.class)))
				.thenAnswer(invocation -> invocation.getArgument(1));

		sedeService.guardar(dto);

		verify(sedeRepository).findByActivoTrueAndUuidEqualsIgnoreCase("uuid-nuevo");
		verify(sedeRepository).saveAndFlush(any(Sede.class));
	}

	@Test
	void obtenerPaginaPorFiltro() {
		Sede model = new Sede();
		Page<Sede> pageModel = new PageImpl<>(List.of(model));
		Page<SedeDTO> pageDto = new PageImpl<>(List.of(new SedeDTO()));

	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		Sede model = new Sede();
		model.setActivo(true);

		when(sedeRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(reservaService.getListByFechaDesdeInstalacionSedeIdAndReservaEstados(
				any(LocalDate.now(ZoneId.of("Europe/Madrid")).getClass()), any(Long.class), any(List.class)))
				.thenReturn(List.of());

		sedeService.eliminar(id);

		assertThat(model.isActivo()).isFalse();
		verify(sedeRepository).saveAndFlush(model);
		verify(reservaService).cancelarReservasEmpresa(any());
	}

	@Test
	void buscarPorNombreEqualsIgnoreCase() {
		String nombre = "Sede Central";
		Sede model = new Sede();
		SedeDTO dto = new SedeDTO();

		when(sedeRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre)).thenReturn(model);
		when(sedeMapper.modelToDTO(model)).thenReturn(dto);

		SedeDTO resultado = sedeService.findByNombreEqualsIgnoreCase(nombre);

		assertThat(resultado).isEqualTo(dto);
		verify(sedeRepository).findByActivoTrueAndNombreEqualsIgnoreCase(nombre);
		verify(sedeMapper).modelToDTO(model);
	}

	@Test
	void obtenerListComboDTO() {
		List<Sede> listaModel = List.of(new Sede());
		List<ComboDTO> listaComboDto = List.of(new ComboDTO());

		when(sedeRepository.findByActivoTrue()).thenReturn(listaModel);
		when(sedeMapper.listModelToListComboDTO(listaModel)).thenReturn(listaComboDto);

		List<ComboDTO> resultado = sedeService.getListComboDTO();

		assertThat(resultado).isNotNull();
		verify(sedeRepository).findByActivoTrue();
	}

	@Test
	void obtenerListDTO() {
		List<Sede> listaModel = List.of(new Sede());

		List<SedeDTO> listaDto = new ArrayList(List.of(new SedeDTO()));

		when(sedeRepository.findByActivoTrue()).thenReturn(listaModel);
		when(sedeMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<SedeDTO> resultado = sedeService.getListDTO();

		assertThat(resultado).isNotNull();
		verify(sedeRepository).findByActivoTrue();
		verify(sedeMapper).listModelToListDTO(listaModel);
	}

	@Test
	void obtenerListDTOConFiltro() {
		SedeFilter filter = new SedeFilter();
		List<Sede> listaModel = List.of(new Sede());
		List<SedeDTO> listaDto = new ArrayList<>(List.of(new SedeDTO()));

		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(any())).thenReturn(true);
		when(sedeRepository.findAll(any(Specification.class))).thenReturn(listaModel);
		when(sedeMapper.listModelToListDTO(listaModel)).thenReturn(listaDto);

		List<SedeDTO> resultado = sedeService.getListDTO(filter);

		assertThat(resultado).isNotNull();
		verify(sedeRepository).findAll(any(Specification.class));
	}
	
	

	@Test
	void getListSedePublicoDTO() {
		SedePublicoFilter filter = new SedePublicoFilter();
		Sede sede = new Sede();
		sede.setId(1L);
		Page<Sede> pageSedes = new PageImpl<>(List.of(sede));

		when(sedeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageSedes);
		when(instalacionRepository.findByActivoTrueAndSedeId(1L)).thenReturn(List.of(new Instalacion()));
		when(instalacionMapper.listModelToListDTO(any())).thenReturn(List.of(new InstalacionDTO()));
		when(sedeMapper.modelToMapaDTO(any(), any())).thenReturn(new SedePublicoDTO());

		List<SedePublicoDTO> resultado = sedeService.getListSedePublicoDTO(filter);

		assertThat(resultado).hasSize(1);
		verify(sedeRepository).findAll(any(Specification.class), any(Pageable.class));
	}

	@Test
	void getSedePublicoDTOById() {
		Long id = 1L;
		Sede sede = new Sede();
		sede.setId(id);

		when(sedeRepository.findByActivoTrueAndId(id)).thenReturn(sede);
		when(instalacionRepository.findByActivoTrueAndSedeId(id)).thenReturn(List.of(new Instalacion()));
		when(instalacionMapper.listModelToListDTO(any())).thenReturn(List.of(new InstalacionDTO()));
		when(sedeMapper.modelToMapaDTO(eq(sede), any())).thenReturn(new SedePublicoDTO());

		SedePublicoDTO resultado = sedeService.getSedePublicoDTOById(id);

		assertThat(resultado).isNotNull();
		verify(sedeRepository).findByActivoTrueAndId(id);
	}
}