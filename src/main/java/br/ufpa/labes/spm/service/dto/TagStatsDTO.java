package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.TagStats} entity.
 */
public class TagStatDTO implements Serializable {

    private Long id;

    private Long count;


    private Long tagId;

    private Long theAssetId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
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

        TagStatDTO tagStatDTO = (TagStatDTO) o;
        if (tagStatDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tagStatDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TagStatDTO{" +
            "id=" + getId() +
            ", count=" + getCount() +
            ", tag=" + getTagId() +
            ", theAsset=" + getTheAssetId() +
            "}";
    }
}
