package com.gestion.deportiva.controller.publico;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gestion.deportiva.dto.MunicipioDTO;
import com.gestion.deportiva.dto.ProvinciaDTO;
import com.gestion.deportiva.dto.SedeDTO;
import com.gestion.deportiva.service.MunicipioService;
import com.gestion.deportiva.service.ProvinciaService;
import com.gestion.deportiva.service.SedeService;

@Controller
public class PublicoRestController {

	private final ProvinciaService provinciaService;

	private final MunicipioService municipioService;

	private final SedeService sedeService;

	PublicoRestController(ProvinciaService provinciaService, MunicipioService municipioService,
			SedeService sedeService) {
		this.provinciaService = provinciaService;
		this.municipioService = municipioService;
		this.sedeService = sedeService;
	}

	@GetMapping("/api/provincias")
	@ResponseBody
	public List<ProvinciaDTO> getProvincias(@RequestParam Long padreId) {
		return provinciaService.getListDTOByComunidadAutonomaId(padreId);
	}

	@GetMapping("/api/municipios")
	@ResponseBody
	public List<MunicipioDTO> getMunicipios(@RequestParam Long padreId, @RequestParam(required = false) String tipo) {
		if ("comunidad".equals(tipo)) {
			return municipioService.getListDTOByComunidadAutonomaIdOrProvinciaId(padreId, null);
		}
		return municipioService.getListDTOByComunidadAutonomaIdOrProvinciaId(null, padreId);
	}

	@GetMapping("/api/sede/list/instalacion")
	@ResponseBody
	public List<SedeDTO> getSedesInstalacion(@RequestParam Long padreId) {
		return sedeService.getListDTOParaInstalacion(padreId);
	}

}
