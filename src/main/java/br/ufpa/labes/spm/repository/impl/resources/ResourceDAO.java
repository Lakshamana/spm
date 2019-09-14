package br.ufpa.labes.spm.repository.impl.resources;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.resources.IResourceDAO;
import br.ufpa.labes.spm.domain.Resource;

@Stateless
public class ResourceDAO extends BaseDAO<Resource, String> implements IResourceDAO{

	protected ResourceDAO(Class<Resource> businessClass) {
		super(businessClass);
	}

	public ResourceDAO() {
		super(Resource.class);
	}


}
