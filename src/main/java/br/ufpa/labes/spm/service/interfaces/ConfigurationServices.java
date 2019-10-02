package br.ufpa.labes.spm.service.interfaces;

import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.service.dto.SpmConfigurationDTO;

public interface ConfigurationServices {

  public boolean perfilSave(SpmConfigurationDTO confi, AgentDTO agent);

  public SpmConfigurationDTO getPerfil(Long oid);

  public SpmConfigurationDTO updateConfiguration(
      Integer agentOid, SpmConfigurationDTO spmconfiguration);
}
