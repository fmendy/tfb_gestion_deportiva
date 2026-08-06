package com.gestion.deportiva.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gestion.deportiva.dto.UsuarioTokenDTO;
import com.gestion.deportiva.dto.filter.UsuarioTokenFilter;
import com.gestion.deportiva.dto.specifications.UsuarioTokenSpecifications;
import com.gestion.deportiva.mapper.UsuarioTokenMapper;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioToken;
import com.gestion.deportiva.repository.UsuarioTokenRepository;
import com.gestion.deportiva.service.UsuarioTokenService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class UsuarioTokenServiceImpl extends BaseServiceImpl<UsuarioTokenDTO, UsuarioTokenFilter>
		implements UsuarioTokenService {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioTokenServiceImpl.class);

	private final UsuarioTokenRepository usuarioTokenRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private final UsuarioTokenMapper usuarioTokenMapper;

	UsuarioTokenServiceImpl(UsuarioTokenRepository usuarioTokenRepository, UsuarioTokenMapper usuarioTokenMapper) {
		this.usuarioTokenRepository = usuarioTokenRepository;
		this.usuarioTokenMapper = usuarioTokenMapper;
	}

	@Override
	public UsuarioTokenDTO findById(Long id) {
		logger.info("Buscando UsuarioToken por ID: {}", id);
		return usuarioTokenMapper.modelToDTO(usuarioTokenRepository.findByActivoTrueAndId(id));
	}

	@Override
	public UsuarioTokenDTO findByUuid(String uuid) {
		logger.info("Buscando UsuarioToken por UUID: {}", uuid);
		return usuarioTokenMapper.modelToDTO(usuarioTokenRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	@Transactional
	public Long guardar(UsuarioTokenDTO dto) {
		logger.info("Guardando UsuarioToken");
		UsuarioToken model = usuarioTokenRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		if (model == null) {
			logger.info("Creando nuevo UsuarioToken");
			model = new UsuarioToken();
		}
		model = usuarioTokenMapper.dtoToModel(dto, model);
		usuarioTokenRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	@Transactional
	public String crearToken(Long usuarioId) {
		desactivarTokensByUsuarioId(usuarioId);
		logger.info("Creando UsuarioToken");
		Usuario usuario = new Usuario(usuarioId);
		UsuarioToken model = new UsuarioToken();
		model.setUsuario(usuario);
		usuarioTokenRepository.saveAndFlush(model);
		return model.getUuid();
	}

	@Override
	@Transactional
	public void desactivarTokensByUsuarioId(Long usuarioId) {
		logger.info("Desactivando UsuarioToken");
		List<UsuarioToken> list = usuarioTokenRepository.findByActivoTrueAndUsuarioId(usuarioId);
		desactivar(list);

	}

	@Override
	@Transactional
	public UsuarioToken getTokenActivoByUuid(String uuid) {
		UsuarioToken token = usuarioTokenRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		if (isValidToken(token, 5L)) {
			return token;
		}

		return null;
	}

	@Override
	@Transactional
	public Boolean isValidToken(UsuarioToken usuarioToken, Long minValid) {

		return usuarioToken != null && usuarioToken.getFechaCreacion().plusMinutes(minValid)
				.isAfter(LocalDateTime.now(ZoneId.of("Europe/Madrid")));
	}

	@Override
	public Page<UsuarioTokenDTO> getPageByFilter(UsuarioTokenFilter filter, Pageable pageable) {
		return usuarioTokenMapper
				.pageToPageDTO(usuarioTokenRepository.findAll(UsuarioTokenSpecifications.filter(filter), pageable));
	}

	@Override
	public void eliminar(Long id) {
		logger.info("Eliminando UsuarioToken por ID: {}");
		UsuarioToken model = usuarioTokenRepository.findByActivoTrueAndId(id);
		model.setActivo(false);
		usuarioTokenRepository.saveAndFlush(model);
	}

	@Override
	public List<UsuarioTokenDTO> getListDTO() {
		return usuarioTokenMapper.listModelToListDTO(usuarioTokenRepository.findByActivoTrue());
	}

	@Override
	public List<UsuarioTokenDTO> getListDTO(UsuarioTokenFilter filter) {
		return usuarioTokenMapper
				.listModelToListDTO(usuarioTokenRepository.findAll(UsuarioTokenSpecifications.filter(filter)));
	}

	@Override
	public boolean canWrite(Long id) {
		return true;
	}

	@Override
	public boolean canRead(Long id) {
		return true;
	}

}
