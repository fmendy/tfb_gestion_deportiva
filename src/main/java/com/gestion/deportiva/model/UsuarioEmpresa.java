package com.gestion.deportiva.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario_empresa")
public class UsuarioEmpresa extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 719893123592917927L;

	public UsuarioEmpresa(Long id) {
		super(id);
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", nullable = false)
	private Empresa empresa;

	@Column(name = "id_usuario", nullable = false)
	private Usuario usuario;

}
