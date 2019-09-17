package org.qrconsult.spm.services.interfaces;

import java.util.List;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.formProject.ProjectDTO;
import org.qrconsult.spm.dtos.formSystem.SystemDTO;
import org.qrconsult.spm.dtos.formSystem.SystemsDTO;




@Remote
public interface SystemServices {
	public SystemDTO getSystem(String nameSystem);
	
	public SystemDTO saveSystem(SystemDTO systemDTO);
	
	public Boolean removeSystem(SystemDTO systemDTO);
	
	public SystemsDTO getSystems(String termoBusca, String domainFilter);
	
	public SystemsDTO getSystems();
	
	public Boolean removeProjectToSystem(SystemDTO systemDTO);
	
	public List<ProjectDTO> getProjectToSystem();
}
