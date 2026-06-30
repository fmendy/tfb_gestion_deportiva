package com.gestion.deportiva.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.FranjaHorariaDTO;
import com.gestion.deportiva.dto.FranjaHorariaDuracionDTO;
import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.InstalacionDisponibilidadDTO;
import com.gestion.deportiva.dto.InstalacionPublicoDTO;
import com.gestion.deportiva.dto.filter.InstalacionFilter;
import com.gestion.deportiva.dto.specifications.InstalacionSpecifications;
import com.gestion.deportiva.mapper.InstalacionMapper;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionConfiguracionReserva;
import com.gestion.deportiva.model.InstalacionHorario;
import com.gestion.deportiva.model.InstalacionHorarioBloqueado;
import com.gestion.deportiva.model.InstalacionHorarioEspecial;
import com.gestion.deportiva.model.Reserva;
import com.gestion.deportiva.repository.InstalacionConfiguracionReservaRepository;
import com.gestion.deportiva.repository.InstalacionHorarioBloqueadoRepository;
import com.gestion.deportiva.repository.InstalacionHorarioEspecialRepository;
import com.gestion.deportiva.repository.InstalacionHorarioRepository;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.repository.ReservaRepository;
import com.gestion.deportiva.service.InstalacionService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;
import com.gestion.deportiva.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class InstalacionServiceImpl implements InstalacionService {

	private static final Logger logger = LoggerFactory.getLogger(InstalacionServiceImpl.class);

	@Autowired
	private InstalacionRepository instalacionRepository;

	@Autowired
	private InstalacionHorarioRepository instalacionHorarioRepository;

	@Autowired
	private InstalacionHorarioEspecialRepository instalacionHorarioEspecialRepository;

	@Autowired
	private InstalacionConfiguracionReservaRepository instalacionConfiguracionReservaRepository;

	@Autowired
	private InstalacionHorarioBloqueadoRepository instalacionHorarioBloqueadoRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private InstalacionMapper instalacionMapper;

	@Autowired
	private ReservaRepository reservaRepository;

	@Override
	public InstalacionDTO findById(Long id) {
		logger.info("Buscando Instalacion por ID: {}", id);
		return instalacionMapper.modelToDTO(instalacionRepository.findByActivoTrueAndId(id));
	}

	@Override
	public InstalacionDTO findByUuid(String uuid) {
		logger.info("Buscando Instalacion por UUID: {}", uuid);
		return instalacionMapper.modelToDTO(instalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	private InstalacionFilter limitacionesPermisos(InstalacionFilter filter) {
		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)) {
			return filter;
		}

		if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_EMPRESA)) {
			filter.setListEmpresaIds(SecurityUtil.getCurrentUserListEmpresaId());
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_SEDE)) {
			filter.setListSedeIds(SecurityUtil.getCurrentUserListSedeId());
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Localizacion.GESTION_INSTALACION)) {
			filter.setListInstalacionIds(SecurityUtil.getCurrentUserListInstalacionId());
		}

		else {
			filter.setListInstalacionIds(List.of(-1L));
		}
		return filter;
	}

	@Override
	@Transactional
	public Long guardar(InstalacionDTO dto) {
		logger.info("Guardando Instalacion");
		Instalacion model = instalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo Instalacion");
			model = new Instalacion();
		}
		model = instalacionMapper.dtoToModel(dto, model);
		instalacionRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<InstalacionDTO> getPageByFilter(InstalacionFilter filter, Pageable pageable) {
		return instalacionMapper.pageToPageDTO(instalacionRepository
				.findAll(InstalacionSpecifications.filter(limitacionesPermisos(filter)), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando Instalacion por ID: {}");
		Instalacion model = instalacionRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		instalacionRepository.saveAndFlush(model);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando Instalacion por ID: {}");
		Instalacion model = instalacionRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		instalacionRepository.saveAndFlush(model);
	}

	@Override
	public InstalacionDTO findByNombreEqualsIgnoreCase(String nombre) {
		return instalacionMapper.modelToDTO(instalacionRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	public List<ComboDTO> getListComboDTO() {
		return instalacionMapper.listModelToListComboDTO(instalacionRepository.findByActivoTrue());
	}

	@Override
	public List<InstalacionDTO> getListDTO() {
		return Utils.sortByNombre(instalacionMapper.listModelToListDTO(instalacionRepository.findByActivoTrue()));
	}

	@Override
	public List<InstalacionDTO> getListDTO(InstalacionFilter filter) {
		return Utils.sortByNombre(instalacionMapper.listModelToListDTO(
				instalacionRepository.findAll(InstalacionSpecifications.filter(limitacionesPermisos(filter)))));
	}

	@Override
	public boolean canWrite(Long id) {
		return true;
	}

	@Override
	public boolean canRead(Long id) {
		return true;
	}

	@Override
	public byte[] exportarExcel(InstalacionFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InstalacionDTO> getListDTOParaEmpleado(Long empresaId, Long sedeId) {
		List<InstalacionDTO> retVal = new ArrayList<>();
		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)
				|| SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_GLOBAL)) {
			retVal = getListDTO();
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_EMPRESA)) {
			retVal = instalacionMapper.listModelToListDTO(instalacionRepository
					.findByActivoTrueAndSedeEmpresaIdIn(SecurityUtil.getCurrentUserListEmpresaId()));
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_SEDE)) {
			retVal = instalacionMapper.listModelToListDTO(instalacionRepository
					.findByActivoTrueAndSedeIdIn(SecurityUtil.getCurrentUserListSedeId()).stream().toList());
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_INSTALACION)) {
			retVal = instalacionMapper.listModelToListDTO(
					instalacionRepository.findByActivoTrueAndIdIn(SecurityUtil.getCurrentUserListInstalacionId()));
		}

		if (empresaId != null) {
			retVal.removeIf(s -> !s.getEmpresaId().equals(empresaId));
		}
		if (sedeId != null) {
			retVal.removeIf(s -> !s.getSedeId().equals(sedeId));
		}
		Utils.sortByCampo(retVal, InstalacionDTO::getEmpresaSedeInstalacionNombre);
		return Utils.addEmptyOption(retVal, InstalacionDTO.class);
	}

	@Override
	public InstalacionPublicoDTO getPublicoDTOById(Long id) {
		Instalacion instalacion = instalacionRepository.findByActivoTrueAndId(id);
		List<InstalacionHorario> listInstalacionHorarios = instalacionHorarioRepository
				.findByActivoTrueAndInstalacionId(id);
		List<InstalacionHorarioEspecial> listInstalacionHorarioEspecials = instalacionHorarioEspecialRepository
				.findByActivoTrueAndInstalacionId(id);
		return instalacionMapper.toPublicDTO(instalacion, listInstalacionHorarios, listInstalacionHorarioEspecials);
	}

	@Override
	public InstalacionDisponibilidadDTO getDisponibilidadDTOById(Long id, LocalDate fecha) {
		Instalacion instalacion = instalacionRepository.findByActivoTrueAndId(id);
		InstalacionConfiguracionReserva instalacionConfiguracionReserva = instalacionConfiguracionReservaRepository
				.findByActivoTrueAndInstalacionId(id);
		return instalacionMapper.toDisponibilidadDTO(instalacion, instalacionConfiguracionReserva,
				calcularDisponibilidad(id, fecha));
	}

	public List<FranjaHorariaDTO> calcularDisponibilidad(Long instalacionId, LocalDate fecha) {
	    InstalacionConfiguracionReserva config = instalacionConfiguracionReservaRepository
	            .findByActivoTrueAndInstalacionId(instalacionId);
	    long intervalo = config.getIntervaloHorario();

	    List<InstalacionHorarioEspecial> especiales = instalacionHorarioEspecialRepository
	            .findByInstalacionIdAndFechaAndActivoTrue(instalacionId, fecha);
	    List<InstalacionHorarioBloqueado> bloqueos = instalacionHorarioBloqueadoRepository
	            .findByActivoTrueAndInstalacionIdAndFecha(instalacionId, fecha);
	    List<Reserva> reservas = reservaRepository.findByInstalacionIdAndFechaAndReservaEstadoNombreIn(instalacionId,
	            fecha, List.of("PENDIENTE", "APROBADA", "COMPLETADA", "USUARIO NO COMPARECE"));

	    List<InstalacionHorario> horariosActivos = new ArrayList<>();
	    if (!especiales.isEmpty()) {
	        if (especiales.get(0).getCerrado()) return new ArrayList<>();
	        for (InstalacionHorarioEspecial e : especiales) {
	            InstalacionHorario ih = new InstalacionHorario();
	            ih.setHoraInicio(e.getHoraInicio());
	            ih.setHoraFin(e.getHoraFin());
	            horariosActivos.add(ih);
	        }
	    } else {
	        horariosActivos = buscarHorarioAplicable(instalacionId, fecha);
	    }

	    List<FranjaHorariaDTO> resultado = new ArrayList<>();

	    // --- NUEVA LÓGICA DE GENERACIÓN ---
	    for (InstalacionHorario h : horariosActivos) {
	        for (LocalTime inicio = h.getHoraInicio(); inicio.plusMinutes(config.getDuracionMin()).isBefore(h.getHoraFin()) 
	             || inicio.plusMinutes(config.getDuracionMin()).equals(h.getHoraFin()); inicio = inicio.plusMinutes(intervalo)) {
	            
	            List<FranjaHorariaDuracionDTO> opcionesValidas = new ArrayList<>();

	            for (long duracion = config.getDuracionMin(); duracion <= config.getDuracionMax(); duracion += intervalo) {
	                LocalTime fin = inicio.plusMinutes(duracion);
	                
	                // No permitir que la reserva exceda el horario de cierre
	                if (fin.isAfter(h.getHoraFin())) break;

	                if (estaLibre(inicio, fin, reservas, bloqueos)) {
	                    opcionesValidas.add(new FranjaHorariaDuracionDTO(duracion, fin));
	                }

	                if (config.getDuracionMin().equals(config.getDuracionMax())) break;
	            }

	            if (!opcionesValidas.isEmpty()) {
	                resultado.add(new FranjaHorariaDTO(inicio, opcionesValidas, "DISPONIBLE"));
	            }
	        }
	    }
	    return resultado;
	}

	// Función auxiliar para verificar disponibilidad del rango completo
	private boolean estaLibre(LocalTime inicio, LocalTime fin, List<Reserva> reservas, List<InstalacionHorarioBloqueado> bloqueos) {
	    boolean ocupadoPorReserva = reservas.stream()
	            .anyMatch(r -> r.getHoraInicio().isBefore(fin) && r.getHoraFin().isAfter(inicio));
	    boolean ocupadoPorBloqueo = bloqueos.stream()
	            .anyMatch(b -> b.getHoraInicio().isBefore(fin) && b.getHoraFin().isAfter(inicio));
	    return !ocupadoPorReserva && !ocupadoPorBloqueo;
	}

	private List<InstalacionHorario> buscarHorarioAplicable(Long instalacionId, LocalDate fecha) {
		return instalacionHorarioRepository.findByActivoTrueAndInstalacionIdAndDiaSemana(instalacionId,
				Utils.intToLong(fecha.getDayOfWeek().getValue()));
	}
}
