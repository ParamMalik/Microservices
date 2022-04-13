package com.training.cards.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.cards.model.CardsModel;

@Repository
public interface CardsRepository extends JpaRepository<CardsModel, Integer> {

//	@Query("select s from cards s where s.customerId = ?1")
	List<CardsModel> findByCustomerId(Integer id);
}
