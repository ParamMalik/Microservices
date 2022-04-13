package com.training.cards.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.ToString;

@Entity(name = "cards")
@Data
@ToString
public class CardsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cardId;
	private Integer customerId;
	private String cardNumber;
	private String cardType;
	private Integer totalLimit;
	private Integer amountUsed;
	private Integer availableAmount;
	private LocalDate createDt;

}
