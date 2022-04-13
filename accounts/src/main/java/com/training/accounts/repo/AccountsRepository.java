package com.training.accounts.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.accounts.model.AccountsModel;

@Repository
public interface AccountsRepository extends JpaRepository<AccountsModel, Long> {
	AccountsModel findByCustomerId(Integer id);
}
