package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ResourceInstSug.
 */
@Entity
@Table(name = "resource_inst_sug")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ResourceInstSug implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theResourceInstSugs")
    private Resource resourceChosen;

    @ManyToOne
    @JsonIgnoreProperties("theResourceInstSugs")
    private ResourceType resourceTypeRequired;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "resource_inst_sug_resource_suggested",
               joinColumns = @JoinColumn(name = "resource_inst_sug_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "resource_suggested_id", referencedColumnName = "id"))
    private Set<Resource> resourceSuggesteds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resource getResourceChosen() {
        return resourceChosen;
    }

    public ResourceInstSug resourceChosen(Resource resource) {
        this.resourceChosen = resource;
        return this;
    }

    public void setResourceChosen(Resource resource) {
        this.resourceChosen = resource;
    }

    public ResourceType getResourceTypeRequired() {
        return resourceTypeRequired;
    }

    public ResourceInstSug resourceTypeRequired(ResourceType resourceType) {
        this.resourceTypeRequired = resourceType;
        return this;
    }

    public void setResourceTypeRequired(ResourceType resourceType) {
        this.resourceTypeRequired = resourceType;
    }

    public Set<Resource> getResourceSuggesteds() {
        return resourceSuggesteds;
    }

    public ResourceInstSug resourceSuggesteds(Set<Resource> resources) {
        this.resourceSuggesteds = resources;
        return this;
    }

    public ResourceInstSug addResourceSuggested(Resource resource) {
        this.resourceSuggesteds.add(resource);
        resource.getTheResourceInstSugs().add(this);
        return this;
    }

    public ResourceInstSug removeResourceSuggested(Resource resource) {
        this.resourceSuggesteds.remove(resource);
        resource.getTheResourceInstSugs().remove(this);
        return this;
    }

    public void setResourceSuggesteds(Set<Resource> resources) {
        this.resourceSuggesteds = resources;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceInstSug)) {
            return false;
        }
        return id != null && id.equals(((ResourceInstSug) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ResourceInstSug{" +
            "id=" + getId() +
            "}";
    }
}
