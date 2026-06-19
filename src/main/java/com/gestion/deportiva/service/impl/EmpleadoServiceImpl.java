package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.EmpleadoDTO;
import com.gestion.deportiva.dto.EmpleadoRegistroDTO;
import com.gestion.deportiva.dto.filter.EmpleadoFilter;
import com.gestion.deportiva.dto.specifications.EmpleadoSpecifications;
import com.gestion.deportiva.mapper.EmpleadoMapper;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.InstalacionRepository;
import com.gestion.deportiva.repository.SedeRepository;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.service.EmpleadoService;
import com.gestion.deportiva.service.UsuarioEmpresaService;
import com.gestion.deportiva.service.UsuarioInstalacionService;
import com.gestion.deportiva.service.UsuarioRolService;
import com.gestion.deportiva.service.UsuarioSedeService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

	private static final Logger logger = LoggerFactory.getLogger(EmpleadoServiceImpl.class);

	@Autowired
	private UsuarioRepository usuarioRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private EmpleadoMapper empleadoMapper;

	@Autowired
	private SedeRepository sedeRepository;

	@Autowired
	private InstalacionRepository instalacionRepository;
	
	@Autowired
	private UsuarioRolService usuarioRolService;
	
	@Autowired
	private UsuarioEmpresaService usuarioEmpresaService;
	
	@Autowired
	private UsuarioSedeService usuarioSedeService;
	
	@Autowired
	private UsuarioInstalacionService usuarioInstalacionService;

	@Override
	public EmpleadoDTO findById(Long id) {
		logger.info("Buscando Empleado por ID: {}", id);
		Usuario usuario = usuarioRepository.findByActivoTrueAndId(id);
		return empleadoMapper.modelToDTO(usuario);
	}

	@Override
	public EmpleadoDTO findByUuid(String uuid) {
		logger.info("Buscando Empleado por UUID: {}", uuid);
		Usuario usuario = usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		return empleadoMapper.modelToDTO(usuario);
	}

	@Override
	@Transactional
	public Long guardar(EmpleadoDTO dto) {
		return null;
	}

	@Override
	public Page<EmpleadoDTO> getPageByFilter(EmpleadoFilter filter, Pageable pageable) {
		return empleadoMapper.pageToPageDTO(
				usuarioRepository.findAll(EmpleadoSpecifications.filter(limitacionesPermisos(filter)), pageable));
	}

	private EmpleadoFilter limitacionesPermisos(EmpleadoFilter filter) {
		if (SecurityUtil.hasGlobalAccess())
			return filter;

		if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_EMPRESA)) {
			applyEmpresaAccess(filter);
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_SEDE)) {
			applySedeAccess(filter);
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_INSTALACION)) {
			applyInstalacionAccess(filter);
		} else {
			filter.setInstalacionId(-1L);
		}
		return filter;
	}

	private void applyInstalacionAccess(EmpleadoFilter filter) {
		List<Long> listIds = SecurityUtil.getCurrentUserListInstalacionId();
		filter.setListInstalacionIds(listIds);

	}

	private void applySedeAccess(EmpleadoFilter filter) {
		List<Long> listIds = SecurityUtil.getCurrentUserListSedeId();
		filter.setListSedeIds(listIds);
		filter.setListInstalacionIds(instalacionRepository.findListIdsByListSedesIdsIn(listIds));

	}

	private void applyEmpresaAccess(EmpleadoFilter filter) {
		List<Long> listIds = SecurityUtil.getCurrentUserListEmpresaId();
		filter.setListEmpresaIds(listIds);
		filter.setListSedeIds(sedeRepository.findListIdsByListEmpresasIdsIn(listIds));
		filter.setListInstalacionIds(instalacionRepository.findListIdsByListEmpresasIdsIn(listIds));
	}

	@Override
	public void eliminar(Long id) {
	}

	@Override
	public void eliminar(String uuid) {
	}

	@Override
	public EmpleadoDTO findByNombreEqualsIgnoreCase(String nombre) {
		return empleadoMapper.modelToDTO(usuarioRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	public List<ComboDTO> getListComboDTO() {
		return null;
	}

	@Override
	public List<EmpleadoDTO> getListDTO() {
		return null;
	}

	@Override
	public List<EmpleadoDTO> getListDTO(EmpleadoFilter filter) {
		return null;
	}

	@Override
	public boolean canWrite(Long id) {
		if (SecurityUtil.hasGlobalAccess()
				|| SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_GLOBAL)) {
			return true;
		}

		// Si es creación (id == null) y tiene al menos un permiso de gestión
		if (id == null) {
			return SecurityUtil.hasAnyAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_EMPRESA,
					Constantes.Permiso.Usuario.GESTION_USUARIO_SEDE,
					Constantes.Permiso.Usuario.GESTION_USUARIO_INSTALACION);
		}

		Usuario usuario = usuarioRepository.findByActivoTrueAndId(id);
		if (usuario == null)
			return false;

		// Delegamos la validación según el rol
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_EMPRESA)) {
			return canAccessByEmpresa(usuario);
		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_SEDE)) {
			return canAccessBySede(usuario);
		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_INSTALACION)) {
			return canAccessByInstalacion(usuario);
		}

		return false;
	}

	private boolean canAccessByEmpresa(Usuario usuario) {
		List<Long> allowedEmpresas = SecurityUtil.getCurrentUserListEmpresaId();

		boolean match = usuario.getListUsuarioEmpresa().stream()
				.anyMatch(ue -> allowedEmpresas.contains(ue.getEmpresa().getId()));
		if (match)
			return true;

		match = usuario.getListUsuarioSede().stream()
				.anyMatch(us -> allowedEmpresas.contains(us.getSede().getEmpresa().getId()));
		if (match)
			return true;

		return usuario.getListUsuarioInstalacion().stream()
				.anyMatch(ui -> allowedEmpresas.contains(ui.getInstalacion().getSede().getEmpresa().getId()));
	}

	private boolean canAccessBySede(Usuario usuario) {
		List<Long> allowedSedes = SecurityUtil.getCurrentUserListSedeId();

		boolean match = usuario.getListUsuarioSede().stream()
				.anyMatch(us -> allowedSedes.contains(us.getSede().getId()));
		if (match)
			return true;

		return usuario.getListUsuarioInstalacion().stream()
				.anyMatch(ui -> allowedSedes.contains(ui.getInstalacion().getSede().getId()));
	}

	private boolean canAccessByInstalacion(Usuario usuario) {
		List<Long> allowedInstalaciones = SecurityUtil.getCurrentUserListInstalacionId();
		return usuario.getListUsuarioInstalacion().stream()
				.anyMatch(ui -> allowedInstalaciones.contains(ui.getInstalacion().getId()));
	}

	@Override
	public boolean canRead(Long id) {
		return canWrite(id);
	}

	@Override
	public byte[] exportarExcel(EmpleadoFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Long guardar(@Valid EmpleadoRegistroDTO dto) {
		Usuario usuario = empleadoMapper.empleadoRegistroDTOToUsuario(dto);
		usuarioRepository.save(usuario);
		usuarioRolService.asignarRol(usuario.getId(), dto.getRolId());
		if(dto.getEmpresaId() != null) {
			usuarioEmpresaService.asociarUsuarioEmpresa(usuario.getId(), dto.getEmpresaId());
		}
		if(dto.getSedeId() != null) {
			usuarioSedeService.asociarUsuarioSede(usuario.getId(), dto.getSedeId());
		}
		if(dto.getInstalacionId() != null) {
			usuarioInstalacionService.asociarUsuarioInstalacion(usuario.getId(), dto.getInstalacionId());
		}
		return usuario.getId();
	}

	@Override
	public EmpleadoRegistroDTO findEmpleadoRegistroById(Long id) {
		logger.info("Buscando Empleado por ID: {}", id);
		Usuario usuario = usuarioRepository.findByActivoTrueAndId(id);
		return empleadoMapper.modelToEmpleadoRegistroDTO(usuario);
	}

}
