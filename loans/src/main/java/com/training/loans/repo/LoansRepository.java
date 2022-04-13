package com.training.loans.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.loans.model.LoansModel;

public interface LoansRepository extends JpaRepository<LoansModel, Long> {
	List<LoansModel> findByCustomerId(Integer id);
}
