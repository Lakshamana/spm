package br.ufpa.labes.spm.repository.interfaces.organizationPolicies;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Repository;
import br.ufpa.labes.spm.domain.Structure;

@Local
public interface IRepositoryDAO extends IBaseDAO<Repository, String>{
	Structure getTheStructure(String ident);
}
