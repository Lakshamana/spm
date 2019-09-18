package br.ufpa.labes.spm.service.interfaces;

import java.util.List;


import br.ufpa.labes.spm.service.dto.ActivitysDTO;
import br.ufpa.labes.spm.service.dto.ProjectsDTO;
import br.ufpa.labes.spm.service.dto.ProcessDTO;
import br.ufpa.labes.spm.service.dto.ProcessesDTO;

@Remote
public interface ProcessServices {

	public ProjectsDTO getProjectsForAgent(String agentIdent);

	public List<ProcessDTO> getProcess(String agentIdent);

	public ActivitysDTO getActitivitiesFromProcess(String processIdent);

	public ProcessesDTO getProjectsManagedBy(String agentIdent);

}
