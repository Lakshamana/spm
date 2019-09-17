package org.qrconsult.spm.services.interfaces;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.formAgent.AgentDTO;
import org.qrconsult.spm.dtos.formAgent.AgentsDTO;
import org.qrconsult.spm.dtos.formGroup.GroupDTO;
import org.qrconsult.spm.dtos.formGroup.GroupsDTO;
import org.qrconsult.spm.dtos.formTypes.TypesDTO;

@Remote
public interface GroupServices {

	public TypesDTO getGroupTypes();
	
	public GroupDTO saveGroup(GroupDTO groupDTO);	
	
	public GroupsDTO getGroups();
	
	public GroupDTO getGroup(String groupName);

	public GroupsDTO getGroups(String searchTerm, String typeFilter, Boolean ativoFilter);
	
	public AgentsDTO getAgents();
	
	public AgentDTO getAgent(String agentName);
	
	public Boolean removeAgent(String groupName, String agentName);
	
	public Boolean removeGroup(String groupName);
}
