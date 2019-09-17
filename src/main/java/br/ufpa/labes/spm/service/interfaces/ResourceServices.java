package org.qrconsult.spm.services.interfaces;

import java.sql.SQLException;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.formResources.ResourceDTO;
import org.qrconsult.spm.dtos.formResources.ResourcesDTO;
import org.qrconsult.spm.dtos.formTypes.TypesDTO;
import org.qrconsult.spm.model.resources.Resource;

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
