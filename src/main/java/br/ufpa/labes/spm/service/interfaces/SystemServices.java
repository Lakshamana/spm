package br.ufpa.labes.spm.service.interfaces;

import java.util.List;


import br.ufpa.labes.spm.service.dto.ProjectDTO;
import br.ufpa.labes.spm.service.dto.SystemDTO;
import br.ufpa.labes.spm.service.dto.SystemsDTO;




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
