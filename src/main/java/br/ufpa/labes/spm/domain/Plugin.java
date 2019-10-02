package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/** A Plugin. */
@Entity
@Table(name = "plugin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Plugin implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", unique = true)
  private String name;

  @NotNull
  @Column(name = "developer_name", nullable = false)
  private String developerName;

  @NotNull
  @Column(name = "config_file_path", nullable = false)
  private String configFilePath;

  @ManyToOne
  @JsonIgnoreProperties("thePlugins")
  private User theCompany;

  @OneToOne(mappedBy = "thePlugin")
  @JsonIgnore
  private Driver theDriver;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Plugin name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDeveloperName() {
    return developerName;
  }

  public Plugin developerName(String developerName) {
    this.developerName = developerName;
    return this;
  }

  public void setDeveloperName(String developerName) {
    this.developerName = developerName;
  }

  public String getConfigFilePath() {
    return configFilePath;
  }

  public Plugin configFilePath(String configFilePath) {
    this.configFilePath = configFilePath;
    return this;
  }

  public void setConfigFilePath(String configFilePath) {
    this.configFilePath = configFilePath;
  }

  public User getTheCompany() {
    return theCompany;
  }

  public Plugin theCompany(User user) {
    this.theCompany = user;
    return this;
  }

  public void setTheCompany(User user) {
    this.theCompany = user;
  }

  public Driver getTheDriver() {
    return theDriver;
  }

  public Plugin theDriver(Driver driver) {
    this.theDriver = driver;
    return this;
  }

  public void setTheDriver(Driver driver) {
    this.theDriver = driver;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Plugin)) {
      return false;
    }
    return id != null && id.equals(((Plugin) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Plugin{"
        + "id="
        + getId()
        + ", name='"
        + getName()
        + "'"
        + ", developerName='"
        + getDeveloperName()
        + "'"
        + ", configFilePath='"
        + getConfigFilePath()
        + "'"
        + "}";
  }
}
