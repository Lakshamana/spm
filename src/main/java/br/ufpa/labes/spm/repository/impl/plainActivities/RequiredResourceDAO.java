package br.ufpa.labes.spm.repository.impl.plainActivities;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IRequiredResourceDAO;
import br.ufpa.labes.spm.domain.RequiredResource;

@Stateless
public class RequiredResourceDAO extends BaseDAO<RequiredResource, Integer> implements IRequiredResourceDAO {

	protected RequiredResourceDAO(Class<RequiredResource> businessClass) {
		super(businessClass);
	}

	public RequiredResourceDAO() {
		super(RequiredResource.class);
	}

	public RequiredResource findRequiredResourceFromProcessModel(String resourceIdent, String resourceTypeIdent, String normalIdent) {
		List<RequiredResource> retorno = null;

		if (resourceIdent != null && !resourceIdent.equals("")) {

			String hql = "SELECT reqResource FROM "
					+ RequiredResource.class.getName()
					+ " AS reqResource WHERE reqResource.theResource.ident=:resourceIdent AND reqResource.theResourceType.ident=:resourceTypeIdent AND reqResource.theNormal.ident=:normalIdent";
			Query query = getPersistenceContext().createQuery(hql);
			query.setParameter("resourceIdent", resourceIdent);
			query.setParameter("resourceTypeIdent", resourceTypeIdent);
			query.setParameter("normalIdent", normalIdent);

			retorno = (List<RequiredResource>) query.getResultList();
			System.out.println("findRequiredResourceFromProcessModel if");
			System.out.println(retorno);
		} else {

			String hql = "SELECT reqResource FROM " + RequiredResource.class.getName()
					+ " AS reqResource WHERE reqResource.theResourceType.ident=:resourceTypeIdent AND reqResource.theNormal.ident=:normalIdent";
			Query query = getPersistenceContext().createQuery(hql);
			query.setParameter("resourceTypeIdent", resourceTypeIdent);
			query.setParameter("normalIdent", normalIdent);

			retorno = (List<RequiredResource>) query.getResultList();
			System.out.println("findRequiredResourceFromProcessModel else");
			System.out.println(retorno);
		}

		if (retorno != null) {
			if (!retorno.isEmpty()) {
				return retorno.get(0);
			} else {
				return null;
			}
		} else
			return null;
	}

}
