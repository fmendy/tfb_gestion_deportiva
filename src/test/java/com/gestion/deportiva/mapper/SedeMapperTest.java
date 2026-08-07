package com.gestion.deportiva.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.SedeDTO;
import com.gestion.deportiva.dto.SedePublicoDTO;
import com.gestion.deportiva.model.ComunidadAutonoma;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Municipio;
import com.gestion.deportiva.model.Provincia;
import com.gestion.deportiva.model.Sede;

class SedeMapperTest {

	private SedeMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new SedeMapper();
	}

	@Test
	void modelToDTO() {
		ComunidadAutonoma ccaa = new ComunidadAutonoma();
		ccaa.setId(1L);
		ccaa.setNombre("Comunidad de Madrid");

		Provincia provincia = new Provincia();
		provincia.setId(2L);
		provincia.setNombre("Madrid");
		provincia.setComunidadAutonoma(ccaa);

		Municipio municipio = new Municipio();
		municipio.setId(3L);
		municipio.setNombre("Madrid");
		municipio.setProvincia(provincia);

		Empresa empresa = new Empresa();
		empresa.setId(4L);
		empresa.setNombre("Empresa Central");

		Sede model = new Sede();
		model.setId(10L);
		model.setNombre("Sede Norte");
		model.setUuid("uuid-sede-10");
		model.setEmail("norte@empresa.com");
		model.setUrl("https://norte.empresa.com");
		model.setLogo("logo.png");
		model.setDescripcion("Descripción de la sede norte");
		model.setEmpresa(empresa);
		model.setDireccion("Calle Principal 123");
		model.setLatitud("40.4168");
		model.setLongitud("-3.7038");
		model.setMunicipio(municipio);

		SedeDTO dto = mapper.modelToDTO(model);

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(10L);
		assertThat(dto.getNombre()).isEqualTo("Sede Norte");
		assertThat(dto.getUuid()).isEqualTo("uuid-sede-10");
		assertThat(dto.getEmail()).isEqualTo("norte@empresa.com");
		assertThat(dto.getUrl()).isEqualTo("https://norte.empresa.com");
		assertThat(dto.getLogo()).isEqualTo("logo.png");
		assertThat(dto.getDescripcion()).isEqualTo("Descripción de la sede norte");
		assertThat(dto.getEmpresaId()).isEqualTo(4L);
		assertThat(dto.getEmpresaNombre()).isEqualTo("Empresa Central");
		assertThat(dto.getDireccion()).isEqualTo("Calle Principal 123");
		assertThat(dto.getLatitud()).isEqualTo("40.4168");
		assertThat(dto.getLongitud()).isEqualTo("-3.7038");
		assertThat(dto.getMunicipioId()).isEqualTo(3L);
		assertThat(dto.getMunicipioNombre()).isEqualTo("Madrid");
		assertThat(dto.getProvinciaId()).isEqualTo(2L);
		assertThat(dto.getProvinciaNombre()).isEqualTo("Madrid");
		assertThat(dto.getComunidadAutonomaId()).isEqualTo(1L);

		assertThat(dto.getComunidadAutonomaNombre()).isEqualTo("Madrid");
		assertThat(dto.getEmpresaSedeNombre()).isEqualTo("Empresa Central - Sede Norte");
	}

	@Test
	void listModelToListDTO() {
		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa");

		Provincia provincia = new Provincia();
		provincia.setNombre("Provincia");
		provincia.setComunidadAutonoma(new ComunidadAutonoma());

		Municipio municipio = new Municipio();
		municipio.setNombre("Municipio");
		municipio.setProvincia(provincia);

		Sede model = new Sede();
		model.setId(1L);
		model.setNombre("Sede");
		model.setEmpresa(empresa);
		model.setMunicipio(municipio);

		List<SedeDTO> dtos = mapper.listModelToListDTO(List.of(model));

		assertThat(dtos).hasSize(1);
		assertThat(dtos.get(0).getId()).isEqualTo(1L);
		assertThat(dtos.get(0).getNombre()).isEqualTo("Sede");
	}

	@Test
	void pageToPageDTO() {
		Empresa empresa = new Empresa();
		empresa.setNombre("Empresa");

		Provincia provincia = new Provincia();
		provincia.setNombre("Provincia");
		provincia.setComunidadAutonoma(new ComunidadAutonoma());

		Municipio municipio = new Municipio();
		municipio.setNombre("Municipio");
		municipio.setProvincia(provincia);

		Sede model = new Sede();
		model.setId(1L);
		model.setNombre("Sede Page");
		model.setEmpresa(empresa);
		model.setMunicipio(municipio);

		PageRequest pageable = PageRequest.of(0, 10);
		Page<Sede> pageModel = new PageImpl<>(List.of(model), pageable, 1);

		Page<SedeDTO> pageDto = mapper.pageToPageDTO(pageModel);

		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getTotalElements()).isEqualTo(1);
		assertThat(pageDto.getContent()).hasSize(1);
		assertThat(pageDto.getContent().get(0).getNombre()).isEqualTo("Sede Page");
	}

	@Test
	void dtoToModelCreandoNuevoSiModelEsNull() {
		SedeDTO dto = new SedeDTO();
		dto.setId(5L);
		dto.setUuid("uuid-new");
		dto.setNombre("Nueva Sede");
		dto.setDescripcion("Desc");
		dto.setEmail("test@test.com");
		dto.setUrl("url");
		dto.setLogo("logo");
		dto.setDireccion("Dir");
		dto.setLatitud("10.0");
		dto.setLongitud("20.0");
		dto.setEmpresaId(2L);
		dto.setMunicipioId(3L);

		Sede model = mapper.dtoToModel(dto, null);

		assertThat(model).isNotNull();
		assertThat(model.getId()).isEqualTo(5L);
		assertThat(model.getUuid()).isEqualTo("uuid-new");
		assertThat(model.getNombre()).isEqualTo("Nueva Sede");
		assertThat(model.getDescripcion()).isEqualTo("Desc");
		assertThat(model.getEmail()).isEqualTo("test@test.com");
		assertThat(model.getUrl()).isEqualTo("url");
		assertThat(model.getLogo()).isEqualTo("logo");
		assertThat(model.getDireccion()).isEqualTo("Dir");
		assertThat(model.getLatitud()).isEqualTo("10.0");
		assertThat(model.getLongitud()).isEqualTo("20.0");
		assertThat(model.getEmpresa()).isNotNull();
		assertThat(model.getEmpresa().getId()).isEqualTo(2L);
		assertThat(model.getMunicipio()).isNotNull();
		assertThat(model.getMunicipio().getId()).isEqualTo(3L);
	}

	@Test
	void dtoToModelActualizandoModelExistente() {
		Sede model = new Sede();
		model.setId(1L);
		model.setUuid("uuid-old");
		model.setNombre("Antigua Sede");

		SedeDTO dto = new SedeDTO();
		dto.setId(2L);
		dto.setUuid("uuid-updated");
		dto.setNombre("Sede Actualizada");
		dto.setEmpresaId(4L);
		dto.setMunicipioId(5L);

		Sede resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado).isEqualTo(model);
		assertThat(resultado.getId()).isEqualTo(2L);
		assertThat(resultado.getUuid()).isEqualTo("uuid-updated");
		assertThat(resultado.getNombre()).isEqualTo("Sede Actualizada");
		assertThat(resultado.getEmpresa().getId()).isEqualTo(4L);
		assertThat(resultado.getMunicipio().getId()).isEqualTo(5L);
	}

	@Test
	void dtoToModelConUuidVacioNoLoModifica() {
		Sede model = new Sede();
		model.setUuid("uuid-original");

		SedeDTO dto = new SedeDTO();
		dto.setUuid("");
		dto.setEmpresaId(1L);
		dto.setMunicipioId(1L);

		Sede resultado = mapper.dtoToModel(dto, model);

		assertThat(resultado.getUuid()).isEqualTo("uuid-original");
	}

	@Test
	void listModelToListComboDTO() {
		Sede model = new Sede();
		model.setId(1L);
		model.setNombre("Sede Central");

		List<ComboDTO> combos = mapper.listModelToListComboDTO(List.of(model));

		assertThat(combos).hasSize(1);
		assertThat(combos.get(0).getKey()).isEqualTo(1L);
		assertThat(combos.get(0).getValue()).isEqualTo("Sede Central");
	}

	@Test
	void modelToMapaDTO() {
		ComunidadAutonoma ccaa = new ComunidadAutonoma();
		ccaa.setId(1L);
		ccaa.setNombre("Cataluña");

		Provincia provincia = new Provincia();
		provincia.setId(2L);
		provincia.setNombre("Barcelona");
		provincia.setComunidadAutonoma(ccaa);

		Municipio municipio = new Municipio();
		municipio.setId(3L);
		municipio.setNombre("Barcelona");
		municipio.setProvincia(provincia);

		Empresa empresa = new Empresa();
		empresa.setId(4L);
		empresa.setNombre("Empresa Catalana");

		Sede model = new Sede();
		model.setId(10L);
		model.setNombre("Sede Barcelona");
		model.setUuid("uuid-sede-bcn");
		model.setEmail("bcn@empresa.com");
		model.setUrl("https://bcn.empresa.com");
		model.setLogo("logo-bcn.png");
		model.setDescripcion("Sede en Barcelona");
		model.setEmpresa(empresa);
		model.setDireccion("Diagonal 100");
		model.setLatitud("41.3851");
		model.setLongitud("2.1734");
		model.setMunicipio(municipio);

		List<InstalacionDTO> instalaciones = List.of(new InstalacionDTO());

		SedePublicoDTO publicDto = mapper.modelToMapaDTO(model, instalaciones);

		assertThat(publicDto).isNotNull();
		assertThat(publicDto.getId()).isEqualTo(10L);
		assertThat(publicDto.getNombre()).isEqualTo("Sede Barcelona");
		assertThat(publicDto.getUuid()).isEqualTo("uuid-sede-bcn");
		assertThat(publicDto.getEmail()).isEqualTo("bcn@empresa.com");
		assertThat(publicDto.getUrl()).isEqualTo("https://bcn.empresa.com");
		assertThat(publicDto.getLogo()).isEqualTo("logo-bcn.png");
		assertThat(publicDto.getDescripcion()).isEqualTo("Sede en Barcelona");
		assertThat(publicDto.getEmpresaId()).isEqualTo(4L);
		assertThat(publicDto.getEmpresaNombre()).isEqualTo("Empresa Catalana");
		assertThat(publicDto.getDireccion()).isEqualTo("Diagonal 100");
		assertThat(publicDto.getLatitud()).isEqualTo("41.3851");
		assertThat(publicDto.getLongitud()).isEqualTo("2.1734");
		assertThat(publicDto.getMunicipioId()).isEqualTo(3L);
		assertThat(publicDto.getMunicipioNombre()).isEqualTo("Barcelona");
		assertThat(publicDto.getProvinciaId()).isEqualTo(2L);
		assertThat(publicDto.getProvinciaNombre()).isEqualTo("Barcelona");
		assertThat(publicDto.getComunidadAutonomaId()).isEqualTo(1L);
		assertThat(publicDto.getComunidadAutonomaNombre()).isEqualTo("Barcelona");
		assertThat(publicDto.getEmpresaSedeNombre()).isEqualTo("Empresa Catalana - Sede Barcelona");
		assertThat(publicDto.getListInstalacion()).hasSize(1);
	}
}