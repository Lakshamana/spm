package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A AssetStat.
 */
@Entity
@Table(name = "asset_stat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AssetStat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vote_count")
    private Long voteCount;

    @Column(name = "visit_count")
    private Long visitCount;

    @Column(name = "download_count")
    private Long downloadCount;

    @Column(name = "t_votes")
    private Double tVotes;

    @Column(name = "rate")
    private Double rate;

    @OneToOne(mappedBy = "stats")
    @JsonIgnore
    private Asset theAsset;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public AssetStat voteCount(Long voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    public Long getVisitCount() {
        return visitCount;
    }

    public AssetStat visitCount(Long visitCount) {
        this.visitCount = visitCount;
        return this;
    }

    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public AssetStat downloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
        return this;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Double gettVotes() {
        return tVotes;
    }

    public AssetStat tVotes(Double tVotes) {
        this.tVotes = tVotes;
        return this;
    }

    public void settVotes(Double tVotes) {
        this.tVotes = tVotes;
    }

    public Double getRate() {
        return rate;
    }

    public AssetStat rate(Double rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Asset getTheAsset() {
        return theAsset;
    }

    public AssetStat theAsset(Asset asset) {
        this.theAsset = asset;
        return this;
    }

    public void setTheAsset(Asset asset) {
        this.theAsset = asset;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetStat)) {
            return false;
        }
        return id != null && id.equals(((AssetStat) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AssetStat{" +
            "id=" + getId() +
            ", voteCount=" + getVoteCount() +
            ", visitCount=" + getVisitCount() +
            ", downloadCount=" + getDownloadCount() +
            ", tVotes=" + gettVotes() +
            ", rate=" + getRate() +
            "}";
    }
}
