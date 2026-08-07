package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.FranjaHorariaDTO;
import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.InstalacionDisponibilidadDTO;
import com.gestion.deportiva.dto.InstalacionPublicoDTO;
import com.gestion.deportiva.model.ComunidadAutonoma;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionConfiguracionReserva;
import com.gestion.deportiva.model.InstalacionHorario;
import com.gestion.deportiva.model.InstalacionHorarioEspecial;
import com.gestion.deportiva.model.InstalacionTipo;
import com.gestion.deportiva.model.Municipio;
import com.gestion.deportiva.model.Provincia;
import com.gestion.deportiva.model.Sede;

class InstalacionMapperTest {

	private InstalacionMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new InstalacionMapper();
	}

	@Test
	void modelToDTO() {
		Empresa empresa = new Empresa();
		empresa.setId(1L);
		empresa.setNombre("Empresa Central");

		Sede sede = new Sede();
		sede.setId(2L);
		sede.setNombre("Sede Norte");
		sede.setEmpresa(empresa);

		InstalacionTipo tipo = new InstalacionTipo();
		tipo.setId(3L);
		tipo.setNombre("Pádel");

		Instalacion model = new Instalacion();
		model.setId(10L);
		model.setNombre("Pista 1");
		model.setUuid("uuid-instalacion-10");
		model.setDescripcion("Pista cubierta de pádel");
		model.setSede(sede);
		model.setInstalacionTipo(tipo);

		InstalacionDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(10L);
		assertThat(dto.getNombre()).isEqualTo("Pista 1");
		assertThat(dto.getUuid()).isEqualTo("uuid-instalacion-10");
		assertThat(dto.getDescripcion()).isEqualTo("Pista cubierta de pádel");
		assertThat(dto.getSedeId()).isEqualTo(2L);
		assertThat(dto.getSedeNombre()).isEqualTo("Sede Norte");
		assertThat(dto.getInstalacionTipoId()).isEqualTo(3L);
		assertThat(dto.getInstalacionTipoNombre()).isEqualTo("Pádel");
		assertThat(dto.getEmpresaId()).isEqualTo(1L);
		assertThat(dto.getEmpresaNombre()).isEqualTo("Empresa Central");
		assertThat(dto.getEmpresaSedeInstalacionNombre()).isEqualTo("Empresa Central - Sede Norte - Pista 1");
	}

	@Test
	void listModelToListDTO() {
		Empresa empresa = new Empresa();
		empresa.setId(1L);
		empresa.setNombre("Empresa");

		Sede sede = new Sede();
		sede.setId(1L);
		sede.setNombre("Sede");
		sede.setEmpresa(empresa);

		InstalacionTipo tipo = new InstalacionTipo();
		tipo.setId(1L);
		tipo.setNombre("Tipo");

		Instalacion model = new Instalacion();
		model.setId(1L);
		model.setNombre("Instalacion");
		model.setSede(sede);
		model.setInstalacionTipo(tipo);

		List<InstalacionDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getNombre()).isEqualTo("Instalacion");
	}

	@Test
	void pageToPageDTO() {
		Empresa empresa = new Empresa();
		empresa.setId(1L);
		empresa.setNombre("Empresa");

		Sede sede = new Sede();
		sede.setId(1L);
		sede.setNombre("Sede");
		sede.setEmpresa(empresa);

		InstalacionTipo tipo = new InstalacionTipo();
		tipo.setId(1L);
		tipo.setNombre("Tipo");

		Instalacion model = new Instalacion();
		model.setId(1L);
		model.setNombre("Instalacion Page");
		model.setSede(sede);
		model.setInstalacionTipo(tipo);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<Instalacion> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<InstalacionDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getNombre()).isEqualTo("Instalacion Page");
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		InstalacionDTO dto = new InstalacionDTO();
		dto.setId(5L);
		dto.setUuid("uuid-new");
		dto.setNombre("Nueva Instalación");
		dto.setDescripcion("Desc");
		dto.setSedeId(2L);
		dto.setInstalacionTipoId(3L);

		Instalacion model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getNombre()).isEqualTo("Nueva Instalación");
		assertThat(model.getDescripcion()).isEqualTo("Desc");
		assertThat(model.getSede()).isNotNull();
		assertThat(model.getSede().getId()).isEqualTo(2L);
		assertThat(model.getInstalacionTipo()).isNotNull();
		assertThat(model.getInstalacionTipo().getId()).isEqualTo(3L);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		Instalacion model = new Instalacion();
		model.setId(1L);
		model.setUuid("uuid-old");
		model.setNombre("Antiguo");

		InstalacionDTO dto = new InstalacionDTO();
		dto.setId(2L);
		dto.setUuid("uuid-updated");
		dto.setNombre("Actualizado");
		dto.setSedeId(4L);
		dto.setInstalacionTipoId(5L);

		Instalacion resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getNombre()).isEqualTo("Actualizado");
		assertThat(resultado.getSede().getId()).isEqualTo(4L);
		assertThat(resultado.getInstalacionTipo().getId()).isEqualTo(5L);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		Instalacion model = new Instalacion();
		model.setUuid("uuid-original");

		InstalacionDTO dto = new InstalacionDTO();
		dto.setUuid("");
		dto.setSedeId(1L);
		dto.setInstalacionTipoId(1L);

		Instalacion resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		Instalacion model = new Instalacion();
		model.setId(1L);
		model.setNombre("Pista Central");

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(1L);
		assertThat(combos.get(0).getValue()).isEqualTo("Pista Central");
	}

	@Test
	void toPublicDTO() {
		ComunidadAutonoma ccaa = new ComunidadAutonoma();
		ccaa.setNombre("Comunidad de Madrid");

		Provincia provincia = new Provincia();
		provincia.setNombre("Madrid");
		provincia.setComunidadAutonoma(ccaa);

		Municipio municipio = new Municipio();
		municipio.setNombre("Madrid");
		municipio.setProvincia(provincia);

		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa Pública");

		Sede sede = new Sede();
		sede.setId(1L);
		sede.setNombre("Polideportivo Municipal");
		sede.setDireccion("Calle Mayor 1");
		sede.setMunicipio(municipio);
		sede.setEmpresa(empresa);
		sede.setLatitud("40.4168");
		sede.setLongitud("-3.7038");

		InstalacionTipo tipo = new InstalacionTipo();
		tipo.setNombre("Piscina");

		Instalacion instalacion = new Instalacion();
		instalacion.setId(5L);
		instalacion.setNombre("Piscina Olímpica");
		instalacion.setDescripcion("Piscina cubierta de 50m");
		instalacion.setSede(sede);
		instalacion.setInstalacionTipo(tipo);

		InstalacionHorario horario = new InstalacionHorario();
		horario.setDiaSemana(1L);
		horario.setHoraInicio(LocalTime.of(8, 0));
		horario.setHoraFin(LocalTime.of(22, 0));

		LocalDate hoy = LocalDate.now();
		InstalacionHorarioEspecial especial = new InstalacionHorarioEspecial();
		especial.setFecha(hoy);
		especial.setHoraInicio(LocalTime.of(9, 0));
		especial.setHoraFin(LocalTime.of(15, 0));
		especial.setCerrado(true);

		InstalacionPublicoDTO publicDto = mapper.toPublicDTO(instalacion, List.of(horario), List.of(especial));

		assertThat(publicDto).isNotNull();
		assertThat(publicDto.getId()).isEqualTo(5L);
		assertThat(publicDto.getNombre()).isEqualTo("Piscina Olímpica");
		assertThat(publicDto.getDescripcion()).isEqualTo("Piscina cubierta de 50m");
		assertThat(publicDto.getInstalacionTipoNombre()).isEqualTo("Piscina");
		assertThat(publicDto.getSedeNombre()).isEqualTo("Polideportivo Municipal");
		assertThat(publicDto.getSedeDireccion()).isEqualTo("Calle Mayor 1");
		assertThat(publicDto.getSedeMunicipioNombre()).isEqualTo("Madrid");
		assertThat(publicDto.getSedeMunicipioProvinciaNombre()).isEqualTo("Madrid");
		assertThat(publicDto.getSedeMunicipioProvinciaComunidadAutonomaNombre()).isEqualTo("Comunidad de Madrid");
		assertThat(publicDto.getEmpresaNombre()).isEqualTo("Empresa Pública");
		assertThat(publicDto.getSedeLatitud()).isEqualTo("40.4168");
		assertThat(publicDto.getSedeLongitud()).isEqualTo("-3.7038");
		assertThat(publicDto.getHorarioSemanal()).hasSize(1);
		assertThat(publicDto.getHorarioCalculado()).isNotEmpty();
	}

	@Test
	void toDisponibilidadDTO() {
		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa Deportes");

		ComunidadAutonoma ccaa = new ComunidadAutonoma();
		ccaa.setNombre("Andalucía");

		Provincia provincia = new Provincia();
		provincia.setNombre("Sevilla");
		provincia.setComunidadAutonoma(ccaa);

		Municipio municipio = new Municipio();
		municipio.setNombre("Sevilla");
		municipio.setProvincia(provincia);

		Sede sede = new Sede();
		sede.setNombre("Sede Sur");
		sede.setDireccion("Av. Constitución");
		sede.setMunicipio(municipio);
		sede.setEmpresa(empresa);

		InstalacionTipo tipo = new InstalacionTipo();
		tipo.setNombre("Campo de Fútbol");

		Instalacion instalacion = new Instalacion();
		instalacion.setId(8L);
		instalacion.setNombre("Campo 1");
		instalacion.setDescripcion("Césped artificial");
		instalacion.setSede(sede);
		instalacion.setInstalacionTipo(tipo);

		InstalacionConfiguracionReserva config = new InstalacionConfiguracionReserva();
		config.setDuracionMax(90L);
		config.setDuracionMin(45L);
		config.setIntervaloHorario(30L);

		List<FranjaHorariaDTO> franjas = List.of(new FranjaHorariaDTO());

		InstalacionDisponibilidadDTO disponibilidadDTO = mapper.toDisponibilidadDTO(instalacion, config, franjas);

		assertThat(disponibilidadDTO).isNotNull();
		assertThat(disponibilidadDTO.getId()).isEqualTo(8L);
		assertThat(disponibilidadDTO.getNombre()).isEqualTo("Campo 1");
		assertThat(disponibilidadDTO.getDescripcion()).isEqualTo("Césped artificial");
		assertThat(disponibilidadDTO.getInstalacionTipoNombre()).isEqualTo("Campo de Fútbol");
		assertThat(disponibilidadDTO.getSedeNombre()).isEqualTo("Sede Sur");
		assertThat(disponibilidadDTO.getSedeDireccion()).isEqualTo("Av. Constitución");
		assertThat(disponibilidadDTO.getSedeMunicipioNombre()).isEqualTo("Sevilla");
		assertThat(disponibilidadDTO.getSedeMunicipioProvinciaNombre()).isEqualTo("Sevilla");
		assertThat(disponibilidadDTO.getSedeMunicipioProvinciaComunidadAutonomaNombre()).isEqualTo("Andalucía");
		assertThat(disponibilidadDTO.getEmpresaNombre()).isEqualTo("Empresa Deportes");
		assertThat(disponibilidadDTO.getInstalacionConfiguracionReservaDuracionMax()).isEqualTo(90);
		assertThat(disponibilidadDTO.getInstalacionConfiguracionReservaDuracionMin()).isEqualTo(45);
		assertThat(disponibilidadDTO.getInstalacionConfiguracionReservaIntervaloHorario()).isEqualTo(30);
		assertThat(disponibilidadDTO.getListFranjaHoraria()).hasSize(1);
	}
}