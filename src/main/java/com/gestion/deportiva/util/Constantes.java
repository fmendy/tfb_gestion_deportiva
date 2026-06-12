package com.gestion.deportiva.util;

public class Constantes {

	public static final String PAGE_TITLE = "pageTitle";

	public static final String USERNAME = "username";

	public static final String ANNO = "anno";

	public static final String ROLE = "ROLE";

	public static final String GUION_BAJO = "_";

	public static final String GUION = "-";

	public static final String ESPACION_BLANCO = " ";

	public static final String BARRA = "/";

	public static final String DOS_PUNTOS = ":";

	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

	public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

	public static final String DATE_FORMAT_HH_MM = "HH:mm";

	public static final String HTTP_STATUS = "httpStatus";

	public static final String CUSTOM_ERROR = "customError";

	public static final int CUSTOM_ERROR_CODE = 555;


	public static class Rol {
		public static final String ADMINISTRADOR = "ADMINISTRADOR";

		public static final String USUARIO_CLIENTE = "USUARIO_CLIENTE";

		public static final String USUARIO_EMPRESA = "USUARIO_EMPRESA";

		public static final String USUARIO_SEDE = "USUARIO_SEDE";

		public static final String USUARIO_INSTALACION = "USUARIO_INSTALACION";

	}

	public static class Permiso {
		
		public static final String GESTION_GLOBAL = "GESTION_GLOBAL";

		public static class Usuario {
			public static final String GESTION_USUARIO_GLOBAL = "GESTION_USUARIO_GLOBAL";
			
			public static final String GESTION_USUARIO_EMPRESA = "GESTION_USUARIO_EMPRESA";
			
			public static final String GESTION_USUARIO_SEDE = "GESTION_USUARIO_SEDE";
			
			public static final String GESTION_USUARIO_INSTALACION = "GESTION_USUARIO_INSTALACION";
			
			public static final String MI_PERFIL = "MI_PERFIL";

		}
		
		public static class Localizacion {

			public static final String GESTION_EMPRESA = "GESTION_EMPRESA";
			
			public static final String GESTION_SEDE = "GESTION_SEDE";
			
			public static final String GESTION_INSTALACION = "GESTION_INSTALACION";

		}

	}

	public static class Role {
		public static final String ADMINISTRADOR = "ROLE_ADMINISTRADOR";

		public static final String USUARIO_CLIENTE = "ROLE_USUARIO_CLIENTE";

		public static final String USUARIO_EMPRESA = "ROLE_USUARIO_EMPRESA";

		public static final String USUARIO_SEDE = "ROLE_USUARIO_SEDE";

		public static final String USUARIO_INSTALACION = "ROLE_USUARIO_INSTALACION";

	}

	public static class Visibilidad {
		public static final String PUBLICO = "PUBLICO";

		public static final String PRIVADO = "PRIVADO";
	}

}
