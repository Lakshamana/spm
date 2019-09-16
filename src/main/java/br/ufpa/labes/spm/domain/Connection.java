package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A Connection. */
@Entity
@Table(name = "connection")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy = InheritanceType.JOINED)
public class Connection implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ident")
  private String ident;

  @OneToOne
  @JoinColumn(unique = true)
  private MultipleCon theMultipleConSub;

  @OneToOne
  @JoinColumn(unique = true)
  private SimpleCon theSimpleConSub;

  @OneToOne
  @JoinColumn(unique = true)
  private ArtifactCon theArtifactConSub;

  @ManyToOne
  @JsonIgnoreProperties("theConnections")
  private ProcessModel theProcessModel;

  @ManyToOne
  @JsonIgnoreProperties("theConnections")
  private ConnectionType theConnectionType;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdent() {
    return ident;
  }

  public Connection ident(String ident) {
    this.ident = ident;
    return this;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public MultipleCon getTheMultipleConSub() {
    return theMultipleConSub;
  }

  public Connection theMultipleConSub(MultipleCon multipleCon) {
    this.theMultipleConSub = multipleCon;
    return this;
  }

  public void setTheMultipleConSub(MultipleCon multipleCon) {
    this.theMultipleConSub = multipleCon;
  }

  public SimpleCon getTheSimpleConSub() {
    return theSimpleConSub;
  }

  public Connection theSimpleConSub(SimpleCon simpleCon) {
    this.theSimpleConSub = simpleCon;
    return this;
  }

  public void setTheSimpleConSub(SimpleCon simpleCon) {
    this.theSimpleConSub = simpleCon;
  }

  public ArtifactCon getTheArtifactConSub() {
    return theArtifactConSub;
  }

  public Connection theArtifactConSub(ArtifactCon artifactCon) {
    this.theArtifactConSub = artifactCon;
    return this;
  }

  public void setTheArtifactConSub(ArtifactCon artifactCon) {
    this.theArtifactConSub = artifactCon;
  }

  public ProcessModel getTheProcessModel() {
    return theProcessModel;
  }

  public Connection theProcessModel(ProcessModel processModel) {
    this.theProcessModel = processModel;
    return this;
  }

  public void setTheProcessModel(ProcessModel processModel) {
    this.theProcessModel = processModel;
  }

  public ConnectionType getTheConnectionType() {
    return theConnectionType;
  }

  public Connection theConnectionType(ConnectionType connectionType) {
    this.theConnectionType = connectionType;
    return this;
  }

  public void setTheConnectionType(ConnectionType connectionType) {
    this.theConnectionType = connectionType;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Connection)) {
      return false;
    }
    return id != null && id.equals(((Connection) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Connection{" + "id=" + getId() + ", ident='" + getIdent() + "'" + "}";
  }
}
