package br.ufpa.labes.spm.repository.impl.resources;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.resources.IConsumableDAO;
import br.ufpa.labes.spm.domain.Consumable;

@Stateless
public class ConsumableDAO extends BaseDAO<Consumable, String> implements IConsumableDAO{

	protected ConsumableDAO(Class<Consumable> businessClass) {
		super(businessClass);
	}

	public ConsumableDAO() {
		super(Consumable.class);
	}


}
