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
import com.gestion.deportiva.dto.filter.EmpleadoFilter;
import com.gestion.deportiva.dto.specifications.EmpleadoSpecifications;
import com.gestion.deportiva.mapper.EmpleadoMapper;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.service.EmpleadoService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

	private static final Logger logger = LoggerFactory.getLogger(EmpleadoServiceImpl.class);

	@Autowired
	private UsuarioRepository usuarioRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private EmpleadoMapper empleadoMapper;

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
		if (SecurityUtil.hasAuthority(Constantes.Permiso.GESTION_GLOBAL)
				|| SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_GLOBAL)) {
			return filter;
		}
		if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_EMPRESA)) {
			filter.setListEmpresaIds(SecurityUtil.getCurrentUserListEmpresaId());
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_SEDE)) {
			filter.setListSedeIds(SecurityUtil.getCurrentUserListSedeId());
		} else if (SecurityUtil.hasAuthority(Constantes.Permiso.Usuario.GESTION_USUARIO_INSTALACION)) {
			filter.setListInstalacionIds(SecurityUtil.getCurrentUserListInstalacionId());
		} else {
			filter.setInstalacionId(-1L);
		}
		return filter;
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
		return false;
	}

	@Override
	public boolean canRead(Long id) {
		return false;
	}

	@Override
	public byte[] exportarExcel(EmpleadoFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
