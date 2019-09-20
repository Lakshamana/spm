package br.ufpa.labes.spm.service.interfaces;

import java.util.List;


import br.ufpa.labes.spm.service.dto.ProjectDTO;
import br.ufpa.labes.spm.service.dto.DevelopingSystemDTO;
import br.ufpa.labes.spm.service.dto.SystemsDTO;


public interface SystemServices {
	public DevelopingSystemDTO getSystem(String nameSystem);

	public DevelopingSystemDTO saveSystem(DevelopingSystemDTO systemDTO);

	public Boolean removeSystem(DevelopingSystemDTO systemDTO);

	public SystemsDTO getSystems(String termoBusca, String domainFilter);

	public SystemsDTO getSystems();

	public Boolean removeProjectToSystem(DevelopingSystemDTO systemDTO);

	public List<ProjectDTO> getProjectToSystem();
}
