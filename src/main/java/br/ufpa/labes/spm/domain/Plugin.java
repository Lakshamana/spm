package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A Plugin. */
@Entity
@Table(name = "plugin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Plugin implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "developer_name", nullable = false)
  private String developerName;

  @NotNull
  @Column(name = "json_config_file", nullable = false)
  private String jsonConfigFile;

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JoinTable(
      name = "plugin_user",
      joinColumns = @JoinColumn(name = "plugin_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  private Set<User> users = new HashSet<>();

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

  public String getJsonConfigFile() {
    return jsonConfigFile;
  }

  public Plugin jsonConfigFile(String jsonConfigFile) {
    this.jsonConfigFile = jsonConfigFile;
    return this;
  }

  public void setJsonConfigFile(String jsonConfigFile) {
    this.jsonConfigFile = jsonConfigFile;
  }

  public Set<User> getUsers() {
    return users;
  }

  public Plugin users(Set<User> users) {
    this.users = users;
    return this;
  }

  public Plugin addUser(User user) {
    this.users.add(user);
    return this;
  }

  public Plugin removeUser(User user) {
    this.users.remove(user);
    return this;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
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
        + ", jsonConfigFile='"
        + getJsonConfigFile()
        + "'"
        + "}";
  }
}
