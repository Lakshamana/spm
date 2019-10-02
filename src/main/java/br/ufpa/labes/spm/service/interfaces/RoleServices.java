package br.ufpa.labes.spm.service.interfaces;

import java.util.List;

import br.ufpa.labes.spm.service.dto.AbilityDTO;
import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.service.dto.RoleDTO;
import br.ufpa.labes.spm.service.dto.RolesDTO;

public interface RoleServices {
  public RoleDTO getRole(String nameRole);

  public RoleDTO saveRole(RoleDTO roleDTO);

  public RoleDTO saveAbilityToRole(RoleDTO roleDTO);

  public Boolean removeRole(RoleDTO roleDTO);

  public RolesDTO getRoles(String termoBusca, String domainFilter);

  public String[] getRoleTypes();

  public List<AbilityDTO> getAbilitysToRole();

  public RolesDTO getRoles();

  public List<AgentDTO> getAgentToRole();

  public RoleDTO saveAgentToRole(RoleDTO roleDTO);

  public Boolean removeAbilityRole(RoleDTO roleDTO);

  public Boolean removeAgentRole(RoleDTO roleDTO);

  public List<RoleDTO> getRolesToTree();

  public String getPerfil(RoleDTO agent, int oid);
}
