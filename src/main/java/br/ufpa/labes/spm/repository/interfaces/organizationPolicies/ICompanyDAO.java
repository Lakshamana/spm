package br.ufpa.labes.spm.repository.interfaces.organizationPolicies;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Company;

public interface ICompanyDAO extends IBaseDAO<Company, Long> {

  boolean alreadyExistCNPJ(String cnpj, String ident);
}
