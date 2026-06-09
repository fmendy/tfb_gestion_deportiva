package com.gestion.deportiva.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.UsuarioRolesDTO;
import com.gestion.deportiva.dto.UsuarioRolDTO;
import com.gestion.deportiva.dto.filter.UsuarioRolFilter;
import com.gestion.deportiva.dto.specifications.UsuarioRolSpecifications;
import com.gestion.deportiva.model.Rol;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioRol;
import com.gestion.deportiva.repository.RolRepository;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.repository.UsuarioRolRepository;
import com.gestion.deportiva.service.UsuarioRolService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.UsuarioRolUtil;
import jakarta.transaction.Transactional;

@Service
public class UsuarioRolServiceImpl implements UsuarioRolService {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioRolServiceImpl.class);

	@Autowired
	private UsuarioRolRepository usuarioRolRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private RolRepository rolRepository;

	@Override
	public UsuarioRolDTO findById(Long id) {
		logger.info("Buscando UsuarioRol con ID: {}", id);
		return UsuarioRolUtil.modelToDTO(usuarioRolRepository.findByActivoTrueAndId(id));
	}

	@Override
	public UsuarioRolDTO findByUuid(String uuid) {
		logger.info("Buscando UsuarioRol con UUID: {}", uuid);
		return UsuarioRolUtil.modelToDTO(usuarioRolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	public String guardar(UsuarioRolDTO dto) {
		logger.info("Guardando UsuarioRol");
		UsuarioRol model = usuarioRolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			model = new UsuarioRol();
		}
		dto.setRolId(rolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getRolUuid()).getId());
		dto.setUsuarioId(usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUsuarioUuid()).getId());
		model = UsuarioRolUtil.dtoToModel(dto, model);
		return dto.getUuid();
	}

	@Override
	public Page<UsuarioRolDTO> getPageByFilter(UsuarioRolFilter filter, Pageable pageable) {
		return UsuarioRolUtil
				.pageToPageDTO(usuarioRolRepository.findAll(UsuarioRolSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando UsuarioRol con ID: {}", id);
		UsuarioRol usuarioRol = usuarioRolRepository.findByActivoTrueAndId(id);
		if (usuarioRol != null) {
			usuarioRol.setActivo(false);
			usuarioRolRepository.saveAndFlush(usuarioRol);
		}
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando UsuarioRol con UUID: {}", uuid);
		UsuarioRol usuarioRol = usuarioRolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		if (usuarioRol != null) {
			usuarioRol.setActivo(false);
			usuarioRolRepository.saveAndFlush(usuarioRol);
		}
	}

	@Override
	public void asignarRolPorDefecto(Usuario usuario) {
		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setUsuario(usuario);
		usuarioRol.setRol(rolRepository.findByActivoTrueAndNombreEqualsIgnoreCase(Constantes.Rol.CONDUCTOR));
		usuarioRolRepository.saveAndFlush(usuarioRol);
	}


	@Override
	public List<UsuarioRolDTO> getListDTO() {
		return UsuarioRolUtil.listModelToListDTO(usuarioRolRepository.findByActivoTrue());
	}

	@Override
	public List<UsuarioRolDTO> getListDTO(UsuarioRolFilter filter) {
		return UsuarioRolUtil.listModelToListDTO(usuarioRolRepository.findAll(UsuarioRolSpecifications.filter(filter)));
	}


	@Override
	public boolean canWrite(String uuid) {
		return false;
	}

	@Override
	public boolean canRead(String uuid) {
		return true;
	}

	@Override
	public byte[] exportarExcel(UsuarioRolFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UsuarioRolesDTO findUsuarioRolesDTOByUsuarioUuid(String usuarioUuid) {
		logger.info("Construyendo usuarioRol con UUID: {}", usuarioUuid);
		Usuario usuario = usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(usuarioUuid);
		List<UsuarioRol> listUsuarioRol= usuarioRolRepository
				.findByActivoTrueAndUsuario_UuidEqualsIgnoreCase(usuarioUuid);
		return UsuarioRolUtil.buildUsuarioRolesDTO(usuario, listUsuarioRol);
	}
	

	@Override
	@Transactional
	public void guardar(UsuarioRolesDTO dto) {
		logger.info("Guardando demarcacion para usuario UUID: {}", dto.getUsuarioUuid());

		Usuario usuario = usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUsuarioUuid());
		if (usuario == null) {
			throw new IllegalArgumentException("Usuario no encontrado o inactivo");
		}

		// Lista de seleccionados desde el formulario
		Set<String> seleccionados = new HashSet<>(dto.getListActualRolUuid());

		// Equipamientos actuales (activos e inactivos)
		List<UsuarioRol> actuales = usuarioRolRepository
				.findByActivoTrueAndUsuario_UuidEqualsIgnoreCase(dto.getUsuarioUuid());

		for (UsuarioRol existente : actuales) {
			String rolUuid = existente.getRol().getUuid();
			boolean marcado = seleccionados.contains(rolUuid);

			if (marcado && !existente.isActivo()) {
				existente.setActivo(true);
				usuarioRolRepository.save(existente);
			} else if (!marcado && existente.isActivo()) {
				existente.setActivo(false);
				usuarioRolRepository.save(existente);
			}

			// Eliminamos del set los ya procesados
			seleccionados.remove(rolUuid);
		}

		// Crea nuevos si quedaron UUIDs sin procesar
		for (String nuevoUuid : seleccionados) {
			Rol rol = rolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(nuevoUuid);
			if (rol != null) {
				UsuarioRol nuevo = new UsuarioRol();
				nuevo.setUsuario(usuario);
				nuevo.setRol(rol);
				usuarioRolRepository.save(nuevo);
			}
		}
	}


}
