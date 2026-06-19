package com.gestion.deportiva.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.RolDTO;
import com.gestion.deportiva.dto.filter.RolFilter;
import com.gestion.deportiva.dto.specifications.RolSpecifications;
import com.gestion.deportiva.mapper.RolMapper;
import com.gestion.deportiva.model.Rol;
import com.gestion.deportiva.repository.RolRepository;
import com.gestion.deportiva.service.RolService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;
import com.gestion.deportiva.util.Utils;

@Service
public class RolServiceImpl implements RolService {

	private static final Logger logger = LoggerFactory.getLogger(RolServiceImpl.class);

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private RolMapper rolMapper;

	@Override
	public RolDTO findById(Long id) {
		logger.info("Buscando rol por ID: {}", id);
		return rolMapper.modelToDTO(rolRepository.findByActivoTrueAndId(id));
	}

	@Override
	public RolDTO findByUuid(String uuid) {
		logger.info("Buscando rol por UUID: {}", uuid);
		return rolMapper.modelToDTO(rolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	public Long guardar(RolDTO dto) {
		logger.info("Guardando rol");
		Rol model = rolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo rol");
			model = new Rol();
		}
		model = rolMapper.dtoToModel(dto, model);
		rolRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<RolDTO> getPageByFilter(RolFilter filter, Pageable pageable) {
		return rolMapper.pageToPageDTO(rolRepository.findAll(RolSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando rol por ID: {}");
		Rol rol = rolRepository.findByActivoTrueAndId(id);
		rol.setActivo(false);
		rolRepository.saveAndFlush(rol);
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando rol por ID: {}");
		Rol rol = rolRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		rol.setActivo(false);
		rolRepository.saveAndFlush(rol);
	}

	@Override
	public RolDTO findByNombreEqualsIgnoreCase(String nombre) {
		return rolMapper.modelToDTO(rolRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre));
	}

	@Override
	public List<ComboDTO> getListComboDTO() {
		List<Rol> list = rolRepository.findByActivoTrueOrderByNombreAsc();
		return rolMapper.listModelToListComboDTO(list);
	}

	@Override
	public List<RolDTO> getListDTO() {
		return Utils.sortByNombre(rolMapper.listModelToListDTO(rolRepository.findByActivoTrue()));
	}

	@Override
	public List<RolDTO> getListDTO(RolFilter filter) {
		return Utils
				.sortByNombre(rolMapper.listModelToListDTO(rolRepository.findAll(RolSpecifications.filter(filter))));
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
	public byte[] exportarExcel(RolFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RolFilter getFilterParaUsuarioController() {
		RolFilter filter = new RolFilter();
		/*
		 * if(SecurityUtil.hasAuthority(Constantes.ROLE+"_"+Constantes.Rol.ADMINISTRADOR
		 * )) { return filter; }else
		 * if(SecurityUtil.hasAuthority(Constantes.ROLE+"_"+Constantes.Rol.GESTOR)) {
		 * filter.setListNombre(Arrays.asList(Constantes.Rol.GESTOR,
		 * Constantes.Rol.CONDUCTOR)); }else { filter.setListNombre(Arrays.asList(
		 * Constantes.Rol.CONDUCTOR)); }
		 */
		return filter;
	}

	@Override
	public List<RolDTO> getListDTOParaEmpleado() {
		List<Rol> listRoles = new ArrayList<>();
		if (SecurityUtil.hasAuthority(Constantes.Role.ADMINISTRADOR)) {
			listRoles = rolRepository.findByActivoTrue();
		} else if (SecurityUtil.hasAuthority(Constantes.Role.USUARIO_EMPRESA)) {
			listRoles = rolRepository.findByActivoTrueAndNombreInIgnoreCase(Arrays.asList(
					Constantes.Rol.USUARIO_EMPRESA, Constantes.Rol.USUARIO_SEDE, Constantes.Rol.USUARIO_INSTALACION));
		} else if (SecurityUtil.hasAuthority(Constantes.Role.USUARIO_SEDE)) {
			listRoles = rolRepository.findByActivoTrueAndNombreInIgnoreCase(
					Arrays.asList(Constantes.Rol.USUARIO_SEDE, Constantes.Rol.USUARIO_INSTALACION));
		} else if (SecurityUtil.hasAuthority(Constantes.Role.USUARIO_INSTALACION)) {
			listRoles = rolRepository
					.findByActivoTrueAndNombreInIgnoreCase(Arrays.asList(Constantes.Rol.USUARIO_INSTALACION));
		}
		return Utils.addEmptyOptionIfMoreThanOneOption(rolMapper.listModelToListDTO(listRoles), RolDTO.class);
	}

}
