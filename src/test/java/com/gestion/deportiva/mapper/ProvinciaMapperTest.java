package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ProvinciaDTO;
import com.gestion.deportiva.model.ComunidadAutonoma;
import com.gestion.deportiva.model.Provincia;

class ProvinciaMapperTest {

	private ProvinciaMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ProvinciaMapper();
	}

	@Test
	void modelToDTO() {
		ComunidadAutonoma comunidad = new ComunidadAutonoma();
		comunidad.setNombre("Andalucía");
		comunidad.setUuid("uuid-ccaa-andaluza");

		Provincia model = new Provincia();
		model.setId(10L);
		model.setNombre("Sevilla");
		model.setUuid("uuid-provincia-sevilla");
		model.setCodigoIne(41L);
		model.setComunidadAutonoma(comunidad);

		ProvinciaDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(10L);
		assertThat(dto.getNombre()).isEqualTo("Sevilla");
		assertThat(dto.getUuid()).isEqualTo("uuid-provincia-sevilla");
		assertThat(dto.getCodigoIne()).isEqualTo(41L);
		assertThat(dto.getComunidadAutonomaNombre()).isEqualTo("Andalucía");
		assertThat(dto.getComunidadAutonomaUuid()).isEqualTo("uuid-ccaa-andaluza");
	}

	@Test
	void listModelToListDTO() {
		ComunidadAutonoma comunidad = new ComunidadAutonoma();
		comunidad.setNombre("Comunidad");

		Provincia model = new Provincia();
		model.setId(1L);
		model.setNombre("Provincia 1");
		model.setComunidadAutonoma(comunidad);

		List<ProvinciaDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getId()).isEqualTo(1L);
		assertThat(dtos.get(0).getNombre()).isEqualTo("Provincia 1");
	}

	@Test
	void pageToPageDTO() {
		ComunidadAutonoma comunidad = new ComunidadAutonoma();
		comunidad.setNombre("Comunidad");

		Provincia model = new Provincia();
		model.setId(1L);
		model.setNombre("Provincia Page");
		model.setComunidadAutonoma(comunidad);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<Provincia> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<ProvinciaDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getNombre()).isEqualTo("Provincia Page");
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		ComunidadAutonoma comunidad = new ComunidadAutonoma();
		comunidad.setId(3L);

		ProvinciaDTO dto = new ProvinciaDTO();
		dto.setId(15L);
		dto.setUuid("uuid-new");
		dto.setNombre("Málaga");
		dto.setCodigoIne(29L);

		Provincia model = mapper.dtoToModel(dto, null, comunidad);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(15L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getNombre()).isEqualTo("Málaga");
		assertThat(model.getCodigoIne()).isEqualTo(29L);
		assertThat(model.getComunidadAutonoma()).isEqualTo(comunidad);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		ComunidadAutonoma comunidadNueva = new ComunidadAutonoma();
		comunidadNueva.setId(4L);

		Provincia model = new Provincia();
		model.setId(5L);
		model.setUuid("uuid-old");
		model.setNombre("Antiguo Nombre");

		ProvinciaDTO dto = new ProvinciaDTO();
		dto.setId(6L);
		dto.setUuid("uuid-updated");
		dto.setNombre("Nuevo Nombre");
		dto.setCodigoIne(18L);

		Provincia resultado = mapper.dtoToModel(dto, model, comunidadNueva);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(6L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getNombre()).isEqualTo("Nuevo Nombre");
		assertThat(resultado.getCodigoIne()).isEqualTo(18L);
		assertThat(resultado.getComunidadAutonoma()).isEqualTo(comunidadNueva);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		Provincia model = new Provincia();
		model.setUuid("uuid-original");

		ProvinciaDTO dto = new ProvinciaDTO();
		dto.setUuid("");

		Provincia resultado = mapper.dtoToModel(dto, model, new ComunidadAutonoma());

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		ComunidadAutonoma comunidad = new ComunidadAutonoma();
		comunidad.setId(2L);

		Provincia model = new Provincia();
		model.setId(1L);
		model.setNombre("Cádiz");
		model.setComunidadAutonoma(comunidad);

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(1L);
		assertThat(combos.get(0).getValue()).isEqualTo("Cádiz");
	}
}