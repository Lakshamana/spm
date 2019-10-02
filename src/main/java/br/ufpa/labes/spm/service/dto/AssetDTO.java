package br.ufpa.labes.spm.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Asset} entity. */
public class AssetDTO implements Serializable {

  private Long id;

  private String uid;

  private LocalDate creationDate;

  private LocalDate publishDate;

  private String name;

  private String description;

  private String applicability;

  private String tags;

  private String path;

  private String latestVersion;

  private Boolean readOnly;

  private Long statsId;

  private Long ownerId;

  private Set<AuthorDTO> favoritedBies = new HashSet<>();

  private Set<AuthorDTO> followers = new HashSet<>();

  private Set<AuthorDTO> collaborators = new HashSet<>();

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

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public LocalDate getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(LocalDate publishDate) {
    this.publishDate = publishDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getApplicability() {
    return applicability;
  }

  public void setApplicability(String applicability) {
    this.applicability = applicability;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getLatestVersion() {
    return latestVersion;
  }

  public void setLatestVersion(String latestVersion) {
    this.latestVersion = latestVersion;
  }

  public Boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(Boolean readOnly) {
    this.readOnly = readOnly;
  }

  public Long getStatsId() {
    return statsId;
  }

  public void setStatsId(Long assetStatId) {
    this.statsId = assetStatId;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long authorId) {
    this.ownerId = authorId;
  }

  public Set<AuthorDTO> getFavoritedBies() {
    return favoritedBies;
  }

  public void setFavoritedBies(Set<AuthorDTO> authors) {
    this.favoritedBies = authors;
  }

  public Set<AuthorDTO> getFollowers() {
    return followers;
  }

  public void setFollowers(Set<AuthorDTO> authors) {
    this.followers = authors;
  }

  public Set<AuthorDTO> getCollaborators() {
    return collaborators;
  }

  public void setCollaborators(Set<AuthorDTO> authors) {
    this.collaborators = authors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AssetDTO assetDTO = (AssetDTO) o;
    if (assetDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), assetDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "AssetDTO{"
        + "id="
        + getId()
        + ", uid='"
        + getUid()
        + "'"
        + ", creationDate='"
        + getCreationDate()
        + "'"
        + ", publishDate='"
        + getPublishDate()
        + "'"
        + ", name='"
        + getName()
        + "'"
        + ", description='"
        + getDescription()
        + "'"
        + ", applicability='"
        + getApplicability()
        + "'"
        + ", tags='"
        + getTags()
        + "'"
        + ", path='"
        + getPath()
        + "'"
        + ", latestVersion='"
        + getLatestVersion()
        + "'"
        + ", readOnly='"
        + isReadOnly()
        + "'"
        + ", stats="
        + getStatsId()
        + ", owner="
        + getOwnerId()
        + "}";
  }
}
