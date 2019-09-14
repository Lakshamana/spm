package br.ufpa.labes.spm.repository.impl.organizationPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IStructureDAO;
import br.ufpa.labes.spm.domain.Repository;
import br.ufpa.labes.spm.domain.Structure;

public class StructureDAO extends BaseDAO<Structure, String> implements IStructureDAO{

	protected StructureDAO(Class<Structure> businessClass) {
		super(businessClass);

	}

	public StructureDAO() {
		super(Structure.class);
	}

}
