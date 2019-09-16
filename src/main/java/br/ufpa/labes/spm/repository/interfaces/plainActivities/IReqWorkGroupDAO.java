package br.ufpa.labes.spm.repository.interfaces.plainActivities;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ReqWorkGroup;

public interface IReqWorkGroupDAO extends IBaseDAO<ReqWorkGroup, Integer> {

  public ReqWorkGroup findReqWorkGroupFromProcessModel(
      String groupIdent, String WorkgroupTypeIdent, String normalIdent);
}
