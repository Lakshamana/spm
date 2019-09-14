package br.ufpa.labes.spm.repository.interfaces.plainActivities;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ReqGroup;

@Local
public interface IReqGroupDAO extends IBaseDAO<ReqGroup, Integer>{

	public ReqGroup findReqGroupFromProcessModel(String groupIdent, String groupTypeIdent, String normalIdent);
}
