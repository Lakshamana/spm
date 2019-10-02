package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.VCSRepository} entity. */
public class VCSRepositoryDTO implements Serializable {

  private Long id;

  private String ident;

  private String controlVersionSystem;

  private String server;

  private String port;

  private String connectionMethod;

  private String repository;

  private String username;

  private String password;

  private Boolean defaultUser;

  private Long theStructureId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdent() {
    return ident;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public String getControlVersionSystem() {
    return controlVersionSystem;
  }

  public void setControlVersionSystem(String controlVersionSystem) {
    this.controlVersionSystem = controlVersionSystem;
  }

  public String getServer() {
    return server;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getConnectionMethod() {
    return connectionMethod;
  }

  public void setConnectionMethod(String connectionMethod) {
    this.connectionMethod = connectionMethod;
  }

  public String getRepository() {
    return repository;
  }

  public void setRepository(String repository) {
    this.repository = repository;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean isDefaultUser() {
    return defaultUser;
  }

  public void setDefaultUser(Boolean defaultUser) {
    this.defaultUser = defaultUser;
  }

  public Long getTheStructureId() {
    return theStructureId;
  }

  public void setTheStructureId(Long structureId) {
    this.theStructureId = structureId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VCSRepositoryDTO vCSRepositoryDTO = (VCSRepositoryDTO) o;
    if (vCSRepositoryDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), vCSRepositoryDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "VCSRepositoryDTO{"
        + "id="
        + getId()
        + ", ident='"
        + getIdent()
        + "'"
        + ", controlVersionSystem='"
        + getControlVersionSystem()
        + "'"
        + ", server='"
        + getServer()
        + "'"
        + ", port='"
        + getPort()
        + "'"
        + ", connectionMethod='"
        + getConnectionMethod()
        + "'"
        + ", repository='"
        + getRepository()
        + "'"
        + ", username='"
        + getUsername()
        + "'"
        + ", password='"
        + getPassword()
        + "'"
        + ", defaultUser='"
        + isDefaultUser()
        + "'"
        + ", theStructure="
        + getTheStructureId()
        + "}";
  }
}
