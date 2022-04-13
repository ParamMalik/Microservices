package com.training.accounts.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity(name = "Accounts")
public class AccountsModel {

	private Integer customerId;
	@Id
	private Long accountNumber;
	private String accountType;
	private String branchAddress;
	private LocalDate createDt;

}
