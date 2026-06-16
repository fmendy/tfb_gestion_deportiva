package com.gestion.deportiva.controller.publico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private ProvinciaService provinciaService;

	@Autowired
	private MunicipioService municipioService;
	
	@Autowired
	private SedeService sedeService;

	@GetMapping("/api/provincias")
	@ResponseBody
	public List<ProvinciaDTO> getProvincias(@RequestParam Long padreId) {
		return provinciaService.getListDTOByComunidadAutonomaId(padreId);
	}

	@GetMapping("/api/municipios")
	@ResponseBody
	public List<MunicipioDTO> getMunicipios(@RequestParam Long padreId, @RequestParam(required = false) String tipo) {
		// Si el tipo es 'comunidad', busca todos los municipios de la comunidad
		if ("comunidad".equals(tipo)) {
			return municipioService.getListDTOByComunidadAutonomaIdOrProvinciaId(padreId, null);
		}
		// Si no, busca por provincia
		return municipioService.getListDTOByComunidadAutonomaIdOrProvinciaId(null, padreId);
	}
	
	@GetMapping("/api/sede/list/instalacion")
	@ResponseBody
	public List<SedeDTO> getSedesInstalacion(@RequestParam Long padreId) {
		return sedeService.getListDTOParaInstalacion(padreId);
	}

}
