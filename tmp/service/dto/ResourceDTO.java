package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import br.ufpa.labes.spm.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class ResourceDTO implements Serializable{
	private Long id;
	private String ident;
	private String name;
	private String description;
	@IgnoreMapping
	private String mtbfTime;
	private String mtbfUnitTime;
	@IgnoreMapping
	private String cost;
	private String costString;
	private String currency;
	private Boolean staticOk;
	private Boolean isActive; //Added in 13/07/2008 by Murilo

	@IgnoreMapping
	private String belongsTo;
	@IgnoreMapping
	private String theResourceType;
	@IgnoreMapping
	private Collection<String> requires;
	@IgnoreMapping
	private Collection<String> isRequired;

	@IgnoreMapping
	private Collection<String> possess;

	public ResourceDTO() {
		super();
		ident = "";
		name = "";
		description = "";
		mtbfTime = "";
		mtbfUnitTime = "";
		cost = "";
		currency = "";
		staticOk = false;
		isActive = false;
		belongsTo = "";
		theResourceType = "";
		requires = new ArrayList<String>();
		isRequired = new ArrayList<String>();
		possess = new ArrayList<String>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getMtbfTime() {
		return mtbfTime;
	}

	public void setMtbfTime(String mtbfTime) {
		this.mtbfTime = mtbfTime;
	}

	public String getMtbfUnitTime() {
		return mtbfUnitTime;
	}

	public void setMtbfUnitTime(String mtbfUnitTime) {
		this.mtbfUnitTime = mtbfUnitTime;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public Boolean getStaticOk() {
		return staticOk;
	}

	public Boolean isStaticOk() {
		return staticOk;
	}

	public void setStaticOk(Boolean staticOk) {
		this.staticOk = staticOk;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public Boolean isIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getBelongsTo() {
		return belongsTo;
	}

	public void setBelongsTo(String belongsTo) {
		this.belongsTo = belongsTo;
	}

	public String getTheResourceType() {
		return theResourceType;
	}

	public void setTheResourceType(String theResourceType) {
		this.theResourceType = theResourceType;
	}

	public Collection<String> getRequires() {
		return requires;
	}

	public void setRequires(Collection<String> requires) {
		this.requires = requires;
	}

	public Collection<String> getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Collection<String> isRequired) {
		this.isRequired = isRequired;
	}

	public Collection<String> getPossess() {
		return possess;
	}

	public void setPossess(Collection<String> possess) {
		this.possess = possess;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ident == null) ? 0 : ident.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceDTO other = (ResourceDTO) obj;
		if (ident == null) {
			if (other.ident != null)
				return false;
		} else if (!ident.equals(other.ident))
			return false;
		return true;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCostString() {
		return costString;
	}

	public void setCostString(String costString) {
		this.costString = costString;
	}
}
