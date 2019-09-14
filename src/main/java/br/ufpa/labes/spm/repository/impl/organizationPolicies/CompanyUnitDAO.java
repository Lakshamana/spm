package br.ufpa.labes.spm.repository.impl.organizationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.ICompanyUnitDAO;
import br.ufpa.labes.spm.domain.CompanyUnit;

@Stateless
public class CompanyUnitDAO extends BaseDAO<CompanyUnit, String> implements ICompanyUnitDAO{

	protected CompanyUnitDAO(Class<CompanyUnit> businessClass) {
		super(businessClass);
	}

	public CompanyUnitDAO() {
		super(CompanyUnit.class);
	}


}
