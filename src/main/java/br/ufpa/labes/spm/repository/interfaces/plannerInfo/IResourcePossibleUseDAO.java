package br.ufpa.labes.spm.repository.interfaces.plannerInfo;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ResourcePossibleUse;

@Local
public interface IResourcePossibleUseDAO extends IBaseDAO<ResourcePossibleUse, Integer>{

}
