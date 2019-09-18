package br.ufpa.labes.spm.service.interfaces;


import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.service.dto.ConfigurationDTO;

@Remote
public interface ConfigurationServices {

	public boolean perfilSave(ConfigurationDTO confi,AgentDTO agent);

	public ConfigurationDTO getPerfil(Integer oid);

	public ConfigurationDTO updateConfiguration(Integer agentOid, ConfigurationDTO configuration);
}
