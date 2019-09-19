package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.AssetStat} entity.
 */
public class AssetStatDTO implements Serializable {

    private Long id;

    private Long voteCount;

    private Long visitCount;

    private Long downloadCount;

    private Double tVotes;

    private Double rate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
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

    public Double gettVotes() {
        return tVotes;
    }

    public void settVotes(Double tVotes) {
        this.tVotes = tVotes;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AssetStatDTO assetStatDTO = (AssetStatDTO) o;
        if (assetStatDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), assetStatDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AssetStatDTO{" +
            "id=" + getId() +
            ", voteCount=" + getVoteCount() +
            ", visitCount=" + getVisitCount() +
            ", downloadCount=" + getDownloadCount() +
            ", tVotes=" + gettVotes() +
            ", rate=" + getRate() +
            "}";
    }
}
