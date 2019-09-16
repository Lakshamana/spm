package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A AuthorStat. */
@Entity
@Table(name = "author_stat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AuthorStat implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "rate")
  private Double rate;

  @Column(name = "visit_count")
  private Long visitCount;

  @Column(name = "download_count")
  private Long downloadCount;

  @ManyToOne
  @JsonIgnoreProperties("theAuthorStats")
  private Author author;

  @ManyToOne
  @JsonIgnoreProperties("authorStats")
  private Asset theAsset;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getRate() {
    return rate;
  }

  public AuthorStat rate(Double rate) {
    this.rate = rate;
    return this;
  }

  public void setRate(Double rate) {
    this.rate = rate;
  }

  public Long getVisitCount() {
    return visitCount;
  }

  public AuthorStat visitCount(Long visitCount) {
    this.visitCount = visitCount;
    return this;
  }

  public void setVisitCount(Long visitCount) {
    this.visitCount = visitCount;
  }

  public Long getDownloadCount() {
    return downloadCount;
  }

  public AuthorStat downloadCount(Long downloadCount) {
    this.downloadCount = downloadCount;
    return this;
  }

  public void setDownloadCount(Long downloadCount) {
    this.downloadCount = downloadCount;
  }

  public Author getAuthor() {
    return author;
  }

  public AuthorStat author(Author author) {
    this.author = author;
    return this;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public Asset getTheAsset() {
    return theAsset;
  }

  public AuthorStat theAsset(Asset asset) {
    this.theAsset = asset;
    return this;
  }

  public void setTheAsset(Asset asset) {
    this.theAsset = asset;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AuthorStat)) {
      return false;
    }
    return id != null && id.equals(((AuthorStat) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "AuthorStat{"
        + "id="
        + getId()
        + ", rate="
        + getRate()
        + ", visitCount="
        + getVisitCount()
        + ", downloadCount="
        + getDownloadCount()
        + "}";
  }
}
