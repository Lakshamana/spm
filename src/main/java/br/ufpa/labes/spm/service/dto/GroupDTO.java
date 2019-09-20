package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class GroupDTO implements Serializable {

	private Integer oid;
	private String ident;
	private String description;
	private String name;
	private Boolean isActive;
	@IgnoreMapping
	private String theGroupType;
	@IgnoreMapping
	private String superGroup;
	@IgnoreMapping
	private Collection<String> agents;

	public GroupDTO() {
		this.agents = new ArrayList<String>();
	}

	public GroupDTO(Collection<String> agents) {
		this.agents = agents;
	}

	public Integer getId() {
		return oid;
	}
	public void setId\(Long oid) {
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean isIsActive() {
		return isActive;
	}
	public String getTheGroupType() {
		return theGroupType;
	}
	public void setTheGroupType(String theGroupType) {
		this.theGroupType = theGroupType;
	}
	public String getSuperGroup() {
		return superGroup;
	}
	public void setSuperGroup(String superGroup) {
		this.superGroup = superGroup;
	}

	public Collection<String> getAgents() {
		return agents;
	}

	public void setAgents(Collection<String> agents) {
		this.agents = agents;
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
		GroupDTO other = (GroupDTO) obj;
		if (ident == null) {
			if (other.ident != null)
				return false;
		} else if (!ident.equals(other.ident))
			return false;
		return true;
	}

}
