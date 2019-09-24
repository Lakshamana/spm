package br.ufpa.labes.spm.service.interfaces;


import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.service.dto.AgentsDTO;
import br.ufpa.labes.spm.service.dto.WorkGroupDTO;
import br.ufpa.labes.spm.service.dto.WorkGroupsDTO;
import br.ufpa.labes.spm.service.dto.TypesDTO;

@Remote
public interface WorkGroupServices {

	public TypesDTO getWorkGroupTypes();

	public WorkGroupDTO saveWorkGroup(WorkGroupDTO WorkGroupDTO);

	public WorkGroupsDTO getWorkGroups();

	public WorkGroupDTO getWorkGroup(String WorkGroupName);

	public WorkGroupsDTO getWorkGroups(String searchTerm, String typeFilter, Boolean ativoFilter);

	public AgentsDTO getAgents();

	public AgentDTO getAgent(String agentName);

	public Boolean removeAgent(String WorkGroupName, String agentName);

	public Boolean removeWorkGroup(String WorkGroupName);
}
