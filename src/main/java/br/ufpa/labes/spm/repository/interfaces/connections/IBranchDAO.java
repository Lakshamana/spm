package br.ufpa.labes.spm.repository.interfaces.connections;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Branch;

@Local
public interface IBranchDAO extends IBaseDAO<Branch, String>{

}
