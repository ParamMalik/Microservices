package com.training.loans.model;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
public class LoansModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loanNumber;
	private Integer customerId;
	private Date StartDt;
	private String loanType;
	private Integer totalLoan;
	private Integer amountPaid;
	private Integer outstandingAmount;
	private LocalDate createDt;

}
