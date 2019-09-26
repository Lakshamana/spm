package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.AuthorStat} entity.
 */
public class AuthorStatDTO implements Serializable {

    private Long id;


    private Long assetId;

    private Long authorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
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
        return "AuthorStatDTO{" +
            "id=" + getId() +
            ", asset=" + getAssetId() +
            ", author=" + getAuthorId() +
            "}";
    }
}
