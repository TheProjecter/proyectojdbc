package cl.chilefactory.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String password;
	private Timestamp expiring;
	private Set roles = new HashSet();

	
	public Timestamp getExpiring() {
		return expiring;
	}
	public void setExpiring(Timestamp expiring) {
		this.expiring = expiring;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set getRoles() {
		return roles;
	}
	public void setRoles(Set roles) {
		this.roles = roles;
	}
	
	
}
