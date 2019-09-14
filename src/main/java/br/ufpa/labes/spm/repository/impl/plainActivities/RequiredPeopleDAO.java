package br.ufpa.labes.spm.repository.impl.plainActivities;

import java.util.Collection;
import java.util.HashSet;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IRequiredPeopleDAO;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.domain.ReqGroup;
import br.ufpa.labes.spm.domain.RequiredPeople;

@Stateless
public class RequiredPeopleDAO extends BaseDAO<RequiredPeople, Integer> implements IRequiredPeopleDAO{

	protected RequiredPeopleDAO(Class<RequiredPeople> businessClass) {
		super(businessClass);
	}

	public RequiredPeopleDAO() {
		super(RequiredPeople.class);
	}

	public Collection<String> getReqPeopleEmails( String normalIdent ) {
			getPersistenceContext().getTransaction().begin();

			String hql = "from " + RequiredPeople.class.getName() + " as reqPeople where reqPeople.theNormal.ident = :normalIdent";

			Query query = getPersistenceContext().createQuery( hql );
			query.setParameter( "normalIdent", normalIdent );

			Collection<String> toReturn = new HashSet<String>();

			Collection<RequiredPeople> reqPeopleList = query.getResultList();

			for ( RequiredPeople people : reqPeopleList ) {
				if ( people instanceof ReqAgent ) {
					ReqAgent reqAgent = (ReqAgent)people;
					if(reqAgent.getTheAgent()!=null)
						toReturn.add( reqAgent.getTheAgent().getEMail() );
				}
				else if ( people instanceof ReqGroup ) {
					ReqGroup reqGroup = (ReqGroup)people;

					Collection<Agent> agents = (Collection<Agent>) reqGroup.getTheGroup().getTheAgent();

					for ( Agent agent : agents ) {
						toReturn.add( agent.getEMail() );
					}
				}
			}

			getPersistenceContext().getTransaction().commit();
			getPersistenceContext().close();
			return toReturn;
	}


}
