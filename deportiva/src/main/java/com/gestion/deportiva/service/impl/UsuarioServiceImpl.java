package com.gestion.deportiva.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion.deportiva.config.AuditorAwareContext;
import com.gestion.deportiva.dto.CustomUserDetails;
import com.gestion.deportiva.dto.MiPerfilDTO;
import com.gestion.deportiva.dto.MiPerfilPasswordDTO;
import com.gestion.deportiva.dto.UsuarioDTO;
import com.gestion.deportiva.dto.UsuarioRegistroDTO;
import com.gestion.deportiva.dto.filter.UsuarioFilter;
import com.gestion.deportiva.dto.specifications.UsuarioSpecifications;
import com.gestion.deportiva.model.Permiso;
import com.gestion.deportiva.model.Rol;
import com.gestion.deportiva.model.RolPermiso;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioRol;

import com.gestion.deportiva.repository.UsuarioRepository;

import com.gestion.deportiva.service.UsuarioService;
import com.gestion.deportiva.util.SecurityUtil;
import com.gestion.deportiva.util.UsuarioUtil;
import com.gestion.deportiva.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<UsuarioDTO> getPageByFilter(UsuarioFilter filter, Pageable pageable) {
		return UsuarioUtil.pageToPageDTO(
				usuarioRepository.findAll(UsuarioSpecifications.filter(limitacionesPermisos(filter)), pageable));
	}

	private UsuarioFilter limitacionesPermisos(UsuarioFilter filter) {

		return filter;
	}

	@Override
	@Transactional
	public String guardar(UsuarioDTO form) {
		Usuario model = usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(form.getUuid());
		model = UsuarioUtil.dtoToModel(form, model);
		usuarioRepository.saveAndFlush(model);
		return model.getUuid();
	}

	@Override
	public UsuarioDTO findById(Long id) {
		return UsuarioUtil.modelToDTO(usuarioRepository.findByActivoTrueAndId(id));
	}

	@Override
	public Usuario getByUsername(String username) {
		return usuarioRepository.findByActivoTrueAndNombreEqualsIgnoreCase(username);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByActivoTrueAndEmailIgnoreCase(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		Set<GrantedAuthority> authorities = new HashSet<>();

		// Roles del usuario
		List<UsuarioRol> listUsuarioRol = usuario.getListUsuarioRol().stream().filter(ur -> ur.isActivo()).toList();

		for (UsuarioRol usuarioRol : listUsuarioRol) {
			Rol rol = usuarioRol.getRol();
			if (rol.isActivo()) {
				// Rol como ROLE_X
				List<RolPermiso> listRolPermiso = rol.getListRolPermiso().stream().filter(rp -> rp.isActivo()).toList();
				for (RolPermiso rp : listRolPermiso) {
					Permiso permiso = rp.getPermiso();
					if (permiso.isActivo()) {
						authorities.add(new SimpleGrantedAuthority(permiso.getNombre()));
					}
				}

				authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));
			}

		}
		return new CustomUserDetails(usuario.getId(), usuario.getUuid(), usuario.getNombre(), usuario.getPassword(),
				authorities);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario getUsuarioWithoutAuditor(String nombre) {
		AuditorAwareContext.disableAuditing();
		// Accedemos al usuario sin activar auditoría
		Usuario usuario = usuarioRepository.findByActivoTrueAndNombreEqualsIgnoreCase(nombre);
		// Habilitamos la auditoría nuevamente
		AuditorAwareContext.enableAuditing();
		return usuario;
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		Usuario usuario = usuarioRepository.findByActivoTrueAndId(id);
		usuario.setActivo(false);
		usuarioRepository.saveAndFlush(usuario);
	}

	@Override
	public Long guardarDatos(UsuarioDTO form) {
		Usuario model = usuarioRepository.findByActivoTrueAndId(form.getId());
		model = UsuarioUtil.dtoToModel(form, model);
		usuarioRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public String getNombreById(Long id) {
		Usuario usuario = usuarioRepository.findByActivoTrueAndId(id);
		if (usuario != null) {
			return usuario.getNombre();
		}
		return null;
	}

	@Override
	public String registrar(UsuarioRegistroDTO dto) {
		Usuario usuario = UsuarioUtil.registroDTOToModel(dto);
		usuarioRepository.saveAndFlush(usuario);

		return usuario.getUuid();
	}

	@Override
	public UsuarioDTO findByUuid(String uuid) {
		return UsuarioUtil.modelToDTO(usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	public void eliminar(String uuid) {
		Usuario usuario = usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		usuario.setActivo(false);
		usuarioRepository.saveAndFlush(usuario);

	}

	@Override
	public MiPerfilDTO getMiPerfilDTO() {
		Usuario usuario = usuarioRepository.findByActivoTrueAndId(SecurityUtil.getCurrentUserId());
		return UsuarioUtil.modelToMiPerfilDTO(usuario);
	}

	@Override
	public MiPerfilPasswordDTO getMiPerfilPasswordDTO() {
		Usuario usuario = usuarioRepository.findByActivoTrueAndId(SecurityUtil.getCurrentUserId());
		return UsuarioUtil.modelToMiPerfilPasswordDTO(usuario);
	}

	@Override
	public void actualizarPassword(MiPerfilPasswordDTO dto) {
		Usuario usuario = usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(dto.getUuid());
		dto.setPasswordNuevo(passwordEncoder.encode(dto.getPasswordNuevo()));
		usuarioRepository.saveAndFlush(usuario);
	}

	@Override
	public List<UsuarioDTO> getListDTO() {
		return Utils.sortByNombre(UsuarioUtil.listModelToListDTO(usuarioRepository.findByActivoTrue()));
	}

	@Override
	public List<UsuarioDTO> getListDTO(UsuarioFilter filter) {

		return Utils.sortByNombre(UsuarioUtil.listModelToListDTO(
				usuarioRepository.findAll(UsuarioSpecifications.filter(limitacionesPermisos(filter)))));
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
	public byte[] exportarExcel(UsuarioFilter filter) throws IOException {
		return null;
	}

}
