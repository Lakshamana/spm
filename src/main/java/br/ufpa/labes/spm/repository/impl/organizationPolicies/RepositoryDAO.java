package br.ufpa.labes.spm.repository.impl.organizationPolicies;

import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IRepositoryDAO;
import br.ufpa.labes.spm.domain.Repository;
import br.ufpa.labes.spm.domain.Structure;

public class RepositoryDAO extends BaseDAO<Repository, String> implements IRepositoryDAO {
	protected RepositoryDAO(Class<Repository> businessClass) {
		super(businessClass);
	}

	public RepositoryDAO() {
		super(Repository.class);
	}

	@Override
	public Structure getTheStructure(String ident) {
		String hql = " select DISTINCT repository.theStructure from " + Repository.class.getName() +
				" as repository where repository.ident = :ident";

		Query query = this.getPersistenceContext().createQuery( hql );
		query.setParameter("ident", ident);
		if ((!query.getResultList().isEmpty()) && (query.getResultList() != null))
			return (Structure) query.getSingleResult();
		else return null;
	}
}
