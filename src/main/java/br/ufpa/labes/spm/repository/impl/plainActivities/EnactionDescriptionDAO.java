package br.ufpa.labes.spm.repository.impl.plainActivities;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IEnactionDescriptionDAO;
import br.ufpa.labes.spm.domain.EnactionDescription;

@Stateless
public class EnactionDescriptionDAO extends BaseDAO<EnactionDescription, Integer> implements IEnactionDescriptionDAO{

	protected EnactionDescriptionDAO(Class<EnactionDescription> businessClass) {
		super(businessClass);
	}

	public EnactionDescriptionDAO() {
		super(EnactionDescription.class);
	}

}
