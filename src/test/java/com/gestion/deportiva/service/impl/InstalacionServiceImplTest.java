package com.gestion.deportiva.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;

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
import com.gestion.deportiva.dto.FranjaHorariaDTO;
import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.InstalacionPublicoDTO;
import com.gestion.deportiva.dto.filter.InstalacionPublicoFilter;
import com.gestion.deportiva.mapper.InstalacionMapper;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionConfiguracionReserva;
import com.gestion.deportiva.model.InstalacionHorario;
import com.gestion.deportiva.repository.InstalacionConfiguracionReservaRepository;
import com.gestion.deportiva.repository.InstalacionHorarioBloqueadoRepository;
import com.gestion.deportiva.repository.InstalacionHorarioEspecialRepository;
import com.gestion.deportiva.repository.InstalacionHorarioRepository;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.repository.ReservaRepository;
import com.gestion.deportiva.service.InstalacionHorarioService;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
class InstalacionServiceImplTest {

	@Mock
	private InstalacionRepository instalacionRepository;

	@Mock
	private InstalacionHorarioRepository instalacionHorarioRepository;

	@Mock
	private InstalacionHorarioEspecialRepository instalacionHorarioEspecialRepository;

	@Mock
	private InstalacionConfiguracionReservaRepository instalacionConfiguracionReservaRepository;

	@Mock
	private InstalacionHorarioBloqueadoRepository instalacionHorarioBloqueadoRepository;

	@Mock
	private EntityManager entityManager;

	@Mock
	private InstalacionMapper instalacionMapper;

	@Mock
	private ReservaRepository reservaRepository;

	@Mock
	private ReservaService reservaService;

	@Mock
	private InstalacionHorarioService instalacionHorarioService;

	@InjectMocks
	private InstalacionServiceImpl instalacionService;

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
	void findById() {
		Long id = 1L;
		Instalacion model = new Instalacion();
		InstalacionDTO dto = new InstalacionDTO();

		when(instalacionRepository.findByActivoTrueAndId(id)).thenReturn(model);
		when(instalacionMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionDTO result = instalacionService.findById(id);

		assertThat(result).isEqualTo(dto);
		verify(instalacionRepository).findByActivoTrueAndId(id);
	}

	@Test
	void findByUuid() {
		String uuid = "uuid-123";
		Instalacion model = new Instalacion();
		InstalacionDTO dto = new InstalacionDTO();

		when(instalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid)).thenReturn(model);
		when(instalacionMapper.modelToDTO(model)).thenReturn(dto);

		InstalacionDTO result = instalacionService.findByUuid(uuid);

		assertThat(result).isEqualTo(dto);
		verify(instalacionRepository).findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
	}

	@Test
	void getListByFilter() {
		InstalacionPublicoFilter filter = new InstalacionPublicoFilter();
		filter.setFecha(LocalDate.of(2026, 8, 1));
		filter.setHoraInicio(LocalTime.of(10, 0));
		Pageable pageable = PageRequest.of(0, 10);

		Instalacion instalacion = new Instalacion();
		instalacion.setId(1L);
		Page<Instalacion> pageModel = new PageImpl<>(List.of(instalacion));

		InstalacionConfiguracionReserva config = new InstalacionConfiguracionReserva();
		config.setDuracionMin(60L);

		when(instalacionRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(pageModel);
		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndId(1L)).thenReturn(config);
		when(instalacionHorarioService.estaAbierta(eq(1L), eq(filter.getFecha()), eq(filter.getHoraInicio()), eq(60L)))
				.thenReturn(true);
		when(reservaService.isFranjaHorariaDisponibleParaInstalacion(eq(filter.getFecha()), eq(filter.getHoraInicio()),
				eq(60L), eq(1L))).thenReturn(true);
		when(instalacionMapper.listModelToListDTO(any())).thenReturn(List.of(new InstalacionDTO()));

		List<InstalacionDTO> result = instalacionService.getListByFilter(filter, pageable);

		assertThat(result).hasSize(1);
	}

	@Test
	void eliminarPorId() {
		Long id = 1L;
		Instalacion instalacion = new Instalacion();
		instalacion.setActivo(true);

		when(instalacionRepository.findByActivoTrueAndId(id)).thenReturn(instalacion);
		when(reservaService.getListByFechaDesdeInstalacionIdAndReservaEstados(any(), eq(id), any()))
				.thenReturn(Collections.emptyList());

		instalacionService.eliminar(id);

		assertThat(instalacion.isActivo()).isFalse();
		verify(instalacionRepository).saveAndFlush(instalacion);
		verify(reservaService).cancelarReservasEmpresa(any());
	}

	@Test
	void getListComboDTO() {
		when(instalacionRepository.findByActivoTrue()).thenReturn(List.of(new Instalacion()));
		when(instalacionMapper.listModelToListComboDTO(any())).thenReturn(List.of(new ComboDTO()));

		List<ComboDTO> result = instalacionService.getListComboDTO();
		assertThat(result).hasSize(1);
	}

	@Test
	void getListDTOParaEmpleado() {
		securityUtilMockedStatic.when(() -> SecurityUtil.hasAuthority(any())).thenReturn(true);
		when(instalacionRepository.findByActivoTrue()).thenReturn(List.of(new Instalacion()));
		when(instalacionMapper.listModelToListDTO(any()))
				.thenReturn(new java.util.ArrayList<>(List.of(new InstalacionDTO())));

		List<InstalacionDTO> result = instalacionService.getListDTOParaEmpleado(null, null);

		assertThat(result).isNotEmpty();
	}

	@Test
	void getPublicoDTOById() {
		Long id = 1L;
		when(instalacionRepository.findByActivoTrueAndId(id)).thenReturn(new Instalacion());
		when(instalacionHorarioRepository.findByActivoTrueAndInstalacionId(id)).thenReturn(Collections.emptyList());
		when(instalacionHorarioEspecialRepository.findByActivoTrueAndInstalacionId(id))
				.thenReturn(Collections.emptyList());
		when(instalacionMapper.toPublicDTO(any(), any(), any())).thenReturn(new InstalacionPublicoDTO());

		InstalacionPublicoDTO result = instalacionService.getPublicoDTOById(id);

		assertThat(result).isNotNull();
	}

	@Test
	void calcularDisponibilidad() {
		Long id = 1L;
		LocalDate fecha = LocalDate.of(2026, 8, 1);

		InstalacionConfiguracionReserva config = new InstalacionConfiguracionReserva();
		config.setIntervaloHorario(30L);
		config.setDuracionMin(60L);
		config.setDuracionMax(120L);

		InstalacionHorario horario = new InstalacionHorario();
		horario.setHoraInicio(LocalTime.of(10, 0));
		horario.setHoraFin(LocalTime.of(12, 0));

		when(instalacionConfiguracionReservaRepository.findByActivoTrueAndInstalacionId(id)).thenReturn(config);
		when(instalacionHorarioEspecialRepository.findByActivoTrueAndInstalacionIdAndFecha(id, fecha))
				.thenReturn(Collections.emptyList());
		when(instalacionHorarioBloqueadoRepository.findByActivoTrueAndInstalacionIdAndFecha(id, fecha))
				.thenReturn(Collections.emptyList());
		when(reservaRepository.findByInstalacionIdAndFechaAndReservaEstadoNombreIn(eq(id), eq(fecha), any()))
				.thenReturn(Collections.emptyList());
		when(instalacionHorarioRepository.findByActivoTrueAndInstalacionIdAndDiaSemana(eq(id), anyLong()))
				.thenReturn(List.of(horario));

		List<FranjaHorariaDTO> disponibilidades = instalacionService.calcularDisponibilidad(id, fecha);

		assertThat(disponibilidades).isNotEmpty();
		// Should have slots starting at 10:00, 10:30, 11:00
		assertThat(disponibilidades).hasSize(3);
	}
}