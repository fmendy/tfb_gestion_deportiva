package com.gestion.deportiva.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.MaestraDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

	public static String dateToMesAnnoString(Date date) {

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);

		// Obtienes el mes actual
		Month mes = Month.of(month);
		// Obtienes el nombre del mes
		String nombreMes = mes.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-ES"));
		return nombreMes + " " + year;
	}

	public static String dateToAnno(Date date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		cal.setTime(date);

		return Integer.toString(cal.get(Calendar.YEAR));
	}

	public static String getMessage(String key) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", Locale.getDefault());
		return resourceBundle.getString(key);
	}

	public static String getParameterSort(Sort sort) {
		if (sort != null && StringUtils.hasText(sort.toString()) && !sort.toString().equalsIgnoreCase("UNSORTED")) {
			String[] aux = sort.toString().split(" ");
			return "&sort=" + aux[0].trim().replace(":", "") + "," + aux[1].trim().replace(":", "");
		}
		return "";
	}

	public static <T extends MaestraDTO> List<T> addEmptyOptionIfMoreThanOneOption(List<T> list, Class<T> clazz) {
		if (list != null && list.size() > 1) {
			try {
				T emptyOption = clazz.getDeclaredConstructor().newInstance();
				emptyOption.setId(null);
				emptyOption.setNombre("");
				list.add(0, emptyOption);
			} catch (Exception e) {
				throw new RuntimeException("No se pudo crear instancia de clase hija de MaestraDTO: " + clazz.getName(),
						e);
			}
		}
		return list;
	}

	public static <T extends MaestraDTO> List<T> addEmptyOption(List<T> list, Class<T> clazz) {
		if (list != null && list.size() > 0) {
			try {
				T emptyOption = clazz.getDeclaredConstructor().newInstance();
				emptyOption.setId(null);
				emptyOption.setNombre("");
				list.add(0, emptyOption);
			} catch (Exception e) {
				throw new RuntimeException("No se pudo crear instancia de clase hija de MaestraDTO: " + clazz.getName(),
						e);
			}
		}
		return list;
	}

	public static Date dateSetHoraMinutoSegundoMilisegundo(Date date, int hora, int min, int sec, int milisec) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hora);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, sec);
		cal.set(Calendar.MILLISECOND, milisec);
		return cal.getTime();
	}

	public static String dateToString(Date date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}

	public static String dateToString(LocalDateTime date, String pattern) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
		return dtf.format(date);
	}

	public static Long booleanToLong(boolean boo) {
		return boo ? 1L : 0L;
	}

	public static boolean longToBoolean(Long l) {
		return (l != null && l == 1L);
	}

	public static String booleanToSiNo(boolean boo) {
		return (boo ? getMessage("global.si") : getMessage("global.no"));
	}

	public static String longToSiNo(Long lo) {
		return booleanToSiNo(longToBoolean(lo));
	}

	public static Long intToLong(int i) {
		return Long.parseLong(Integer.toString(i));
	}

	public static <T extends MaestraDTO> List<T> sortByNombre(List<T> lista) {
		Collections.sort(lista, Comparator.comparing(MaestraDTO::getNombre));
		return lista;
	}

	public static <T extends MaestraDTO, U extends Comparable<? super U>> List<T> sortByCampo(List<T> lista,
			Function<? super T, ? extends U> keyExtractor) {

		Collections.sort(lista, Comparator.comparing(keyExtractor));
		return lista;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	/**
	 * Genera un color pastel aleatorio pero consistente según una semilla (por
	 * ejemplo, UUID del conductor).
	 */
	public static ColorSet generatePastelColor(String seed) {
		if (seed == null)
			seed = String.valueOf(System.nanoTime());

		// Usamos el hash del seed como semilla del Random
		Random random = new Random(seed.hashCode());

		// Colores pastel: alto brillo y saturación baja
		int red = (random.nextInt(128) + 127);
		int green = (random.nextInt(128) + 127);
		int blue = (random.nextInt(128) + 127);

		String background = String.format("#%02x%02x%02x", red, green, blue);

		// Bordes ligeramente más oscuros
		String border = String.format("#%02x%02x%02x", Math.max(red - 40, 0), Math.max(green - 40, 0),
				Math.max(blue - 40, 0));

		// Texto negro o blanco según contraste (luminosidad)
		double luminance = (0.299 * red + 0.587 * green + 0.114 * blue) / 255;
		String text = luminance > 0.7 ? "#000000" : "#ffffff";

		return new ColorSet(background, border, text);
	}

	/**
	 * DTO simple para agrupar los colores del calendario.
	 */
	public static class ColorSet {
		public final String background;
		public final String border;
		public final String text;

		public ColorSet(String background, String border, String text) {
			this.background = background;
			this.border = border;
			this.text = text;
		}
	}

}
