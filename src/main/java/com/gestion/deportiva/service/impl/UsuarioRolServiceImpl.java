package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.UsuarioRolDTO;
import com.gestion.deportiva.dto.filter.UsuarioRolFilter;
import com.gestion.deportiva.dto.specifications.UsuarioRolSpecifications;
import com.gestion.deportiva.mapper.UsuarioRolMapper;
import com.gestion.deportiva.model.Rol;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioRol;
import com.gestion.deportiva.repository.RolRepository;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.repository.UsuarioRolRepository;
import com.gestion.deportiva.service.UsuarioRolService;

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

	@Autowired
	private UsuarioRolMapper usuarioRolMapper;

	@Override
	public UsuarioRolDTO findById(Long id) {
		logger.info("Buscando UsuarioRol con ID: {}", id);
		return usuarioRolMapper.modelToDTO(usuarioRolRepository.findByActivoTrueAndId(id));
	}

	@Override
	public UsuarioRolDTO findByUuid(String uuid) {
		logger.info("Buscando UsuarioRol con UUID: {}", uuid);
		return usuarioRolMapper.modelToDTO(usuarioRolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	public Long guardar(UsuarioRolDTO dto) {
		logger.info("Guardando UsuarioRol");
		UsuarioRol model = usuarioRolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			model = new UsuarioRol();
		}
		dto.setRolId(rolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getRolUuid()).getId());
		dto.setUsuarioId(usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUsuarioUuid()).getId());
		model = usuarioRolMapper.dtoToModel(dto, model);
		return dto.getId();
	}

	@Override
	public Page<UsuarioRolDTO> getPageByFilter(UsuarioRolFilter filter, Pageable pageable) {
		return usuarioRolMapper
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
	public List<UsuarioRolDTO> getListDTO() {
		return usuarioRolMapper.listModelToListDTO(usuarioRolRepository.findByActivoTrue());
	}

	@Override
	public List<UsuarioRolDTO> getListDTO(UsuarioRolFilter filter) {
		return usuarioRolMapper
				.listModelToListDTO(usuarioRolRepository.findAll(UsuarioRolSpecifications.filter(filter)));
	}

	@Override
	public boolean canWrite(Long id) {
		return false;
	}

	@Override
	public boolean canRead(Long id) {
		return true;
	}

	@Override
	public byte[] exportarExcel(UsuarioRolFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void asignarRol(Long usuarioId, String rolNombre) {
		eliminarRolesByUsuarioId(usuarioId);
		Rol rol = rolRepository.findByActivoTrueAndNombreEqualsIgnoreCase(rolNombre);
		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setUsuario(new Usuario(usuarioId));
		usuarioRol.setRol(rol);
		usuarioRolRepository.saveAndFlush(usuarioRol);
	}

	@Override
	public void eliminarRolesByUsuarioId(Long usuarioId) {
		List<UsuarioRol> listUsuarioRol = usuarioRolRepository.findByActivoTrueAndUsuarioId(usuarioId);
		for (UsuarioRol usuarioRol : listUsuarioRol) {
			if (usuarioRol.isActivo()) {
				usuarioRol.setActivo(false);
				usuarioRolRepository.save(usuarioRol);
			}
		}
	}

	@Override
	@Transactional
	public Long asignarRol(Long usuarioId, Long rolId) {
		eliminarRolesByUsuarioId(usuarioId);
		UsuarioRol usuarioRol = new UsuarioRol(new Usuario(usuarioId), new Rol(rolId));
		usuarioRolRepository.saveAndFlush(usuarioRol);
		return usuarioRol.getId();

	}

	@Override
	public UsuarioRol getUsuarioRolByUsuarioId(Long usuarioId) {
		return usuarioRolRepository.findByActivoTrueAndUsuarioId(usuarioId).getFirst();
	}

}
