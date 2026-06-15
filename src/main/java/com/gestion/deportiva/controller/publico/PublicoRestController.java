package com.gestion.deportiva.controller.publico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.service.MunicipioService;
import com.gestion.deportiva.service.ProvinciaService;

@Controller
public class PublicoRestController {

	@Autowired
	private ProvinciaService provinciaService;

	@Autowired
	private MunicipioService municipioService;

	@GetMapping("/api/provincias")
	@ResponseBody
	public List<ComboDTO> getProvincias(@RequestParam Long padreId) {
		return provinciaService.getListComboDTOByComunidadAutonomaId(padreId);
	}

	@GetMapping("/api/municipios")
	@ResponseBody
	public List<ComboDTO> getMunicipios(@RequestParam Long padreId, @RequestParam(required = false) String tipo) {
		// Si el tipo es 'comunidad', busca todos los municipios de la comunidad
		if ("comunidad".equals(tipo)) {
			return municipioService.getListComboDTOByComunidadAutonomaIdOrProvinciaId(padreId, null);
		}
		// Si no, busca por provincia
		return municipioService.getListComboDTOByComunidadAutonomaIdOrProvinciaId(null, padreId);
	}

}
