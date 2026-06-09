package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;

import com.gestion.deportiva.dto.BreadcrumbDTO;

public class BreadcrumbBuilder {

	private final List<BreadcrumbDTO> breadcrumbs = new ArrayList<>();

	private BreadcrumbBuilder() {
	}

	public static BreadcrumbBuilder start() {
		return new BreadcrumbBuilder();
	}

	/**
	 * Añade un breadcrumb con texto fijo o clave i18n. Si label empieza con
	 * "breadcrumb." se traduce automáticamente.
	 * 
	 * @param label texto o clave i18n
	 * @param url   url del breadcrumb (null si es el último)
	 */
	public BreadcrumbBuilder add(String label, String url) {
		String translated = label;
		if (label != null && label.startsWith("breadcrumb.")) {
			translated = Utils.getMessage(label); // Traduce automáticamente
		}
		breadcrumbs.add(new BreadcrumbDTO(translated, url));
		return this;
	}

	/** Añade un breadcrumb de home (Inicio) al inicio de la lista */
	public BreadcrumbBuilder includeHome() {
		breadcrumbs.add(0, new BreadcrumbDTO(Utils.getMessage("breadcrumb.inicio"), "/"));
		return this;
	}

	public List<BreadcrumbDTO> build() {
		return breadcrumbs;
	}
}