package br.ufpa.labes.spm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A ToolType. */
@Entity
@Table(name = "tool_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ToolType extends Type implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "theToolType")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ToolDefinition> theToolDefinitions = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<ToolDefinition> getTheToolDefinitions() {
    return theToolDefinitions;
  }

  public ToolType theToolDefinitions(Set<ToolDefinition> toolDefinitions) {
    this.theToolDefinitions = toolDefinitions;
    return this;
  }

  public ToolType addTheToolDefinition(ToolDefinition toolDefinition) {
    this.theToolDefinitions.add(toolDefinition);
    toolDefinition.setTheToolType(this);
    return this;
  }

  public ToolType removeTheToolDefinition(ToolDefinition toolDefinition) {
    this.theToolDefinitions.remove(toolDefinition);
    toolDefinition.setTheToolType(null);
    return this;
  }

  public void setTheToolDefinitions(Set<ToolDefinition> toolDefinitions) {
    this.theToolDefinitions = toolDefinitions;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ToolType)) {
      return false;
    }
    return id != null && id.equals(((ToolType) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "ToolType{" + "id=" + getId() + "}";
  }
}
