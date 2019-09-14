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


    private Long thePeopleInstSugSubId;

    private Long theActivityInstantiatedId;

    private Long chosenResourceId;

    private Long requiredResourceTypeId;

    private Set<ResourceDTO> sugRsrcs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getThePeopleInstSugSubId() {
        return thePeopleInstSugSubId;
    }

    public void setThePeopleInstSugSubId(Long peopleInstSugId) {
        this.thePeopleInstSugSubId = peopleInstSugId;
    }

    public Long getTheActivityInstantiatedId() {
        return theActivityInstantiatedId;
    }

    public void setTheActivityInstantiatedId(Long activityInstantiatedId) {
        this.theActivityInstantiatedId = activityInstantiatedId;
    }

    public Long getChosenResourceId() {
        return chosenResourceId;
    }

    public void setChosenResourceId(Long resourceId) {
        this.chosenResourceId = resourceId;
    }

    public Long getRequiredResourceTypeId() {
        return requiredResourceTypeId;
    }

    public void setRequiredResourceTypeId(Long typeId) {
        this.requiredResourceTypeId = typeId;
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
            ", thePeopleInstSugSub=" + getThePeopleInstSugSubId() +
            ", theActivityInstantiated=" + getTheActivityInstantiatedId() +
            ", chosenResource=" + getChosenResourceId() +
            ", requiredResourceType=" + getRequiredResourceTypeId() +
            "}";
    }
}
