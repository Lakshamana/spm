package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.InstantiationSuggestion} entity.
 */
public class InstantiationSuggestionDTO implements Serializable {

    private Long id;


    private Long theActivityInstantiatedId;

    private Set<ResourceDTO> sugRsrcs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheActivityInstantiatedId() {
        return theActivityInstantiatedId;
    }

    public void setTheActivityInstantiatedId(Long activityInstantiatedId) {
        this.theActivityInstantiatedId = activityInstantiatedId;
    }

    public Set<ResourceDTO> getSugRsrcs() {
        return sugRsrcs;
    }

    public void setSugRsrcs(Set<ResourceDTO> resources) {
        this.sugRsrcs = resources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InstantiationSuggestionDTO instantiationSuggestionDTO = (InstantiationSuggestionDTO) o;
        if (instantiationSuggestionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), instantiationSuggestionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InstantiationSuggestionDTO{" +
            "id=" + getId() +
            ", theActivityInstantiated=" + getTheActivityInstantiatedId() +
            "}";
    }
}
