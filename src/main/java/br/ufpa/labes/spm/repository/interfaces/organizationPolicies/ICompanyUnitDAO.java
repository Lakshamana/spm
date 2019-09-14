package br.ufpa.labes.spm.repository.interfaces.organizationPolicies;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.CompanyUnit;

@Local
public interface ICompanyUnitDAO extends IBaseDAO<CompanyUnit, String>{

}
