package br.ufpa.labes.spm.repository.interfaces.organizationPolicies;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.VCSRepository;
import br.ufpa.labes.spm.domain.Structure;

public interface IRepositoryDAO extends IBaseDAO<VCSRepository, String> {
  Structure getTheStructure(String ident);
}
