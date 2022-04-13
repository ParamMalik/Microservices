package com.training.loans.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.training.loans.config.LoansServiceConfig;
import com.training.loans.model.Customer;
import com.training.loans.model.LoansModel;
import com.training.loans.model.LoansPropertiesFromGit;
import com.training.loans.repo.LoansRepository;

@RestController
public class LoansController {

	@Autowired
	private LoansRepository loansRepository;

	@Autowired
	private LoansServiceConfig loansServiceConfig;

	@GetMapping(path = "/status")
	public String getStatus() {
		return "apis are up and running";
	}

	@PostMapping("/myLoans")
	public List<LoansModel> getLoanDetails(@RequestBody Customer customerId) {
		List<LoansModel> customerLoanDetailsByCustomerId = loansRepository.findByCustomerId(customerId.getCustomerId());

		return customerLoanDetailsByCustomerId;

	}

	@PostMapping("/save")
	public LoansModel setLoanDetails(@RequestBody LoansModel loansModel) {
		var saveAccountDetails = loansRepository.save(loansModel);
		return saveAccountDetails;

	}

	@GetMapping("/loans/properties")
	public String getLoansProperties() throws JsonProcessingException {
		ObjectWriter withDefaultPrettyPrinter = new ObjectMapper().writer().withDefaultPrettyPrinter();
		LoansPropertiesFromGit loansPropertiesFromGit = new LoansPropertiesFromGit(loansServiceConfig.getMsg(),
				loansServiceConfig.getBuildVersion(), loansServiceConfig.getMailDetails(),
				loansServiceConfig.getActiveBranches());

		String loansPropertiesFromGitAsString = withDefaultPrettyPrinter.writeValueAsString(loansPropertiesFromGit);
		return loansPropertiesFromGitAsString;
	}
}
