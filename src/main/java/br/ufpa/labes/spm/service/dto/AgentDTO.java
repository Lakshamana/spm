package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.ufpa.labes.spm.annotations.IgnoreMapping;

@SuppressWarnings("serial")
@XmlRootElement(name = "agent")
public class AgentDTO implements Serializable {

  private Long id;

  private String ident;

  private String name;

  private String email;

  private Float costHour;

  private String password;

  private Boolean staticOk;

  private Boolean isActive;

  private String artifactMngLogin;

  private String artifactMngPassword;

  private String description;

  private Boolean online;

  private String upload;

  @IgnoreMapping private List<String> afinityToAgent;
  @IgnoreMapping private List<String> abilityToAgent;
  @IgnoreMapping private List<String> groupToAgent;
  @IgnoreMapping private List<String> roleToAgent;
  @IgnoreMapping private List<String> roleIdentsToAgent;
  @IgnoreMapping private List<TaskDTO> tasks;
  @IgnoreMapping private float workingCost;
  @IgnoreMapping private float estimatedWorkingCost;

  public AgentDTO() {
    this.afinityToAgent = new ArrayList<String>();
    this.abilityToAgent = new ArrayList<String>();
    this.groupToAgent = new ArrayList<String>();
    this.roleToAgent = new ArrayList<String>();
    this.tasks = new ArrayList<TaskDTO>();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean isOnline() {
    return online;
  }

  @XmlTransient
  public Boolean getOnline() {
    return online;
  }

  public void setOnline(Boolean online) {
    this.online = online;
  }

  public String getIdent() {
    return ident;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Float getCostHour() {
    return costHour;
  }

  public void setCostHour(Float costHour) {
    this.costHour = costHour;
  }

  @XmlTransient
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @XmlTransient
  public Boolean isIsActive() {
    return isActive;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  @XmlTransient
  public String getArtifactMngPassword() {
    return artifactMngPassword;
  }

  public void setArtifactMngPassword(String artifactMngPassword) {
    this.artifactMngPassword = artifactMngPassword;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @XmlElement(name = "email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String eMail) {
    this.email = eMail;
  }

  @XmlTransient
  public Boolean isStaticOk() {
    return staticOk;
  }

  public List<String> getAfinityToAgent() {
    return afinityToAgent;
  }

  public void setAfinityToAgent(List<String> afinityToAgent) {
    this.afinityToAgent = afinityToAgent;
  }

  public List<String> getAbilityToAgent() {
    return abilityToAgent;
  }

  public void setAbilityToAgent(List<String> abilityToAgent) {
    this.abilityToAgent = abilityToAgent;
  }

  public List<String> getGroupToAgent() {
    return groupToAgent;
  }

  public void setGroupToAgent(List<String> groupToAgent) {
    this.groupToAgent = groupToAgent;
  }

  public List<String> getRoleToAgent() {
    return roleToAgent;
  }

  public void setRoleToAgent(List<String> roleToAgent) {
    this.roleToAgent = roleToAgent;
  }

  public List<String> getRoleIdentsToAgent() {
    return roleIdentsToAgent;
  }

  public void setRoleIdentsToAgent(List<String> roleIdentsToAgent) {
    this.roleIdentsToAgent = roleIdentsToAgent;
  }

  //	@XmlTransient
  public String getUpload() {
    return upload;
  }

  public void setUpload(String upload) {
    this.upload = upload;
  }

  public List<TaskDTO> getTasks() {
    return tasks;
  }

  public void setTasks(List<TaskDTO> tasks) {
    this.tasks = tasks;
  }

  public float getWorkingCost() {
    return workingCost;
  }

  public void setWorkingCost(float workingCost) {
    this.workingCost = workingCost;
  }

  public float getEstimatedWorkingCost() {
    return estimatedWorkingCost;
  }

  public void setEstimatedWorkingCost(float estimatedWorkingCost) {
    this.estimatedWorkingCost = estimatedWorkingCost;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((ident == null) ? 0 : ident.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    AgentDTO other = (AgentDTO) obj;
    if (ident == null) {
      if (other.ident != null) return false;
    } else if (!ident.equals(other.ident)) return false;
    return true;
  }

  @Override
  public String toString() {
    return "Name: " + this.getName() + "; Email: " + this.getEmail();
  }

  @XmlTransient
  public String getArtifactMngLogin() {
    return artifactMngLogin;
  }

  public void setArtifactMngLogin(String artifactMngLogin) {
    this.artifactMngLogin = artifactMngLogin;
  }
}
