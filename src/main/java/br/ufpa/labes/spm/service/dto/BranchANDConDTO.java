package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.BranchANDCon} entity.
 */
public class BranchANDConDTO implements Serializable {

    private Long id;


    private Set<MultipleConDTO> toMultipleCons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<MultipleConDTO> getToMultipleCons() {
        return toMultipleCons;
    }

    public void setToMultipleCons(Set<MultipleConDTO> multipleCons) {
        this.toMultipleCons = multipleCons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BranchANDConDTO branchANDConDTO = (BranchANDConDTO) o;
        if (branchANDConDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), branchANDConDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BranchANDConDTO{" +
            "id=" + getId() +
            "}";
    }
}
