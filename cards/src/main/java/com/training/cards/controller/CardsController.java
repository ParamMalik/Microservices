package com.training.cards.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.training.cards.config.CardsServiceConfig;
import com.training.cards.model.CardPropertiesFromGitViaConfigServer;
import com.training.cards.model.CardsModel;
import com.training.cards.model.Customer;
import com.training.cards.repo.CardsRepository;

@RestController
public class CardsController {

	@Autowired
	private CardsRepository cardsRepository;

	@Autowired
	private CardsServiceConfig cardsServiceConfig;

	@GetMapping(path = "/status")
	public String getStatus() {
		return "apis are up and running";
	}

	@PostMapping("/myCards")
	public List<CardsModel> getCardsDetails(@RequestBody Customer customerId) {
		List<CardsModel> cusotmerCardDeatilsWithTheGivenCustomerId = cardsRepository
				.findByCustomerId(customerId.getCustomerId());

		return cusotmerCardDeatilsWithTheGivenCustomerId;
	}

	@PostMapping("/save")
	public CardsModel setCardsDetails(@RequestBody CardsModel cardsModel) {
		CardsModel save = cardsRepository.save(cardsModel);

		return save;

	}

	@GetMapping("/cards/properties")
	public String getCardsProperties() throws JsonProcessingException {
		ObjectWriter withDefaultPrettyPrinter = new ObjectMapper().writer().withDefaultPrettyPrinter();
		CardPropertiesFromGitViaConfigServer cardPropertiesFromGitViaConfigServer = new CardPropertiesFromGitViaConfigServer(
				cardsServiceConfig.getMsg(), cardsServiceConfig.getBuildVersion(), cardsServiceConfig.getMailDetails(),
				cardsServiceConfig.getActiveBranches());

		String cardPropertiesFromGitViaConfigServerAsString = withDefaultPrettyPrinter
				.writeValueAsString(cardPropertiesFromGitViaConfigServer);
		return cardPropertiesFromGitViaConfigServerAsString;
	}

}
