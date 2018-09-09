package org.abarhub.angerona.web.rest;

import org.abarhub.angerona.web.dto.DemandeDTO;
import org.abarhub.angerona.web.dto.ReponseDTO;
import org.abarhub.angerona.web.services.CryptageService;
import org.abarhub.angerona.web.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class MainController {

	private static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private CryptageService cryptageService;

	@RequestMapping(value = "/message", method = RequestMethod.POST,
			produces = "application/json", consumes = "application/json")
	public ReponseDTO getMessage(@RequestBody DemandeDTO demandeDTO) throws Exception {
		ReponseDTO reponseDTO;

		LOGGER.info("demandeDTO={}", demandeDTO);

		String password = Base64Util.decode(demandeDTO.getPassword());

		String cle = Base64Util.decode(demandeDTO.getCle());

		reponseDTO = cryptageService.getMessage(password, cle);

		return reponseDTO;
	}
}