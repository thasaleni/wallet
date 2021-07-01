package com.imsglobal.wallet.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.imsglobal.wallet.model.Role;
import com.imsglobal.wallet.model.RoleType;

public interface RoleRepository extends CrudRepository<Role, Long> {
	Optional<Role> findByType(RoleType type);
}
