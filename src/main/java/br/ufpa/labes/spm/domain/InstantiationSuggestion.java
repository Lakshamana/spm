package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A InstantiationSuggestion.
 */
@Entity
@Table(name = "instantiation_suggestion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InstantiationSuggestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theInstSugs")
    private ActivityInstantiated theActivityInstantiated;

    @ManyToOne
    @JsonIgnoreProperties("instSugToResources")
    private Resource chosenResource;

    @ManyToOne
    @JsonIgnoreProperties("instSugToTypes")
    private Type requiredResourceType;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "instantiation_suggestion_sug_rsrc",
               joinColumns = @JoinColumn(name = "instantiation_suggestion_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "sug_rsrc_id", referencedColumnName = "id"))
    private Set<Resource> sugRsrcs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActivityInstantiated getTheActivityInstantiated() {
        return theActivityInstantiated;
    }

    public InstantiationSuggestion theActivityInstantiated(ActivityInstantiated activityInstantiated) {
        this.theActivityInstantiated = activityInstantiated;
        return this;
    }

    public void setTheActivityInstantiated(ActivityInstantiated activityInstantiated) {
        this.theActivityInstantiated = activityInstantiated;
    }

    public Resource getChosenResource() {
        return chosenResource;
    }

    public InstantiationSuggestion chosenResource(Resource resource) {
        this.chosenResource = resource;
        return this;
    }

    public void setChosenResource(Resource resource) {
        this.chosenResource = resource;
    }

    public Type getRequiredResourceType() {
        return requiredResourceType;
    }

    public InstantiationSuggestion requiredResourceType(Type type) {
        this.requiredResourceType = type;
        return this;
    }

    public void setRequiredResourceType(Type type) {
        this.requiredResourceType = type;
    }

    public Set<Resource> getSugRsrcs() {
        return sugRsrcs;
    }

    public InstantiationSuggestion sugRsrcs(Set<Resource> resources) {
        this.sugRsrcs = resources;
        return this;
    }

    public InstantiationSuggestion addSugRsrc(Resource resource) {
        this.sugRsrcs.add(resource);
        resource.getInstSuggestions().add(this);
        return this;
    }

    public InstantiationSuggestion removeSugRsrc(Resource resource) {
        this.sugRsrcs.remove(resource);
        resource.getInstSuggestions().remove(this);
        return this;
    }

    public void setSugRsrcs(Set<Resource> resources) {
        this.sugRsrcs = resources;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstantiationSuggestion)) {
            return false;
        }
        return id != null && id.equals(((InstantiationSuggestion) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "InstantiationSuggestion{" +
            "id=" + getId() +
            "}";
    }
}
