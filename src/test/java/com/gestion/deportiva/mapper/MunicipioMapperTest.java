package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.MunicipioDTO;
import com.gestion.deportiva.model.ComunidadAutonoma;
import com.gestion.deportiva.model.Municipio;
import com.gestion.deportiva.model.Provincia;

class MunicipioMapperTest {

	private MunicipioMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new MunicipioMapper();
	}

	@Test
	void modelToDTO() {
		ComunidadAutonoma comunidad = new ComunidadAutonoma();
		comunidad.setNombre("Comunidad Valenciana");
		comunidad.setUuid("uuid-ccaa");

		Provincia provincia = new Provincia();
		provincia.setNombre("Valencia");
		provincia.setUuid("uuid-provincia");
		provincia.setComunidadAutonoma(comunidad);

		Municipio model = new Municipio();
		model.setId(10L);
		model.setNombre("Gandia");
		model.setUuid("uuid-municipio");
		model.setCodigoIne(46131L);
		model.setDc(6L);
		model.setProvincia(provincia);

		MunicipioDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(10L);
		assertThat(dto.getNombre()).isEqualTo("Gandia");
		assertThat(dto.getUuid()).isEqualTo("uuid-municipio");
		assertThat(dto.getCodigoIne()).isEqualTo(46131L);
		assertThat(dto.getDc()).isEqualTo(6L);
		assertThat(dto.getComunidadAutonomaNombre()).isEqualTo("Comunidad Valenciana");
		assertThat(dto.getComunidadAutonomaUuid()).isEqualTo("uuid-ccaa");
		assertThat(dto.getProvinciaNombre()).isEqualTo("Valencia");
		assertThat(dto.getProvinciaUuid()).isEqualTo("uuid-provincia");
	}

	@Test
	void listModelToListDTO() {
		ComunidadAutonoma comunidad = new ComunidadAutonoma();
		comunidad.setNombre("CCAA");
		Provincia provincia = new Provincia();
		provincia.setNombre("Provincia");
		provincia.setComunidadAutonoma(comunidad);

		Municipio model = new Municipio();
		model.setId(1L);
		model.setNombre("Municipio 1");
		model.setProvincia(provincia);

		List<MunicipioDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getId()).isEqualTo(1L);
		assertThat(dtos.get(0).getNombre()).isEqualTo("Municipio 1");
	}

	@Test
	void pageToPageDTO() {
		ComunidadAutonoma comunidad = new ComunidadAutonoma();
		comunidad.setNombre("CCAA");
		Provincia provincia = new Provincia();
		provincia.setNombre("Provincia");
		provincia.setComunidadAutonoma(comunidad);

		Municipio model = new Municipio();
		model.setId(1L);
		model.setNombre("Municipio Page");
		model.setProvincia(provincia);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<Municipio> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<MunicipioDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getNombre()).isEqualTo("Municipio Page");
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		Provincia provincia = new Provincia();
		provincia.setId(5L);

		MunicipioDTO dto = new MunicipioDTO();
		dto.setId(20L);
		dto.setUuid("uuid-new");
		dto.setNombre("Alzira");
		dto.setCodigoIne(46039L);
		dto.setDc(2L);

		Municipio model = mapper.dtoToModel(dto, null, provincia);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(20L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getNombre()).isEqualTo("Alzira");
		assertThat(model.getCodigoIne()).isEqualTo(46039L);
		assertThat(model.getDc()).isEqualTo(2L);
		assertThat(model.getProvincia()).isEqualTo(provincia);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		Provincia provinciaNueva = new Provincia();
		provinciaNueva.setId(8L);

		Municipio model = new Municipio();
		model.setId(5L);
		model.setUuid("uuid-old");
		model.setNombre("Antiguo Nombre");

		MunicipioDTO dto = new MunicipioDTO();
		dto.setId(6L);
		dto.setUuid("uuid-updated");
		dto.setNombre("Nuevo Nombre");
		dto.setCodigoIne(46999L);
		dto.setDc(9L);

		Municipio resultado = mapper.dtoToModel(dto, model, provinciaNueva);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(6L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getNombre()).isEqualTo("Nuevo Nombre");
		assertThat(resultado.getCodigoIne()).isEqualTo(46999L);
		assertThat(resultado.getDc()).isEqualTo(9L);
		assertThat(resultado.getProvincia()).isEqualTo(provinciaNueva);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		Municipio model = new Municipio();
		model.setUuid("uuid-original");

		MunicipioDTO dto = new MunicipioDTO();
		dto.setUuid("");

		Municipio resultado = mapper.dtoToModel(dto, model, new Provincia());

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		ComunidadAutonoma comunidad = new ComunidadAutonoma();
		comunidad.setId(3L);

		Provincia provincia = new Provincia();
		provincia.setId(12L);
		provincia.setComunidadAutonoma(comunidad);

		Municipio model = new Municipio();
		model.setId(1L);
		model.setNombre("Valencia");
		model.setProvincia(provincia);

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(1L);
		assertThat(combos.get(0).getValue()).isEqualTo("Valencia");
	}
}