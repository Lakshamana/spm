package br.ufpa.labes.spm.repository.impl.plainActivities;

import java.util.List;

import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IReqAgentDAO;
import br.ufpa.labes.spm.domain.ReqAgent;

public class ReqAgentDAO extends BaseDAO<ReqAgent, Integer> implements IReqAgentDAO {

  protected ReqAgentDAO(Class<ReqAgent> businessClass) {
    super(businessClass);
  }

  public ReqAgentDAO() {
    super(ReqAgent.class);
  }

  public ReqAgent findReqAgentFromProcessModel(
      String agentIdent, String roleIdent, String normalIdent) {
    List<ReqAgent> retorno = null;
    if (agentIdent != null && !agentIdent.equals("")) {

      String hql =
          "select obj from "
              + ReqAgent.class.getName()
              + " as obj where obj.theAgent.ident=:agentIdent AND obj.theRole.ident=:roleIdent AND obj.theNormal.ident=:normalIdent";
      Query query = getPersistenceContext().createQuery(hql);
      query.setParameter("agentIdent", agentIdent);
      query.setParameter("roleIdent", roleIdent);
      query.setParameter("normalIdent", normalIdent);

      retorno = (List<ReqAgent>) query.getResultList();
    } else {

      String hql =
          "select obj from "
              + ReqAgent.class.getName()
              + " as obj where obj.theRole.ident=:roleIdent AND obj.theNormal.ident=:normalIdent";
      Query query = getPersistenceContext().createQuery(hql);
      query.setParameter("roleIdent", roleIdent);
      query.setParameter("normalIdent", normalIdent);

      retorno = (List<ReqAgent>) query.getResultList();
    }

    if (retorno != null) {
      if (!retorno.isEmpty()) {
        return retorno.get(0);
      } else {
        return null;
      }
    } else return null;
  }
}
