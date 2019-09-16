package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A ArtifactParam. */
@Entity
@Table(name = "artifact_param")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArtifactParam extends Parameter implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(mappedBy = "theArtifactParameterSub")
  @JsonIgnore
  private Parameter theParameterSuper;

  @ManyToOne
  @JsonIgnoreProperties("theArtifactParams")
  private Artifact theArtifact;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Parameter getTheParameterSuper() {
    return theParameterSuper;
  }

  public ArtifactParam theParameterSuper(Parameter parameter) {
    this.theParameterSuper = parameter;
    return this;
  }

  public void setTheParameterSuper(Parameter parameter) {
    this.theParameterSuper = parameter;
  }

  public Artifact getTheArtifact() {
    return theArtifact;
  }

  public ArtifactParam theArtifact(Artifact artifact) {
    this.theArtifact = artifact;
    return this;
  }

  public void setTheArtifact(Artifact artifact) {
    this.theArtifact = artifact;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArtifactParam)) {
      return false;
    }
    return id != null && id.equals(((ArtifactParam) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "ArtifactParam{" + "id=" + getId() + "}";
  }
}
