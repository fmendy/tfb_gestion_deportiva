package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.data.domain.Sort;

import com.gestion.deportiva.dto.MaestraDTO;

class UtilsTest {

	private MockedStatic<ResourceBundle> resourceBundleMockedStatic;

	@BeforeEach
	void setUp() {
		resourceBundleMockedStatic = mockStatic(ResourceBundle.class);
		ResourceBundle resourceBundle = mock(ResourceBundle.class);
		when(resourceBundle.getString("global.si")).thenReturn("Sí");
		when(resourceBundle.getString("global.no")).thenReturn("No");
		when(resourceBundle.getString("test.key")).thenReturn("Texto de prueba");

		resourceBundleMockedStatic.when(() -> ResourceBundle.getBundle(eq("messages"), any(Locale.class)))
				.thenReturn(resourceBundle);
	}

	@AfterEach
	void tearDown() {
		resourceBundleMockedStatic.close();
	}

	@SuppressWarnings("serial")
	static class DummyMaestraDTO extends MaestraDTO {
		public DummyMaestraDTO() {
		}

		public DummyMaestraDTO(Long id, String nombre) {
			setId(id);
			setNombre(nombre);
		}
	}

	@Test
	void dateToMesAnnoString() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		cal.set(2026, Calendar.JUNE, 15);
		Date date = cal.getTime();

		String resultado = Utils.dateToMesAnnoString(date);

		assertThat(resultado).isNotNull();
		assertThat(resultado).contains("2026");
	}

	@Test
	void dateToAnno() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		cal.set(2026, Calendar.MAY, 10);

		String resultado = Utils.dateToAnno(cal.getTime());

		assertThat(resultado).isEqualTo("2026");
	}

	@Test
	void getMessage() {
		String mensaje = Utils.getMessage("test.key");

		assertThat(mensaje).isEqualTo("Texto de prueba");
	}

	@Test
	void getParameterSort() {
		assertThat(Utils.getParameterSort(null)).isEmpty();
		assertThat(Utils.getParameterSort(Sort.unsorted())).isEmpty();

		Sort sort = Sort.by("nombre").ascending();
		assertThat(Utils.getParameterSort(sort)).isEqualTo("&sort=nombre,ASC");
	}

	@Test
	void addEmptyOptionIfMoreThanOneOption() {
		List<DummyMaestraDTO> list = new ArrayList<>();
		list.add(new DummyMaestraDTO(1L, "Uno"));
		list.add(new DummyMaestraDTO(2L, "Dos"));

		List<DummyMaestraDTO> resultado = Utils.addEmptyOptionIfMoreThanOneOption(list, DummyMaestraDTO.class);

		assertThat(resultado).hasSize(3);
		assertThat(resultado.get(0).getId()).isNull();
		assertThat(resultado.get(0).getNombre()).isEmpty();

		List<DummyMaestraDTO> listUnica = new ArrayList<>();
		listUnica.add(new DummyMaestraDTO(1L, "Uno"));
		Utils.addEmptyOptionIfMoreThanOneOption(listUnica, DummyMaestraDTO.class);
		assertThat(listUnica).hasSize(1);
	}

	@Test
	void addEmptyOption() {
		List<DummyMaestraDTO> list = new ArrayList<>();
		list.add(new DummyMaestraDTO(1L, "Uno"));

		List<DummyMaestraDTO> resultado = Utils.addEmptyOption(list, DummyMaestraDTO.class);

		assertThat(resultado).hasSize(2);
		assertThat(resultado.get(0).getId()).isNull();
		assertThat(resultado.get(0).getNombre()).isEmpty();

		List<DummyMaestraDTO> listVacia = new ArrayList<>();
		Utils.addEmptyOption(listVacia, DummyMaestraDTO.class);
		assertThat(listVacia).isEmpty();
	}

	@Test
	void dateSetHoraMinutoSegundoMilisegundo() {
		Calendar cal = Calendar.getInstance();
		cal.set(2026, Calendar.AUGUST, 7, 0, 0, 0);

		Date dateModificada = Utils.dateSetHoraMinutoSegundoMilisegundo(cal.getTime(), 14, 30, 45, 500);

		Calendar calMod = Calendar.getInstance();
		calMod.setTime(dateModificada);

		assertThat(calMod.get(Calendar.HOUR_OF_DAY)).isEqualTo(14);
		assertThat(calMod.get(Calendar.MINUTE)).isEqualTo(30);
		assertThat(calMod.get(Calendar.SECOND)).isEqualTo(45);
		assertThat(calMod.get(Calendar.MILLISECOND)).isEqualTo(500);
	}

	@Test
	void dateToStringConDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(2026, Calendar.AUGUST, 7);

		String str = Utils.dateToString(cal.getTime(), "yyyy-MM-dd");

		assertThat(str).startsWith("2026-08-07");
	}

	@Test
	void dateToStringConLocalDateTime() {
		LocalDateTime ldt = LocalDateTime.of(2026, 8, 7, 10, 15);

		String str = Utils.dateToString(ldt, "yyyy-MM-dd HH:mm");

		assertThat(str).isEqualTo("2026-08-07 10:15");
	}

	@Test
	void conversionesBooleanLongYSiNo() {
		assertThat(Utils.booleanToLong(true)).isEqualTo(1L);
		assertThat(Utils.booleanToLong(false)).isEqualTo(0L);

		assertThat(Utils.longToBoolean(1L)).isTrue();
		assertThat(Utils.longToBoolean(0L)).isFalse();
		assertThat(Utils.longToBoolean(null)).isFalse();

		assertThat(Utils.booleanToSiNo(true)).isEqualTo("Sí");
		assertThat(Utils.booleanToSiNo(false)).isEqualTo("No");

		assertThat(Utils.longToSiNo(1L)).isEqualTo("Sí");
		assertThat(Utils.longToSiNo(0L)).isEqualTo("No");

		assertThat(Utils.intToLong(42)).isEqualTo(42L);
	}

	@Test
	void ordenacionYDistinct() {
		List<DummyMaestraDTO> list = new ArrayList<>();
		list.add(new DummyMaestraDTO(2L, "Carlos"));
		list.add(new DummyMaestraDTO(1L, "Ana"));

		Utils.sortByNombre(list);
		assertThat(list.get(0).getNombre()).isEqualTo("Ana");
		assertThat(list.get(1).getNombre()).isEqualTo("Carlos");

		Utils.sortByCampo(list, DummyMaestraDTO::getId);
		assertThat(list.get(0).getId()).isEqualTo(1L);
		assertThat(list.get(1).getId()).isEqualTo(2L);

		List<DummyMaestraDTO> duplicados = List.of(new DummyMaestraDTO(1L, "Ana"), new DummyMaestraDTO(2L, "Ana"),
				new DummyMaestraDTO(3L, "Pedro"));

		List<DummyMaestraDTO> filtrados = duplicados.stream().filter(Utils.distinctByKey(DummyMaestraDTO::getNombre))
				.toList();

		assertThat(filtrados).hasSize(2);
	}

	@Test
	void generatePastelColor() {
		Utils.ColorSet colorSetConSeed = Utils.generatePastelColor("test-seed");
		assertThat(colorSetConSeed.background).isNotNull();
		assertThat(colorSetConSeed.border).isNotNull();
		assertThat(colorSetConSeed.text).isNotNull();

		Utils.ColorSet colorSetSinSeed = Utils.generatePastelColor(null);
		assertThat(colorSetSinSeed.background).isNotNull();
	}

	@Test
	void generarStringAleatorio() {
		String aleatorio = Utils.generarStringAleatorio(12);

		assertThat(aleatorio).hasSize(12);
	}

	@Test
	void appendParam() {
		StringBuilder sb = new StringBuilder("/test?");

		Utils.appendParam(sb, "nombre", "Juan");
		Utils.appendParam(sb, "vacio", "");
		Utils.appendParam(sb, "nulo", null);
		Utils.appendParam(sb, "ids", List.of(1, 2, 3));

		String resultado = sb.toString();

		assertThat(resultado).contains("nombre=Juan");
		assertThat(resultado).doesNotContain("vacio=");
		assertThat(resultado).doesNotContain("nulo=");
		assertThat(resultado).contains("ids=1, 2, 3");
	}
}