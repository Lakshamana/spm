package org.qrconsult.spm.services.interfaces;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.formAgent.AgentDTO;
import org.qrconsult.spm.dtos.formAgent.ConfigurationDTO;

@Remote
public interface ConfigurationServices {

	public boolean perfilSave(ConfigurationDTO confi,AgentDTO agent);

	public ConfigurationDTO getPerfil(Integer oid);
	
	public ConfigurationDTO updateConfiguration(Integer agentOid, ConfigurationDTO configuration);
}
