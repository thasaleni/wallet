package com.imsglobal.wallet.repository;

import org.springframework.data.repository.CrudRepository;

import com.imsglobal.wallet.model.User;

public interface UserRepository extends CrudRepository<User, Long>{

	User findByUsername(String username);
	
	boolean existsByUsername(String username);

}
