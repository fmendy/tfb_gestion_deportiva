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
@Table(name = "instalacion_configuracion_reserva")
public class InstalacionConfiguracionReserva extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -3472927818751643488L;

	public InstalacionConfiguracionReserva(Long id) {
		super(id);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_instalacion", nullable = false)
	private Instalacion instalacion;

	@Column(name = "duracion_min", nullable = false)
	private Long duracionMin;

	@Column(name = "duracion_max", nullable = false)
	private Long duracionMax;
	
	@Column(name = "intervalo_horario", nullable = false)
	private Long intervaloHorario;
}
