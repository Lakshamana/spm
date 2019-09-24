package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;
import br.ufpa.labes.spm.domain.enumeration.ShareableStatus;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Shareable} entity.
 */
public class ShareableDTO implements Serializable {

    private Long id;

    private ShareableStatus shareableStatus;

    private String unitOfCost;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShareableStatus getShareableStatus() {
        return shareableStatus;
    }

    public void setShareableStatus(ShareableStatus shareableStatus) {
        this.shareableStatus = shareableStatus;
    }

    public String getUnitOfCost() {
        return unitOfCost;
    }

    public void setUnitOfCost(String unitOfCost) {
        this.unitOfCost = unitOfCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShareableDTO shareableDTO = (ShareableDTO) o;
        if (shareableDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shareableDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShareableDTO{" +
            "id=" + getId() +
            ", shareableStatus='" + getShareableStatus() + "'" +
            ", unitOfCost='" + getUnitOfCost() + "'" +
            "}";
    }
}
