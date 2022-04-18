package com.training.accounts.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerDetails {

	private AccountsModel accounts;
	private List<LoansModel> loans;
	private List<CardsModel> cards;

	public CustomerDetails() {
		super();
	}

	public CustomerDetails(AccountsModel accounts, List<LoansModel> loans, List<CardsModel> cards) {
		this.accounts = accounts;
		this.loans = loans;
		this.cards = cards;
	}

}
