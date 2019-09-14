package br.ufpa.labes.spm.repository.impl.types;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IResourceTypeDAO;
import br.ufpa.labes.spm.domain.ResourceType;

@Stateless
public class ResourceTypeDAO extends BaseDAO<ResourceType, String> implements IResourceTypeDAO{

	protected ResourceTypeDAO(Class<ResourceType> businessClass) {
		super(businessClass);
	}

	public ResourceTypeDAO() {
		super(ResourceType.class);
	}

}
