package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufpa.labes.spm.service.dto.ArtifactDTO;

@SuppressWarnings("serial")
public class ResourcesDTO implements Serializable {
	private List<ResourceDTO> resources;

	public ResourcesDTO(){
	}

	public ResourcesDTO(List<ResourceDTO> resources) {
		super();
		this.resources = resources;
	}

	public boolean addResourceDTO(ResourceDTO resourceDTO) {
		return resources.add(resourceDTO);
	}

	public ResourceDTO getResourceDTO(int index) {
		return resources.get(index);
	}

	public boolean isEmpty() {
		return this.resources.isEmpty();
	}

	public List<String> getResourcesNames() {
		List<String> resourcesNames = new ArrayList<String>();

		for (ResourceDTO resourceDTO : resources) {
			resourcesNames.add(resourceDTO.getName());
		}

		return resourcesNames;
	}

	public List<String> getResourcesIdents() {
		List<String> resourcesIdents = new ArrayList<String>();

		for (ResourceDTO resourceDTO : resources) {
			resourcesIdents.add(resourceDTO.getIdent());
		}

		return resourcesIdents;
	}

	public Map<String, String> getResourceNameIdentMap() {
		Map<String, String> map = new HashMap<String, String>();
		for (ResourceDTO resource : resources) {
			map.put(resource.getName(), resource.getIdent());
		}

		return map;
	}
}
