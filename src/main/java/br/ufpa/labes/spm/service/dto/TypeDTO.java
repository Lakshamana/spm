package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class TypeDTO implements Serializable {

	private Long oid;
	private String ident;
	private String description;
	private Boolean userDefined;
	@IgnoreMapping
	private String superTypeIdent;
	@IgnoreMapping
	private Integer subtypesNumber;
	@IgnoreMapping
	private String rootType;

	public TypeDTO() {
		this.oid = null;
		this.ident = "";
		this.description = "";
		this.userDefined = new Boolean (false);
		this.superTypeIdent = "";
		this.subtypesNumber = 0;
		this.rootType = "";
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getUserDefined() {
		return userDefined;
	}

	public Boolean isUserDefined() {
		return userDefined;
	}

	public void setUserDefined(Boolean userDefined) {
		this.userDefined = userDefined;
	}

	public String getSuperTypeIdent() {
		return superTypeIdent;
	}

	public void setSuperTypeIdent(String superTypeIdent) {
		this.superTypeIdent = superTypeIdent;
	}

	public Integer getSubtypesNumber() {
		return subtypesNumber;
	}

	public void setSubtypesNumber(Integer subtypesNumber) {
		this.subtypesNumber = subtypesNumber;
	}

	public String getRootType() {
		return rootType;
	}

	public void setRootType(String rootType) {
		this.rootType = rootType;
	}

}
