package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class AgentsDTO implements Serializable {
  private String[] nameAgents;
  private List<String> agentIdents;
  private String[] cargoAgents;
  // private String[] superType;
  private List<AgentDTO> agents = new ArrayList<AgentDTO>();

  public AgentsDTO() {
    this.setNameAgents(new String[0]);
    this.setCargoAgents(new String[0]);
  }

  public AgentsDTO(List<AgentDTO> agents) {
    this.agents = agents;
  }

  public boolean isEmpty() {
    if (agents.size() == 0) return true;
    else return false;
  }

  public void add(AgentDTO agentDTO) {
    this.agents.add(agentDTO);
  }

  public String[] getNameAgents() {
    return nameAgents;
  }

  public void setNameAgents(String[] nameAgents) {
    this.nameAgents = nameAgents;
  }

  public List<String> getAgentIdents() {
    return agentIdents;
  }

  public void setAgentIdents(List<String> agentIdents) {
    this.agentIdents = agentIdents;
  }

  public String[] getCargoAgents() {
    return cargoAgents;
  }

  public void setCargoAgents(String[] cargoAgents) {
    this.cargoAgents = cargoAgents;
  }

  public List<AgentDTO> getAgents() {
    return agents;
  }

  public void setAgents(List<AgentDTO> agents) {
    this.agents = agents;
  }

  public AgentDTO getAgentDTO(int index) {
    return this.agents.get(index);
  }

  @Override
  public String toString() {
    return "AgentsDTO [agents=" + agents + "]";
  }
}
