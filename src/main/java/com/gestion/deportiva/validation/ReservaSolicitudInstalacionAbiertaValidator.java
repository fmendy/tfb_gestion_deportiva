package com.gestion.deportiva.validation;

import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.service.InstalacionHorarioBloqueadoService;
import com.gestion.deportiva.service.InstalacionHorarioEspecialService;
import com.gestion.deportiva.service.InstalacionHorarioService;
import com.gestion.deportiva.util.Utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReservaSolicitudInstalacionAbiertaValidator
		implements ConstraintValidator<ReservaSolicitudInstalacionAbiertaValid, ReservaSolicitudDTO> {

	private final InstalacionHorarioEspecialService instalacionHorarioEspecialService;

	private final InstalacionHorarioService instalacionHorarioService;

	private final InstalacionHorarioBloqueadoService instalacionHorarioBloqueadoService;

	ReservaSolicitudInstalacionAbiertaValidator(InstalacionHorarioEspecialService instalacionHorarioEspecialService,
			InstalacionHorarioService instalacionHorarioService,
			InstalacionHorarioBloqueadoService instalacionHorarioBloqueadoService) {
		this.instalacionHorarioEspecialService = instalacionHorarioEspecialService;
		this.instalacionHorarioService = instalacionHorarioService;
		this.instalacionHorarioBloqueadoService = instalacionHorarioBloqueadoService;
	}

	@Override
	public boolean isValid(ReservaSolicitudDTO dto, ConstraintValidatorContext context) {

		// Horario Especial de la instalación
		if (!instalacionHorarioEspecialService.estaAbierta(dto.getInstalacionId(), dto.getFecha(), dto.getHora(),
				dto.getDuracion())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.reserva.instalacion.abierta.horario.especial"))
					.addConstraintViolation();
			return false;
		}

		// Horario normal de la instalación
		if (!instalacionHorarioService.estaAbierta(dto.getInstalacionId(), dto.getFecha(), dto.getHora(),
				dto.getDuracion())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.reserva.instalacion.abierta.horario.habitual"))
					.addConstraintViolation();
			return false;
		}

		// Horario bloqueado de la instalación
		if (!instalacionHorarioBloqueadoService.estaDisponible(dto.getInstalacionId(), dto.getFecha(), dto.getHora(),
				dto.getDuracion())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					Utils.getMessage("error.validacion.reserva.instalacion.horario.bloqueado"))
					.addConstraintViolation();
			return false;
		}

		return true;
	}
}