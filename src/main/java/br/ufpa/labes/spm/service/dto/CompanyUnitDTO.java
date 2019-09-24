
package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.ufpa.labes.spm.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class CompanyUnitDTO implements Serializable {
	private Long id;
	private String ident;
	private String name;
	private String description;

	@IgnoreMapping
	private String theAgent;
	@IgnoreMapping
	private CompanyUnitDTO theCommand;
	@IgnoreMapping
	private List<CompanyUnitDTO> theSubordinates;
	@IgnoreMapping
	private List<String> theUnitAgents;

	public CompanyUnitDTO(){
		ident = "";
		name = "";
		description = "";
		theSubordinates = new ArrayList<CompanyUnitDTO>();
		theUnitAgents = new ArrayList<String>();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CompanyUnitDTO other = (CompanyUnitDTO) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getTheAgent() {
		return theAgent;
	}

	public void setTheAgent(String theAgent) {
		this.theAgent = theAgent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CompanyUnitDTO getTheCommand() {
		return theCommand;
	}

	public void setTheCommand(CompanyUnitDTO theCommand) {
		this.theCommand = theCommand;
	}

	public List<CompanyUnitDTO> getTheSubordinates() {
		return theSubordinates;
	}

	public void setTheSubordinates(List<CompanyUnitDTO> theSubordinates) {
		this.theSubordinates = theSubordinates;
	}

	public List<String> getTheUnitAgents() {
		return theUnitAgents;
	}

	public void setTheUnitAgents(List<String> theUnitAgents) {
		this.theUnitAgents = theUnitAgents;
	}

	@Override
	public String toString() {
		if (theCommand == null)
			return "CompanyUnitDTO [name=" + name + "]";
		else return "CompanyUnitDTO [name=" + name + ", theCommand=" + theCommand.getName() +"]";
	}
}
