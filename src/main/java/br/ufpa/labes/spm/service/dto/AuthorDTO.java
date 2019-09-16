package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Author} entity. */
public class AuthorDTO implements Serializable {

  private Long id;

  private String uid;

  private Integer version;

  private String name;

  private String email;

  private String interests;

  private String city;

  private String country;

  private String photoURL;

  private Long userId;

  private Long theOrganizationSubId;

  private Long thePersonSubId;

  private Set<AuthorDTO> authorsFolloweds = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getInterests() {
    return interests;
  }

  public void setInterests(String interests) {
    this.interests = interests;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPhotoURL() {
    return photoURL;
  }

  public void setPhotoURL(String photoURL) {
    this.photoURL = photoURL;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getTheOrganizationSubId() {
    return theOrganizationSubId;
  }

  public void setTheOrganizationSubId(Long organizationId) {
    this.theOrganizationSubId = organizationId;
  }

  public Long getThePersonSubId() {
    return thePersonSubId;
  }

  public void setThePersonSubId(Long personId) {
    this.thePersonSubId = personId;
  }

  public Set<AuthorDTO> getAuthorsFolloweds() {
    return authorsFolloweds;
  }

  public void setAuthorsFolloweds(Set<AuthorDTO> authors) {
    this.authorsFolloweds = authors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AuthorDTO authorDTO = (AuthorDTO) o;
    if (authorDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), authorDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "AuthorDTO{"
        + "id="
        + getId()
        + ", uid='"
        + getUid()
        + "'"
        + ", version="
        + getVersion()
        + ", name='"
        + getName()
        + "'"
        + ", email='"
        + getEmail()
        + "'"
        + ", interests='"
        + getInterests()
        + "'"
        + ", city='"
        + getCity()
        + "'"
        + ", country='"
        + getCountry()
        + "'"
        + ", photoURL='"
        + getPhotoURL()
        + "'"
        + ", user="
        + getUserId()
        + ", theOrganizationSub="
        + getTheOrganizationSubId()
        + ", thePersonSub="
        + getThePersonSubId()
        + "}";
  }
}
