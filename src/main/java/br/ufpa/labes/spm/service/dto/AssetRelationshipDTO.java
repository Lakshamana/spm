package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.AssetRelationship} entity.
 */
public class AssetRelationshipDTO implements Serializable {

    private Long id;

    private String description;


    private Long kindId;

    private Long theAssetId;

    private Long relatedAssetId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getKindId() {
        return kindId;
    }

    public void setKindId(Long relationshipKindId) {
        this.kindId = relationshipKindId;
    }

    public Long getTheAssetId() {
        return theAssetId;
    }

    public void setTheAssetId(Long assetId) {
        this.theAssetId = assetId;
    }

    public Long getRelatedAssetId() {
        return relatedAssetId;
    }

    public void setRelatedAssetId(Long assetId) {
        this.relatedAssetId = assetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AssetRelationshipDTO assetRelationshipDTO = (AssetRelationshipDTO) o;
        if (assetRelationshipDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), assetRelationshipDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AssetRelationshipDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", kind=" + getKindId() +
            ", theAsset=" + getTheAssetId() +
            ", relatedAsset=" + getRelatedAssetId() +
            "}";
    }
}
