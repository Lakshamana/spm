package br.ufpa.labes.spm.repository.impl.organizationPolicies;

import java.util.List;

import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IProjectDAO;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.Project;

public class ProjectDAO extends BaseDAO<Project, String> implements IProjectDAO{

	protected ProjectDAO(Class<Project> businessClass) {
		super(businessClass);
	}

	public ProjectDAO() {
		super(Project.class);
	}

	@Override
	public List<Project> findAll() {
		String hql = "select project from " + Project.class.getSimpleName() + " as artifact";
		Query query = this.getPersistenceContext().createQuery(hql);
		return query.getResultList();
	}
}
