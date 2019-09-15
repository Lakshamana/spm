package br.ufpa.labes.spm.repository.impl.plainActivities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.INormalDAO;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.WorkGroup;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.InvolvedArtifact;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.domain.ReqWorkGroup;
import br.ufpa.labes.spm.domain.RequiredPeople;
import br.ufpa.labes.spm.domain.RequiredResource;

public class NormalDAO extends BaseDAO<Normal, Integer> implements INormalDAO {

	protected NormalDAO(Class<Normal> businessClass) {
		super(businessClass);
	}

	public NormalDAO() {
		super(Normal.class);
	}

	public String[] getInvolvedAgentsForNormal(String normalIdent) {

		getPersistenceContext().getTransaction().begin();

		String hql = " from " + RequiredPeople.class.getName() + " as reqPeople where (reqPeople.theNormal.ident=:normalID) ";

		Query query = getPersistenceContext().createQuery(hql);

		query.setParameter("normalID", normalIdent);

		List result = query.getResultList();
		Iterator<RequiredPeople> resultIterator = result.iterator();

		ArrayList<String> agents = new ArrayList<String>();

		while (resultIterator.hasNext()) {
			RequiredPeople reqPeople = resultIterator.next();

			if (reqPeople instanceof ReqAgent) {
				ReqAgent reqAgent = (ReqAgent) reqPeople;
				Agent agent = reqAgent.getTheAgent();

				if (agent != null)
					agents.add(reqAgent.getTheAgent().getName());
			} else if (reqPeople instanceof ReqWorkGroup) {
				ReqWorkGroup ReqWorkGroup = (ReqWorkGroup) reqPeople;
				WorkGroup group = ReqWorkGroup.getTheGroup();

				if (group != null)
					agents.add(ReqWorkGroup.getTheGroup().getName() + " (WorkGroup)");
			}
		}

		String[] agentsArray = new String[agents.size()];
		agentsArray = agents.toArray(agentsArray);

		getPersistenceContext().getTransaction().commit();

		return agentsArray;

	}

	public String[] getRequiredResourcesForNormal(String normalIdent) {

		getPersistenceContext().getTransaction().begin();

		String hql = "select reqResource.theResource.ident from " + RequiredResource.class.getName()
				+ " as reqResource where (reqResource.theNormal.ident=:normalID) ";

		Query query = getPersistenceContext().createQuery(hql);

		query.setParameter("normalID", normalIdent);

		List result = query.getResultList();

		getPersistenceContext().getTransaction().commit();

		return (String[]) result.toArray(new String[result.size()]);

	}

	public String[] getInputArtifactsIdentsForNormal(String normalIdent) {
		getPersistenceContext().getTransaction().begin();

		String hql = " from " + InvolvedArtifact.class.getName()
				+ " as involvedArtifact where (involvedArtifact.inInvolvedArtifacts.ident=:normalID) ";

		Query query = getPersistenceContext().createQuery(hql);

		query.setParameter("normalID", normalIdent);

		List result = query.getResultList();

		Iterator<InvolvedArtifact> resultIterator = result.iterator();

		ArrayList<String> inputArtifacts = new ArrayList<String>();

		while (resultIterator.hasNext()) {
			InvolvedArtifact involvedArtifact = resultIterator.next();
			Artifact theArtifact = involvedArtifact.getTheArtifact();

			if (theArtifact != null)
				inputArtifacts.add(theArtifact.getIdent());
		}

		String[] inputArtifactsArray = new String[inputArtifacts.size()];
		inputArtifactsArray = inputArtifacts.toArray(inputArtifactsArray);

		getPersistenceContext().getTransaction().commit();

		return inputArtifactsArray;

	}

	public Artifact[] getOutputArtifactsForNormal(String normalIdent) {

		getPersistenceContext().getTransaction().begin();

		String hql = " from " + InvolvedArtifact.class.getName()
				+ " as involvedArtifact where (involvedArtifact.outInvolvedArtifacts.ident=:normalID) ";

		Query query = getPersistenceContext().createQuery(hql);

		query.setParameter("normalID", normalIdent);

		List result = query.getResultList();
		Iterator<InvolvedArtifact> resultIterator = result.iterator();

		ArrayList<Artifact> outputArtifacts = new ArrayList<Artifact>();

		while (resultIterator.hasNext()) {
			InvolvedArtifact involvedArtifact = resultIterator.next();
			if (involvedArtifact.getTheArtifact() != null) {
				outputArtifacts.add(involvedArtifact.getTheArtifact());
			}
		}

		Artifact[] outputArtifactsArray = new Artifact[outputArtifacts.size()];
		outputArtifactsArray = outputArtifacts.toArray(outputArtifactsArray);

		getPersistenceContext().getTransaction().commit();

		return outputArtifactsArray;
	}

	public String[] getOutputArtifactsIdentsForNormal(String normalIdent) {

		getPersistenceContext().getTransaction().begin();

		String hql = " from " + InvolvedArtifact.class.getName()
				+ " as involvedArtifact where (involvedArtifact.outInvolvedArtifacts.ident=:normalID) ";

		Query query = getPersistenceContext().createQuery(hql);

		query.setParameter("normalID", normalIdent);

		List result = query.getResultList();

		Iterator<InvolvedArtifact> resultIterator = result.iterator();

		ArrayList<String> outputArtifacts = new ArrayList<String>();

		while (resultIterator.hasNext()) {
			InvolvedArtifact involvedArtifact = resultIterator.next();
			if (involvedArtifact.getTheArtifact() != null) {
				outputArtifacts.add(involvedArtifact.getTheArtifact().getIdent());
			}
		}

		String[] outputArtifactsArray = new String[outputArtifacts.size()];
		outputArtifactsArray = outputArtifacts.toArray(outputArtifactsArray);

		getPersistenceContext().getTransaction().commit();

		return outputArtifactsArray;

	}

}
