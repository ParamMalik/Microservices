package com.training.accounts.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CardsModel {

	private Integer cardId;
	private Integer customerId;
	private String cardNumber;
	private String cardType;
	private Integer totalLimit;
	private Integer amountUsed;
	private Integer availableAmount;
	private LocalDate createDt;

}
