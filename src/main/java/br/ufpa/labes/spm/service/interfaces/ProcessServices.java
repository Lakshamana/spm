package org.qrconsult.spm.services.interfaces;

import java.util.List;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.formActivity.ActivitysDTO;
import org.qrconsult.spm.dtos.formProject.ProjectsDTO;
import org.qrconsult.spm.dtos.process.ProcessDTO;
import org.qrconsult.spm.dtos.process.ProcessesDTO;

@Remote
public interface ProcessServices {

	public ProjectsDTO getProjectsForAgent(String agentIdent);

	public List<ProcessDTO> getProcess(String agentIdent);

	public ActivitysDTO getActitivitiesFromProcess(String processIdent);

	public ProcessesDTO getProjectsManagedBy(String agentIdent);
	
}
