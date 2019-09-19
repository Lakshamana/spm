package br.ufpa.labes.spm.repository.impl.organizationPolicies;

import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IRepositoryDAO;
import br.ufpa.labes.spm.domain.VCSRepository;
import br.ufpa.labes.spm.domain.Structure;

public class RepositoryDAO extends BaseDAO<VCSRepository, Long> implements IRepositoryDAO {
  protected RepositoryDAO(Class<VCSRepository> businessClass) {
    super(businessClass);
  }

  public RepositoryDAO() {
    super(VCSRepository.class);
  }

  @Override
  public Structure getTheStructure(String ident) {
    String hql =
        " select DISTINCT repository.theStructure from "
            + VCSRepository.class.getName()
            + " as repository where repository.ident = :ident";

    Query query = this.getPersistenceContext().createQuery(hql);
    query.setParameter("ident", ident);
    if ((!query.getResultList().isEmpty()) && (query.getResultList() != null))
      return (Structure) query.getSingleResult();
    else return null;
  }
}
