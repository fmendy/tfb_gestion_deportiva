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

	public static final int MAX_GRUPOS_USUARIO = 3;

	public static class Rol {
		public static final String ADMINISTRADOR = "ADMINISTRADOR";

		public static final String GESTOR = "GESTOR";

		public static final String CONDUCTOR = "CONDUCTOR";

	}

	public static class Permiso {

		public static class Usuario {
			public static final String READ = "USUARIO_READ";

			public static final String READ_ALL = "USUARIO_READ_ALL";

			public static final String WRITE = "USUARIO_WRITE";

			public static final String WRITE_ALL = "USUARIO_WRITE_ALL";

			public static final String DELETE = "USUARIO_DELETE";

			public static final String DELETE_ALL = "USUARIO_DELETE_ALL";
		}

		public static class UsuarioDemarcacion {
			public static final String READ = "USUARIO_DEMARCACION_READ";

			public static final String READ_ALL = "USUARIO_DEMARCACION_READ_ALL";

			public static final String WRITE = "USUARIO_DEMARCACION_WRITE";

			public static final String WRITE_ALL = "USUARIO_DEMARCACION_WRITE_ALL";

			public static final String DELETE = "USUARIO_DEMARCACION_DELETE";

			public static final String DELETE_ALL = "USUARIO_DEMARCACION_DELETE_ALL";
		}

		public static class UsuarioRol {
			public static final String READ = "USUARIO_ROL_READ";

			public static final String READ_ALL = "USUARIO_ROL_READ_ALL";

			public static final String WRITE = "USUARIO_ROL_WRITE";

			public static final String WRITE_ALL = "USUARIO_ROL_WRITE_ALL";

			public static final String DELETE = "USUARIO_ROL_DELETE";

			public static final String DELETE_ALL = "USUARIO_ROL_DELETE_ALL";
		}

		public static class GestionMaestra {
			public static final String READ = "	GESTION_MAESTRA_READ";

			public static final String WRITE = "GESTION_MAESTRA_WRITE";

			public static final String DELETE = "GESTION_MAESTRA_DELETE";
		}

		public static class ComunidadAutonoma {
			public static final String READ = "COMUNIDAD_AUTONOMA_READ";

			public static final String WRITE = "COMUNIDAD_AUTONOMA_WRITE";

			public static final String DELETE = "COMUNIDAD_AUTONOMA_DELETE";
		}

		public static class Provincia {
			public static final String READ = "PROVINCIA_READ";

			public static final String WRITE = "PROVINCIA_WRITE";

			public static final String DELETE = "PROVINCIA_DELETE";
		}

		public static class Municipio {
			public static final String READ = "	MUNICIPIO_READ";

			public static final String WRITE = "MUNICIPIO_WRITE";

			public static final String DELETE = "MUNICIPIO_DELETE";
		}

		public static class TipoEmpresa {
			public static final String READ = "	TIPO_EMPRESA_MAESTRA_READ";

			public static final String WRITE = "TIPO_EMPRESA_WRITE";

			public static final String DELETE = "TIPO_EMPRESA_DELETE";
		}

		public static class Empresa {
			public static final String READ = "	EMPRESA_MAESTRA_READ";

			public static final String WRITE = "EMPRESA_WRITE";

			public static final String DELETE = "EMPRESA_DELETE";
		}

		public static class Sede {
			public static final String READ = "SEDE_READ";

			public static final String READ_ALL = "SEDE_READ_ALL";

			public static final String WRITE = "SEDE_WRITE";

			public static final String WRITE_ALL = "SEDE_WRITE_ALL";

			public static final String DELETE = "SEDE_DELETE";

			public static final String DELETE_ALL = "SEDE_DELETE_ALL";
		}

		public static class Demarcacion {
			public static final String READ = "DEMARCACION_READ";

			public static final String READ_ALL = "DEMARCACION_READ_ALL";

			public static final String WRITE = "DEMARCACION_WRITE";

			public static final String WRITE_ALL = "DEMARCACION_WRITE_ALL";

			public static final String DELETE = "DEMARCACION_DELETE";

			public static final String DELETE_ALL = "DEMARCACION_DELETE_ALL";
		}

		public static class Maestra {
			public static class Trayecto {

				public static class GestionMaestraTrayecto {
					public static final String READ = "MAESTRA_TRAYECTO_READ";

					public static final String WRITE = "MAESTRA_TRAYECTO_WRITE";

					public static final String DELETE = "MAESTRA_TRAYECTO_DELETE";
				}

				public static class EstadoSolicitud {
					public static final String READ = "	MAESTRA_TRAYECTO_ESTADO_SOLICITUD_READ";

					public static final String WRITE = "MAESTRA_TRAYECTO_ESTADO_SOLICITUD_WRITE";

					public static final String DELETE = "MAESTRA_TRAYECTO_ESTADO_SOLICITUD_DELETE";
				}

			}

			public static class Vehiculo {
				public static class GestionMaestraVehiculo {
					public static final String READ = "	GESTION_MAESTRA_VEHICULO_READ";

					public static final String WRITE = "GESTION_MAESTRA_VEHICULO_WRITE";

					public static final String DELETE = "GESTION_MAESTRA_VEHICULO_DELETE";
				}

				public static class Marca {
					public static final String READ = "	MAESTRA_VEHICULO_MARCA_READ";

					public static final String WRITE = "MAESTRA_VEHICULO_MARCA_WRITE";

					public static final String DELETE = "MAESTRA_VEHICULO_MARCA_DELETE";
				}

				public static class Estado {
					public static final String READ = "	MAESTRA_VEHICULO_ESTADO_READ";

					public static final String WRITE = "MAESTRA_VEHICULO_ESTADO_WRITE";

					public static final String DELETE = "MAESTRA_VEHICULO_ESTADO_DELETE";
				}

				public static class Equipamiento {
					public static final String READ = "	MAESTRA_VEHICULO_EQUIPAMIENTO_READ";

					public static final String WRITE = "MAESTRA_VEHICULO_EQUIPAMIENTO_WRITE";

					public static final String DELETE = "MAESTRA_VEHICULO_EQUIPAMIENTO_DELETE";
				}

				public static class Modelo {
					public static final String READ = "	MAESTRA_VEHICULO_MODELO_READ";

					public static final String WRITE = "MAESTRA_VEHICULO_MODELO_WRITE";

					public static final String DELETE = "MAESTRA_VEHICULO_MODELO_DELETE";
				}

				public static class TipoVehiculo {
					public static final String READ = "	MAESTRA_VEHICULO_TIPO_VEHICULO_READ";

					public static final String WRITE = "MAESTRA_VEHICULO_TIPO_VEHICULO_WRITE";

					public static final String DELETE = "MAESTRA_VEHICULO_TIPO_VEHICULO_DELETE";
				}

				public static class TipoMotor {
					public static final String READ = "	MAESTRA_VEHICULO_TIPO_MOTOR_READ";

					public static final String WRITE = "MAESTRA_VEHICULO_TIPO_MOTOR_WRITE";

					public static final String DELETE = "MAESTRA_VEHICULO_TIPO_MOTOR_DELETE";
				}

				public static class DistintivoAmbiental {
					public static final String READ = "	MAESTRA_VEHICULO_DISTINTIVO_AMBIENTAL_READ";

					public static final String WRITE = "MAESTRA_VEHICULO_DISTINTIVO_AMBIENTAL_WRITE";

					public static final String DELETE = "MAESTRA_VEHICULO_DISTINTIVO_AMBIENTAL_DELETE";
				}

				public static class ResultadoITV {
					public static final String READ = "	MAESTRA_VEHICULO_RESULTADO_ITV_READ";

					public static final String WRITE = "MAESTRA_VEHICULO_RESULTADO_ITV_WRITE";

					public static final String DELETE = "MAESTRA_VEHICULO_RESULTADO_ITV_DELETE";
				}
				
				public static class LugarRepostaje {
					public static final String READ = "	MAESTRA_VEHICULO_LUGAR_REPOSTAJE_READ";

					public static final String WRITE = "MAESTRA_VEHICULO_LUGAR_REPOSTAJE_WRITE";

					public static final String DELETE = "MAESTRA_VEHICULO_LUGAR_REPOSTAJE_DELETE";
				}

			}

		}

		public static class Tr {

			public static class Trayecto {
				public static final String READ = "TRAYECTO_READ";

				public static final String READ_DEMARCACION = "TRAYECTO_READ_DEMARCACION";
				
				public static final String READ_ALL = "TRAYECTO_READ_ALL";

				public static final String WRITE = "TRAYECTO_WRITE";

				public static final String WRITE_DEMARCACION = "TRAYECTO_WRITE_DEMARCACION";
				
				public static final String WRITE_ALL = "TRAYECTO_WRITE_ALL";

				public static final String DELETE = "TRAYECTO_DELETE";

				public static final String DELETE_DEMARCACION = "TRAYECTO_DELETE_DEMARCACION";
				
				public static final String DELETE_ALL = "TRAYECTO_DELETE_ALL";
			}

			public static class Solicitud {
				public static final String READ = "TRAYECTO_SOLICITUD_READ";

				public static final String READ_DEMARCACION = "TRAYECTO_SOLICITUD_READ_DEMARCACION";

				public static final String READ_ALL = "TRAYECTO_SOLICITUD_READ_ALL";

				public static final String WRITE = "TRAYECTO_SOLICITUD_WRITE";

				public static final String WRITE_DEMARCACION = "TRAYECTO_SOLICITUD_WRITE_DEMARCACION";

				public static final String WRITE_ALL = "TRAYECTO_SOLICITUD_WRITE_ALL";

				public static final String DELETE = "TRAYECTO_SOLICITUD_DELETE";

				public static final String DELETE_DEMARCACION = "TRAYECTO_SOLICITUD_DELETE_DEMARCACION";

				public static final String DELETE_ALL = "TRAYECTO_SOLICITUD_DELETE_ALL";
								
				public static final String ACEPTAR = "TRAYECTO_SOLICITUD_ACEPTAR";
				
				public static final String DENEGAR = "TRAYECTO_SOLICITUD_DENEGAR";
			}
		}

		public static class Vh {

			public static class Vehiculo {
				public static final String READ = "VEHICULO_READ";

				public static final String READ_ALL = "VEHICULO_READ_ALL";

				public static final String WRITE = "VEHICULO_WRITE";

				public static final String WRITE_ALL = "VEHICULO_WRITE_ALL";

				public static final String DELETE = "VEHICULO_DELETE";

				public static final String DELETE_ALL = "VEHICULO_DELETE_ALL";
			}

			public static class ITV {
				public static final String READ = "VEHICULO_ITV_READ";

				public static final String READ_ALL = "VEHICULO_ITV_READ_ALL";

				public static final String WRITE = "VEHICULO_ITV_WRITE";

				public static final String WRITE_ALL = "VEHICULO_ITV_WRITE_ALL";

				public static final String DELETE = "VEHICULO_ITV_DELETE";

				public static final String DELETE_ALL = "VEHICULO_ITV_DELETE_ALL";
			}

			public static class Equipamiento {
				public static final String READ = "VEHICULO_EQUIPAMIENTO_READ";

				public static final String READ_ALL = "VEHICULO_EQUIPAMIENTO_READ_ALL";

				public static final String WRITE = "VEHICULO_EQUIPAMIENTO_WRITE";

				public static final String WRITE_ALL = "VEHICULO_EQUIPAMIENTO_WRITE_ALL";

				public static final String DELETE = "VEHICULO_EQUIPAMIENTO_DELETE";

				public static final String DELETE_ALL = "VEHICULO_EQUIPAMIENTO_DELETE_ALL";
			}

			public static class Repostaje {
				public static final String READ = "VEHICULO_REPOSTAJE_READ";

				public static final String READ_ALL = "VEHICULO_REPOSTAJE_READ_ALL";

				public static final String WRITE = "VEHICULO_REPOSTAJE_WRITE";

				public static final String WRITE_ALL = "VEHICULO_REPOSTAJE_WRITE_ALL";

				public static final String DELETE = "VEHICULO_REPOSTAJE_DELETE";

				public static final String DELETE_ALL = "VEHICULO_REPOSTAJE_DELETE_ALL";
			}

		}

	}

	public static class Role {
		public static final String ADMINISTRADOR = "ROLE_ADMINISTRADOR";

		public static final String USUARIO = "ROLE_USUARIO";

	}

	public static class Visibilidad {
		public static final String PUBLICO = "PUBLICO";

		public static final String PRIVADO = "PRIVADO";
	}

}
