package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.AuthorStat} entity. */
public class AuthorStatDTO implements Serializable {

  private Long id;

  private Double rate;

  private Long visitCount;

  private Long downloadCount;

  private Long authorId;

  private Long theAssetId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getRate() {
    return rate;
  }

  public void setRate(Double rate) {
    this.rate = rate;
  }

  public Long getVisitCount() {
    return visitCount;
  }

  public void setVisitCount(Long visitCount) {
    this.visitCount = visitCount;
  }

  public Long getDownloadCount() {
    return downloadCount;
  }

  public void setDownloadCount(Long downloadCount) {
    this.downloadCount = downloadCount;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  public Long getTheAssetId() {
    return theAssetId;
  }

  public void setTheAssetId(Long assetId) {
    this.theAssetId = assetId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AuthorStatDTO authorStatDTO = (AuthorStatDTO) o;
    if (authorStatDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), authorStatDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "AuthorStatDTO{"
        + "id="
        + getId()
        + ", rate="
        + getRate()
        + ", visitCount="
        + getVisitCount()
        + ", downloadCount="
        + getDownloadCount()
        + ", author="
        + getAuthorId()
        + ", theAsset="
        + getTheAssetId()
        + "}";
  }
}
