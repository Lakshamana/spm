package br.ufpa.labes.spm.repository.interfaces.connections;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.BranchAND;

@Local
public interface IBranchANDDAO extends IBaseDAO<BranchAND, String>{

}
