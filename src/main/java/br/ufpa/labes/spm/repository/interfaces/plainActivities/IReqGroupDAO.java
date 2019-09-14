package br.ufpa.labes.spm.repository.interfaces.plainActivities;



import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ReqGroup;

public interface IReqGroupDAO extends IBaseDAO<ReqGroup, Integer>{

	public ReqGroup findReqGroupFromProcessModel(String groupIdent, String groupTypeIdent, String normalIdent);
}
