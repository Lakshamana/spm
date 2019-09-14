package br.ufpa.labes.spm.repository.impl.plainActivities;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IReqGroupDAO;
import br.ufpa.labes.spm.domain.ReqGroup;

@Stateless
public class ReqGroupDAO extends BaseDAO<ReqGroup, Integer> implements IReqGroupDAO {

	protected ReqGroupDAO(Class<ReqGroup> businessClass) {
		super(businessClass);
	}

	public ReqGroupDAO() {
		super(ReqGroup.class);
	}

	public ReqGroup findReqGroupFromProcessModel(String groupIdent, String groupTypeIdent, String normalIdent) {
		List<ReqGroup> retorno = null;

		if (groupIdent != null && !groupIdent.equals("")) {

			String hql = "SELECT reqGroup FROM " + ReqGroup.class.getName()
					+ " AS reqGroup WHERE reqGroup.theGroup.ident=:groupIdent AND reqGroup.theGroupType.ident=:groupTypeIdent AND reqGroup.theNormal.ident=:normalIdent";
			Query query = getPersistenceContext().createQuery(hql);
			query.setParameter("groupIdent", groupIdent);
			query.setParameter("groupTypeIdent", groupTypeIdent);
			query.setParameter("normalIdent", normalIdent);

			retorno = (List<ReqGroup>) query.getResultList();
		} else {

			String hql = "SELECT reqGroup FROM " + ReqGroup.class.getName()
					+ " AS reqGroup WHERE reqGroup.theGroupType.ident=:groupTypeIdent AND reqGroup.theNormal.ident=:normalIdent";
			Query query = getPersistenceContext().createQuery(hql);
			query.setParameter("groupTypeIdent", groupTypeIdent);
			query.setParameter("normalIdent", normalIdent);

			retorno = (List<ReqGroup>) query.getResultList();

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
