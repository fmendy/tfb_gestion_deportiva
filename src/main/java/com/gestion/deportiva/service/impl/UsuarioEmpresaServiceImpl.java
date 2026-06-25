package com.gestion.deportiva.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.UsuarioEmpresaDTO;
import com.gestion.deportiva.dto.filter.UsuarioEmpresaFilter;
import com.gestion.deportiva.dto.specifications.UsuarioEmpresaSpecifications;
import com.gestion.deportiva.mapper.UsuarioEmpresaMapper;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioEmpresa;
import com.gestion.deportiva.repository.UsuarioEmpresaRepository;
import com.gestion.deportiva.service.UsuarioEmpresaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class UsuarioEmpresaServiceImpl implements UsuarioEmpresaService {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioEmpresaServiceImpl.class);

	@Autowired
	private UsuarioEmpresaRepository usuarioEmpresaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UsuarioEmpresaMapper usuarioEmpresaMapper;

	@Override
	public UsuarioEmpresaDTO findById(Long id) {
		logger.info("Buscando UsuarioEmpresa por ID: {}", id);
		return usuarioEmpresaMapper.modelToDTO(usuarioEmpresaRepository.findByActivoTrueAndId(id));
	}

	@Override
	public UsuarioEmpresaDTO findByUuid(String uuid) {
		logger.info("Buscando UsuarioEmpresa por UUID: {}", uuid);
		return usuarioEmpresaMapper.modelToDTO(usuarioEmpresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(UsuarioEmpresaDTO dto) {
		logger.info("Guardando UsuarioEmpresa");
		UsuarioEmpresa model = usuarioEmpresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo UsuarioEmpresa");
			model = new UsuarioEmpresa();
		}
		model = usuarioEmpresaMapper.dtoToModel(dto, model);
		usuarioEmpresaRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public Page<UsuarioEmpresaDTO> getPageByFilter(UsuarioEmpresaFilter filter, Pageable pageable) {
		return usuarioEmpresaMapper
				.pageToPageDTO(usuarioEmpresaRepository.findAll(UsuarioEmpresaSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando UsuarioEmpresa por ID: {}");
		UsuarioEmpresa model = usuarioEmpresaRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		usuarioEmpresaRepository.saveAndFlush(model);
	}

	@Override
	@Transactional
	public void eliminarByUsuarioId(Long usuarioId) {
		logger.info("Desactivando UsuarioEmpresa para el usuario ID: {}", usuarioId);
		List<UsuarioEmpresa> list = usuarioEmpresaRepository.findByActivoTrueAndUsuarioId(usuarioId);
		if (!list.isEmpty()) {
			list.forEach(ue -> ue.setActivo(false));
			usuarioEmpresaRepository.saveAll(list);
		}
	}

	@Override
	public void eliminar(String uuid) {
		logger.info("Eliminando UsuarioEmpresa por ID: {}");
		UsuarioEmpresa model = usuarioEmpresaRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		model.setActivo(false);
		usuarioEmpresaRepository.saveAndFlush(model);
	}

	@Override
	public List<UsuarioEmpresaDTO> getListDTO() {
		return usuarioEmpresaMapper.listModelToListDTO(usuarioEmpresaRepository.findByActivoTrue());
	}

	@Override
	public List<UsuarioEmpresaDTO> getListDTO(UsuarioEmpresaFilter filter) {
		return usuarioEmpresaMapper
				.listModelToListDTO(usuarioEmpresaRepository.findAll(UsuarioEmpresaSpecifications.filter(filter)));
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
	public byte[] exportarExcel(UsuarioEmpresaFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long asociarUsuarioEmpresa(Long usuarioId, Long empresaId) {
		UsuarioEmpresa model = new UsuarioEmpresa();
		model.setUsuario(new Usuario(usuarioId));
		model.setEmpresa(new Empresa(empresaId));
		logger.info("Asociando Usuario " + usuarioId + " con Empresa " + empresaId);
		usuarioEmpresaRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public List<UsuarioEmpresa> getListByUsuarioId(Long usuarioId) {
		return usuarioEmpresaRepository.findByActivoTrueAndUsuarioId(usuarioId);
	}
}
