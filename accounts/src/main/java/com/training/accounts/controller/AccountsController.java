package com.training.accounts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.training.accounts.config.AccountsServiceConfig;
import com.training.accounts.model.AccountsModel;
import com.training.accounts.model.CardsModel;
import com.training.accounts.model.Customer;
import com.training.accounts.model.CustomerDetails;
import com.training.accounts.model.LoansModel;
import com.training.accounts.model.PropertiesFromGitViaConfigServer;
import com.training.accounts.repo.AccountsRepository;
import com.training.accounts.service.client.CardsFeignClient;
import com.training.accounts.service.client.LoansFeignClient;

@RestController
public class AccountsController {

	@Autowired
	private AccountsRepository accountsRepository;

	@Autowired
	private AccountsServiceConfig accountsServiceConfig;

	@Autowired
	private CardsFeignClient cardsFeignClient;

	@Autowired
	private LoansFeignClient loansFeignClient;

	@GetMapping(path = "/status")
	public String getStatus() {
		return "apis are up and running";
	}

	@PostMapping("/myAccounts")
	public AccountsModel getAccountDetails(@RequestBody AccountsModel accountsModel) {
		AccountsModel findByCustomerId = accountsRepository.findByCustomerId(accountsModel.getCustomerId());

		if (findByCustomerId != null) {
			return findByCustomerId;
		}

		return null;

	}

	@PostMapping("/save")
	public AccountsModel setAccountDetails(@RequestBody AccountsModel accountsModel) {
		AccountsModel saveAccountDetails = accountsRepository.save(accountsModel);
		if (saveAccountDetails != null) {
			return saveAccountDetails;
		}

		return null;

	}

	@GetMapping(path = "/accounts/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter withDefaultPrettyPrinter = new ObjectMapper().writer().withDefaultPrettyPrinter();
		PropertiesFromGitViaConfigServer propertiesFromGitViaConfigServer = new PropertiesFromGitViaConfigServer(
				accountsServiceConfig.getMsg(), accountsServiceConfig.getBuildVersion(),
				accountsServiceConfig.getMailDetails(), accountsServiceConfig.getActiveBranches());

		String propertiesFromGitViaConfigServerAsString = withDefaultPrettyPrinter
				.writeValueAsString(propertiesFromGitViaConfigServer);
		return propertiesFromGitViaConfigServerAsString;

	}

	@PostMapping(path = "/myCustomerDetails")
	public CustomerDetails getCustomerDetails(@RequestBody Customer customer) {
		AccountsModel customerWithTheGivenId = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<LoansModel> loanDetails = loansFeignClient.getLoanDetails(customer);
		List<CardsModel> cardDetails = cardsFeignClient.getCardDetails(customer);
		CustomerDetails customerDetails = new CustomerDetails(customerWithTheGivenId, loanDetails, cardDetails);

		return customerDetails;

	}
}
