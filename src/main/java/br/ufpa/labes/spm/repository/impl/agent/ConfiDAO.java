package br.ufpa.labes.spm.repository.impl.agent;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IConfiDAO;
import br.ufpa.labes.spm.domain.Configuration;

public class ConfiDAO extends BaseDAO<Configuration, String> implements IConfiDAO {

	protected ConfiDAO(Class<Configuration> businessClass) {
		super(businessClass);
	}

	public ConfiDAO() {
		super(Configuration.class);
	}

	}
