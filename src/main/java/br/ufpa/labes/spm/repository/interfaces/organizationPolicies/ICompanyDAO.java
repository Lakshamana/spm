package br.ufpa.labes.spm.repository.interfaces.organizationPolicies;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Company;

@Local
public interface ICompanyDAO extends IBaseDAO<Company, String>{

	boolean alreadyExistCNPJ(String cnpj, String ident);
}
