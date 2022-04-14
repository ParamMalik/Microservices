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

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

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

	// Implementing RateLimiter Pattern

	@GetMapping(path = "/status")
	@RateLimiter(name = "getStatusLimiter", fallbackMethod = "getStatusFallBack")
	public String getStatus() {
		return "apis are up and running";
	}

	private String getStatusFallBack(Throwable t) {
		return "This is getStatus Fall Back Mthod";
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

	// This Is Circuit Breaker Pattern of Resilience4j

	// We can provide custom configuration in property file for circuit breaker
	// that after how many request it has to open and when(time) to half open when
	// to open again.

	@PostMapping(path = "/myCustomerDetails")
//	@CircuitBreaker(name = "customerDetailsForBankApp", fallbackMethod = "customFallBackMethodForGetCustomerDetails")

	@Retry(name = "retryForMyCustomerDetails", fallbackMethod = "customFallBackMethodForGetCustomerDetails")
	public CustomerDetails getCustomerDetails(@RequestBody Customer customer) {
		AccountsModel customerWithTheGivenId = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<LoansModel> loanDetails = loansFeignClient.getLoanDetails(customer);
		List<CardsModel> cardDetails = cardsFeignClient.getCardDetails(customer);
		CustomerDetails customerDetails = new CustomerDetails(customerWithTheGivenId, loanDetails, cardDetails);

		return customerDetails;

	}

	// There are some conditions for fall back method to follow
	// fallBack method must accept the same parameters as the original method and it
	// should also
	// accept an object of Throwable as a parameter

	private CustomerDetails customFallBackMethodForGetCustomerDetails(Customer customer, Throwable t) {
		AccountsModel customerWithTheGivenId = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<LoansModel> loanDetails = loansFeignClient.getLoanDetails(customer);
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(customerWithTheGivenId);
		customerDetails.setLoans(loanDetails);

		return customerDetails;
	}

}
