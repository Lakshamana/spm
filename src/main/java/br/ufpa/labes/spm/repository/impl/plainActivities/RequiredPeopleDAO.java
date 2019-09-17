package br.ufpa.labes.spm.repository.impl.plainActivities;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IRequiredPeopleDAO;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.domain.ReqWorkGroup;
import br.ufpa.labes.spm.domain.RequiredPeople;

public class RequiredPeopleDAO extends BaseDAO<RequiredPeople, Integer>
    implements IRequiredPeopleDAO {

  protected RequiredPeopleDAO(Class<RequiredPeople> businessClass) {
    super(businessClass);
  }

  public RequiredPeopleDAO() {
    super(RequiredPeople.class);
  }

  public Collection<String> getReqPeopleEmails(String normalIdent) {
    getPersistenceContext().getTransaction().begin();

    String hql =
        "from "
            + RequiredPeople.class.getName()
            + " as reqPeople where reqPeople.theNormal.ident = :normalIdent";

    Query query = getPersistenceContext().createQuery(hql);
    query.setParameter("normalIdent", normalIdent);

    Collection<String> toReturn = new HashSet<String>();

    Collection<RequiredPeople> reqPeopleList = query.getResultList();

    for (RequiredPeople people : reqPeopleList) {
      if (people instanceof ReqAgent) {
        ReqAgent reqAgent = (ReqAgent) people;
        if (reqAgent.getTheAgent() != null) toReturn.add(reqAgent.getTheAgent().getEmail());
      } else if (people instanceof ReqWorkGroup) {
        ReqWorkGroup ReqWorkGroup = (ReqWorkGroup) people;

        Collection<Agent> agents = (Collection<Agent>) ReqWorkGroup.getTheWorkGroup().getTheAgents();

        for (Agent agent : agents) {
          toReturn.add(agent.getEmail());
        }
      }
    }

    getPersistenceContext().getTransaction().commit();
    getPersistenceContext().close();
    return toReturn;
  }
}
