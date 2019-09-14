package br.ufpa.labes.spm.repository.interfaces.plainActivities;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ReqAgentRequiresAbility;

@Local
public interface IReqAgentRequiresAbilityDAO extends IBaseDAO<ReqAgentRequiresAbility, Integer>{

}
