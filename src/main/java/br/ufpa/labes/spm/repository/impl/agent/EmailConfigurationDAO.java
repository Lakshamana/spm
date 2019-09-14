package br.ufpa.labes.spm.repository.impl.agent;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IEmailConfigurationDAO;
import br.ufpa.labes.spm.domain.EmailConfiguration;

public class EmailConfigurationDAO  extends BaseDAO<EmailConfiguration, Integer> implements IEmailConfigurationDAO{

	protected EmailConfigurationDAO(Class<EmailConfiguration> businessClass) {
		super(businessClass);
	}

	public EmailConfigurationDAO() {
		super(EmailConfiguration.class);
	}

}
