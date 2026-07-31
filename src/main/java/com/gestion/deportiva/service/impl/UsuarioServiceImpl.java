package com.gestion.deportiva.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion.deportiva.config.AuditorAwareContext;
import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.CustomUserDetails;
import com.gestion.deportiva.dto.MiPerfilDTO;
import com.gestion.deportiva.dto.MiPerfilPasswordDTO;
import com.gestion.deportiva.dto.EmpresaRegistroDTO;
import com.gestion.deportiva.dto.UsuarioDTO;
import com.gestion.deportiva.dto.UsuarioPasswordDTO;
import com.gestion.deportiva.dto.UsuarioRegistroDTO;
import com.gestion.deportiva.dto.filter.UsuarioFilter;
import com.gestion.deportiva.dto.specifications.UsuarioSpecifications;
import com.gestion.deportiva.mapper.UsuarioMapper;
import com.gestion.deportiva.model.Permiso;
import com.gestion.deportiva.model.Rol;
import com.gestion.deportiva.model.RolPermiso;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioRol;
import com.gestion.deportiva.model.UsuarioToken;
import com.gestion.deportiva.repository.RolRepository;
import com.gestion.deportiva.repository.UsuarioEmpresaRepository;
import com.gestion.deportiva.repository.UsuarioInstalacionRepository;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.repository.UsuarioRolRepository;
import com.gestion.deportiva.repository.UsuarioSedeRepository;
import com.gestion.deportiva.service.MailService;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.service.UsuarioService;
import com.gestion.deportiva.service.UsuarioTokenService;
import com.gestion.deportiva.util.Constantes;
import com.gestion.deportiva.util.SecurityUtil;
import com.gestion.deportiva.util.Utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;

@Service
public class UsuarioServiceImpl extends MaestraServiceImpl<UsuarioDTO, UsuarioFilter>
		implements UsuarioService, UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UsuarioMapper usuarioMapper;

	@Autowired
	@Qualifier("myPasswordEncoder")
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UsuarioEmpresaRepository usuarioEmpresaRepository;

	@Autowired
	private UsuarioSedeRepository usuarioSedeRepository;

	@Autowired
	private UsuarioInstalacionRepository usuarioInstalacionRepository;

	@Autowired
	private UsuarioRolRepository usuarioRolRepository;

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private MailService mailService;

	@Autowired
	private UsuarioTokenService usuarioTokenService;

	@Autowired
	private ReservaService reservaService;

	@Override
	public Page<UsuarioDTO> getPageByFilter(UsuarioFilter filter, Pageable pageable) {
		return usuarioMapper.pageToPageDTO(
				usuarioRepository.findAll(UsuarioSpecifications.filter(limitacionesPermisos(filter)), pageable));
	}

	private UsuarioFilter limitacionesPermisos(UsuarioFilter filter) {
		return filter;
	}

	@Override
	@Transactional
	public Long guardar(UsuarioDTO form) {
		Usuario model = usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(form.getUuid());
		model = usuarioMapper.dtoToModel(form, model);
		usuarioRepository.saveAndFlush(model);
		return model.getId();
	}

	@Override
	public UsuarioDTO findById(Long id) {
		return usuarioMapper.modelToDTO(usuarioRepository.findByActivoTrueAndId(id));
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

		List<Long> listEmpresaId = usuarioEmpresaRepository.findByActivoTrueAndUsuarioId(usuario.getId()).stream()
				.map(ue -> ue.getEmpresa().getId()).toList();

		List<Long> listSedeId = usuarioSedeRepository.findByActivoTrueAndUsuarioId(usuario.getId()).stream()
				.map(ue -> ue.getSede().getId()).toList();

		List<Long> listInstalacionId = usuarioInstalacionRepository.findByActivoTrueAndUsuarioId(usuario.getId())
				.stream().map(ue -> ue.getInstalacion().getId()).toList();

		return new CustomUserDetails(usuario.getId(), usuario.getNombre(), usuario.getPassword(), authorities,
				listEmpresaId, listSedeId, listInstalacionId);
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

		desactivar(usuarioEmpresaRepository.findByActivoTrueAndUsuarioId(id));
		desactivar(usuarioSedeRepository.findByActivoTrueAndUsuarioId(id));
		desactivar(usuarioInstalacionRepository.findByActivoTrueAndUsuarioId(id));
		desactivar(usuarioRolRepository.findByActivoTrueAndUsuarioId(id));
	}

	@Override
	public Long guardarDatos(UsuarioDTO form) {
		Usuario model = usuarioRepository.findByActivoTrueAndId(form.getId());
		model = usuarioMapper.dtoToModel(form, model);
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
	@Transactional
	public Long registrarUsuarioCliente(UsuarioRegistroDTO dto) {
		Usuario usuario = usuarioMapper.registroDTOToModel(dto);
		usuarioRepository.saveAndFlush(usuario);
		Rol rol = rolRepository.findByActivoTrueAndNombreContainsIgnoreCase(Constantes.Rol.USUARIO_CLIENTE);
		UsuarioRol usuarioRol = new UsuarioRol(usuario, rol);
		usuarioRolRepository.saveAndFlush(usuarioRol);
		return usuario.getId();
	}

	@Override
	public UsuarioDTO findByUuid(String uuid) {
		return usuarioMapper.modelToDTO(usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid));
	}

	@Override
	public void eliminar(String uuid) {
		Usuario usuario = usuarioRepository.findByActivoTrueAndUuidEqualsIgnoreCase(uuid);
		eliminar(usuario.getId());

	}

	@Override
	public MiPerfilDTO getMiPerfilDTO() {
		Usuario usuario = usuarioRepository.findByActivoTrueAndId(SecurityUtil.getCurrentUserId());
		return usuarioMapper.modelToMiPerfilDTO(usuario);
	}

	@Override
	public MiPerfilPasswordDTO getMiPerfilPasswordDTO() {
		Usuario usuario = usuarioRepository.findByActivoTrueAndId(SecurityUtil.getCurrentUserId());
		return usuarioMapper.modelToMiPerfilPasswordDTO(usuario);
	}

	@Override
	public void actualizarPassword(MiPerfilPasswordDTO dto) {
		Usuario usuario = usuarioRepository.findByActivoTrueAndId(dto.getId());
		usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
		usuarioRepository.saveAndFlush(usuario);
	}

	@Override
	public List<UsuarioDTO> getListDTO() {
		return Utils.sortByNombre(usuarioMapper.listModelToListDTO(usuarioRepository.findByActivoTrue()));
	}

	@Override
	public List<UsuarioDTO> getListDTO(UsuarioFilter filter) {

		return Utils.sortByNombre(usuarioMapper.listModelToListDTO(
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
	@Transactional
	public Long registrarUsuarioEmpresa(@Valid EmpresaRegistroDTO dto) {
		Usuario usuario = usuarioMapper.registroEmpresaDTOToModel(dto);
		usuarioRepository.saveAndFlush(usuario);

		return usuario.getId();
	}

	@Override
	public void actualizarMiPerfil(@Valid MiPerfilDTO dto) {
		Usuario usuario = usuarioRepository.findByActivoTrueAndId(dto.getId());
		usuario.setNombre(dto.getNombre());
		usuario.setEmail(dto.getEmail());
		usuarioRepository.saveAndFlush(usuario);
	}

	@Override
	public UsuarioDTO findByNombreEqualsIgnoreCase(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ComboDTO> getListComboDTO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enviarMailPasswordOlvidada(UsuarioPasswordDTO dto) {
		Usuario usuario = usuarioRepository.findByActivoTrueAndEmailEqualsIgnoreCase(dto.getEmail());
		if (usuario != null) {
			try {
				mailService.mensajeUsuarioPasswordOlvidada(usuario);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	@Override
	public void borrarMiCuenta() {
		Usuario usuario = usuarioRepository.findByActivoTrueAndId(SecurityUtil.getCurrentUserId());
		usuario.setNombre("usuario_eliminado");
		usuario.setEmail("usuario_eliminado@tfb_carlemany_apm_2026.com");
		eliminar(SecurityUtil.getCurrentUserId());
		reservaService.cancelarUsuarioFechaDesde(SecurityUtil.getCurrentUserId(), LocalDate.now());
		usuarioRepository.saveAndFlush(usuario);
	}

	@Override
	public void generarPasswordYEnviarMail(UsuarioPasswordDTO dto) {
		UsuarioToken usuarioToken = usuarioTokenService.getTokenActivoByUuid(dto.getUuid());

		if (usuarioToken != null) {

			String password = Utils.generarStringAleatorio(12);
			Usuario usuario = usuarioToken.getUsuario();
			usuarioTokenService.desactivarTokensByUsuarioId(usuario.getId());
			usuario.setPassword(passwordEncoder.encode(password));
			usuarioRepository.saveAndFlush(usuario);
			try {
				mailService.mensajeUsuarioNuevaPassword(usuario, password);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

}
