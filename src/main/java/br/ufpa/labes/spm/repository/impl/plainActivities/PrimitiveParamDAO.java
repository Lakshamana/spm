package br.ufpa.labes.spm.repository.impl.plainActivities;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IPrimitiveParamDAO;
import br.ufpa.labes.spm.domain.PrimitiveParam;

@Stateless
public class PrimitiveParamDAO extends BaseDAO<PrimitiveParam, Integer> implements IPrimitiveParamDAO{

	protected PrimitiveParamDAO(Class<PrimitiveParam> businessClass) {
		super(businessClass);
	}

	public PrimitiveParamDAO() {
		super(PrimitiveParam.class);
	}

}
