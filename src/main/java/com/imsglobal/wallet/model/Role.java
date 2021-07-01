package com.imsglobal.wallet.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="roles")
public class Role {
	@Id
	@GeneratedValue
	private Long id;
	@Enumerated(EnumType.STRING)
	private RoleType type;
	public Role() {}
	public Role(RoleType type) {
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public RoleType getType() {
		return type;
	}
	public void setType(RoleType type) {
		this.type = type;
	}
	
	
}
