package br.ufpa.labes.spm.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import br.ufpa.labes.spm.repository.interfaces.IArtifactManagementDAO;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.ArtifactCon;
import br.ufpa.labes.spm.domain.Task;

public class ArtifactManagementDAO implements IArtifactManagementDAO {

	@PersistenceContext(unitName = "SPMPU")
	private EntityManager em;

	@Override
	public EntityManager getPersistenceContext() {
		return em;
	}

	@Override
	public Task getAgentTask(String agentIdent, String normalIdent) {
		try	{
			String hql = " from " + Task.class.getName() + " as task where task.theNormal.ident = :normalIdent and task.theProcessAgenda.theTaskAgenda.theAgent.ident = :agentIdent";

			Query query = this.getPersistenceContext().createQuery( hql );

			query.setParameter( "agentIdent", agentIdent );
			query.setParameter( "normalIdent", normalIdent );

			Task task = (Task)query.getSingleResult();

			return task;
		}
		catch (HibernateException e){
			e.printStackTrace();
		}
		catch ( Exception e ){
			e.printStackTrace();
		}
		return null;
	}

	public Object[] getArtifactsIdentsFromProcessModelWithoutTemplates( String ident ){
		String hql = " select DISTINCT theArtifact.ident from " + ArtifactCon.class.getName() +
		" as artfCon where (artfCon.ident like '"+ident+".%')"+" and (artfCon.theArtifact.isTemplate = 'false')";

		Query query = this.getPersistenceContext().createQuery( hql );

		List result = query.getResultList();
		return result.toArray(new Object[result.size()]);
	}

	public Artifact[] getArtifactsFromProcessModelWithPathNotEmpty( String ident ){
		String hql = " select DISTINCT theArtifact from " + ArtifactCon.class.getName() +
		" as artfCon where (artfCon.ident like '"+ident+".%')"+" and (artfCon.theArtifact.pathName is not '')";

		Query query = this.getPersistenceContext().createQuery( hql );

		List result = query.getResultList();
		Artifact[] artifacts = new Artifact[result.size()];
		result.toArray(artifacts);
		return artifacts;
	}

}
