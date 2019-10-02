package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

import br.ufpa.labes.spm.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class AgentHasAbilityDTO implements Serializable {

  private Long id;

  private Integer degree;
  @IgnoreMapping private String theAgent;
  @IgnoreMapping private String theAbility;
  @IgnoreMapping private String descriptionTheAbility;

  public AgentHasAbilityDTO() {}

  public AgentHasAbilityDTO(Integer degree, String theAgent, String theAbility) {
    this.degree = degree;
    this.theAgent = theAgent;
    this.theAbility = theAbility;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getDegree() {
    return degree;
  }

  public void setDegree(Integer degree) {
    this.degree = degree;
  }

  public String getTheAgent() {
    return theAgent;
  }

  public void setTheAgent(String theAgent) {
    this.theAgent = theAgent;
  }

  public String getTheAbility() {
    return theAbility;
  }

  public void setTheAbility(String theAbility) {
    this.theAbility = theAbility;
  }

  public String getDescriptionTheAbility() {
    return descriptionTheAbility;
  }

  public void setDescriptionTheAbility(String descriptionTheAbility) {
    this.descriptionTheAbility = descriptionTheAbility;
  }
}
