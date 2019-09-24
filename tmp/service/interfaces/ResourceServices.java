package br.ufpa.labes.spm.service.interfaces;

import java.sql.SQLException;

import javax.ejb.Remote;

import br.ufpa.labes.spm.service.dto.ResourceDTO;
import br.ufpa.labes.spm.service.dto.ResourcesDTO;
import br.ufpa.labes.spm.service.dto.TypesDTO;
import br.ufpa.labes.spm.domain.Resource;

@Remote
public interface ResourceServices {
	public ResourceDTO getResource(String resourceName);
	public ResourceDTO saveResource(ResourceDTO resourceDTO);
	public Boolean removeResource(ResourceDTO resourceDTO) throws SQLException;
	public ResourcesDTO getResources();
	public ResourcesDTO getRequiresResources(String resourceName);
	public Resource getResourceFromName(String resourceName);
	public TypesDTO getResourceTypes();
	public ResourcesDTO getResources(String termoBusca, String domainFilter, Boolean orgFilter);
	public ResourceDTO updateRequireds(String resourceName);
}
