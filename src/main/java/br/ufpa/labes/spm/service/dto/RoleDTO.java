package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.List;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;
import br.ufpa.labes.spm.service.dto.AbilityDTO;
import br.ufpa.labes.spm.service.dto.AgentDTO;

@SuppressWarnings("serial")
public class RoleDTO implements Serializable{
	/**
	 *
	 */

	private Integer oid;

	private String ident;

	private String name;

	private String description;


	@IgnoreMapping
	private String superType;

	@IgnoreMapping
	private List<AbilityDTO> abilityToRole;

	@IgnoreMapping
	private Integer nivelAbility;

	@IgnoreMapping
	private List<AgentDTO> agentToRole;

	public List<AgentDTO> getAgentToRole() {
		return agentToRole;
	}

	public void setAgentToRole(List<AgentDTO> agentToRole) {
		this.agentToRole = agentToRole;
	}

	public List<AbilityDTO> getAbilityToRole() {
		return abilityToRole;
	}

	public void setAbilityToRole(List<AbilityDTO> abilityToRole) {
		this.abilityToRole = abilityToRole;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSuperType() {
		return superType;
	}

	public void setSuperType(String superType) {
		this.superType = superType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNivelAbility() {
		return nivelAbility;
	}

	public void setNivelAbility(Integer nivelAbility) {
		this.nivelAbility = nivelAbility;
	}

}
