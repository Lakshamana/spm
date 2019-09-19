package br.ufpa.labes.spm.repository.impl.organizationPolicies;

import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.ICompanyDAO;
import br.ufpa.labes.spm.domain.Company;

public class CompanyDAO extends BaseDAO<Company, Long> implements ICompanyDAO {

  protected CompanyDAO(Class<Company> businessClass) {
    super(businessClass);
  }

  public CompanyDAO() {
    super(Company.class);
  }

  @Override
  public boolean alreadyExistCNPJ(String cnpj, String ident) {
    String hql =
        "SELECT company FROM " + Company.class.getName() + " AS company WHERE company.cnpj = :cnpj";
    Query query = getPersistenceContext().createQuery(hql);
    query.setParameter("cnpj", cnpj);

    Company company = null;
    if (!query.getResultList().isEmpty()) company = (Company) query.getResultList().get(0);

    if (company == null) return false;
    else if (company.getIdent().equals(ident)) { // PARA O CASO DE SER AÇÃO DE ATUALIZAR A COMPANIA
      return false;
    } else return true;
  }
}
