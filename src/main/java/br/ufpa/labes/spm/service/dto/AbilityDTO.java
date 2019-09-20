package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;


@SuppressWarnings("serial")
public class AbilityDTO implements Serializable{
	private Long oid;
	private String ident;
	@IgnoreMapping
	private String abilityType;
	private String name;
	private String description;

	public AbilityDTO() {
		this.oid = null;
		this.ident = " ";
		this.name = " ";
		this.description = " ";
	}

	public Integer getId() {
		return oid;
	}

	public void setId(Long oid) {
		this.oid = oid;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAbilityType() {
		return abilityType;
	}

	public void setAbilityType(String abilityType) {
		this.abilityType = abilityType;
	}


}
