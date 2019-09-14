package br.ufpa.labes.spm.repository.interfaces.connections;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.BranchCondToActivity;

@Local
public interface IBranchCondToActivityDAO extends IBaseDAO<BranchCondToActivity, Integer>{

}
