package com.training.accounts.model;

import java.sql.Date;
import java.time.LocalDate;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoansModel {

	private Long loanNumber;
	private Integer customerId;
	private Date StartDt;
	private String loanType;
	private Integer totalLoan;
	private Integer amountPaid;
	private Integer outstandingAmount;
	private LocalDate createDt;

}
