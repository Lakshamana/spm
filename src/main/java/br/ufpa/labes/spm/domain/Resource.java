package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Resource.
 */
@Entity
@Table(name = "resource")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy=InheritanceType.JOINED)
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "mtbf_time")
    private Float mtbfTime;

    @Column(name = "mtbf_unit_time")
    private String mtbfUnitTime;

    @Column(name = "currency")
    private String currency;

    @Column(name = "cost")
    private Float cost;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties("possesses")
    private Resource belongsTo;

    @ManyToOne
    @JsonIgnoreProperties("theResources")
    private ResourceType theResourceType;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "resource_requires",
               joinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "requires_id", referencedColumnName = "id"))
    private Set<Resource> requires = new HashSet<>();

    @OneToMany(mappedBy = "theResource")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ResourceEvent> theResourceEvents = new HashSet<>();

    @OneToMany(mappedBy = "theResource")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RequiredResource> theRequiredResources = new HashSet<>();

    @OneToMany(mappedBy = "resourceChosen")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ResourceInstSug> theResourceChosenSuggestions = new HashSet<>();

    @OneToMany(mappedBy = "theResource")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ResourcePossibleUse> theResourcePossibleUses = new HashSet<>();

    @OneToMany(mappedBy = "resource")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ResourceMetric> theResourceMetrics = new HashSet<>();

    @OneToMany(mappedBy = "resource")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ResourceEstimation> theResourceEstimations = new HashSet<>();

    @OneToMany(mappedBy = "belongsTo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Resource> possesses = new HashSet<>();

    @ManyToMany(mappedBy = "resourceSuggesteds")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ResourceInstSug> theResourceSuggestions = new HashSet<>();

    @ManyToMany(mappedBy = "requires")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Resource> isRequireds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdent() {
        return ident;
    }

    public Resource ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getName() {
        return name;
    }

    public Resource name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Resource description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getMtbfTime() {
        return mtbfTime;
    }

    public Resource mtbfTime(Float mtbfTime) {
        this.mtbfTime = mtbfTime;
        return this;
    }

    public void setMtbfTime(Float mtbfTime) {
        this.mtbfTime = mtbfTime;
    }

    public String getMtbfUnitTime() {
        return mtbfUnitTime;
    }

    public Resource mtbfUnitTime(String mtbfUnitTime) {
        this.mtbfUnitTime = mtbfUnitTime;
        return this;
    }

    public void setMtbfUnitTime(String mtbfUnitTime) {
        this.mtbfUnitTime = mtbfUnitTime;
    }

    public String getCurrency() {
        return currency;
    }

    public Resource currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getCost() {
        return cost;
    }

    public Resource cost(Float cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Resource isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Resource getBelongsTo() {
        return belongsTo;
    }

    public Resource belongsTo(Resource resource) {
        this.belongsTo = resource;
        return this;
    }

    public void setBelongsTo(Resource resource) {
        this.belongsTo = resource;
    }

    public ResourceType getTheResourceType() {
        return theResourceType;
    }

    public Resource theResourceType(ResourceType resourceType) {
        this.theResourceType = resourceType;
        return this;
    }

    public void setTheResourceType(ResourceType resourceType) {
        this.theResourceType = resourceType;
    }

    public Set<Resource> getRequires() {
        return requires;
    }

    public Resource requires(Set<Resource> resources) {
        this.requires = resources;
        return this;
    }

    public Resource addRequires(Resource resource) {
        this.requires.add(resource);
        resource.getIsRequireds().add(this);
        return this;
    }

    public Resource removeRequires(Resource resource) {
        this.requires.remove(resource);
        resource.getIsRequireds().remove(this);
        return this;
    }

    public void setRequires(Set<Resource> resources) {
        this.requires = resources;
    }

    public Set<ResourceEvent> getTheResourceEvents() {
        return theResourceEvents;
    }

    public Resource theResourceEvents(Set<ResourceEvent> resourceEvents) {
        this.theResourceEvents = resourceEvents;
        return this;
    }

    public Resource addTheResourceEvent(ResourceEvent resourceEvent) {
        this.theResourceEvents.add(resourceEvent);
        resourceEvent.setTheResource(this);
        return this;
    }

    public Resource removeTheResourceEvent(ResourceEvent resourceEvent) {
        this.theResourceEvents.remove(resourceEvent);
        resourceEvent.setTheResource(null);
        return this;
    }

    public void setTheResourceEvents(Set<ResourceEvent> resourceEvents) {
        this.theResourceEvents = resourceEvents;
    }

    public Set<RequiredResource> getTheRequiredResources() {
        return theRequiredResources;
    }

    public Resource theRequiredResources(Set<RequiredResource> requiredResources) {
        this.theRequiredResources = requiredResources;
        return this;
    }

    public Resource addTheRequiredResource(RequiredResource requiredResource) {
        this.theRequiredResources.add(requiredResource);
        requiredResource.setTheResource(this);
        return this;
    }

    public Resource removeTheRequiredResource(RequiredResource requiredResource) {
        this.theRequiredResources.remove(requiredResource);
        requiredResource.setTheResource(null);
        return this;
    }

    public void setTheRequiredResources(Set<RequiredResource> requiredResources) {
        this.theRequiredResources = requiredResources;
    }

    public Set<ResourceInstSug> getTheResourceChosenSuggestions() {
        return theResourceChosenSuggestions;
    }

    public Resource theResourceChosenSuggestions(Set<ResourceInstSug> resourceInstSugs) {
        this.theResourceChosenSuggestions = resourceInstSugs;
        return this;
    }

    public Resource addTheResourceChosenSuggestions(ResourceInstSug resourceInstSug) {
        this.theResourceChosenSuggestions.add(resourceInstSug);
        resourceInstSug.setResourceChosen(this);
        return this;
    }

    public Resource removeTheResourceChosenSuggestions(ResourceInstSug resourceInstSug) {
        this.theResourceChosenSuggestions.remove(resourceInstSug);
        resourceInstSug.setResourceChosen(null);
        return this;
    }

    public void setTheResourceChosenSuggestions(Set<ResourceInstSug> resourceInstSugs) {
        this.theResourceChosenSuggestions = resourceInstSugs;
    }

    public Set<ResourcePossibleUse> getTheResourcePossibleUses() {
        return theResourcePossibleUses;
    }

    public Resource theResourcePossibleUses(Set<ResourcePossibleUse> resourcePossibleUses) {
        this.theResourcePossibleUses = resourcePossibleUses;
        return this;
    }

    public Resource addTheResourcePossibleUse(ResourcePossibleUse resourcePossibleUse) {
        this.theResourcePossibleUses.add(resourcePossibleUse);
        resourcePossibleUse.setTheResource(this);
        return this;
    }

    public Resource removeTheResourcePossibleUse(ResourcePossibleUse resourcePossibleUse) {
        this.theResourcePossibleUses.remove(resourcePossibleUse);
        resourcePossibleUse.setTheResource(null);
        return this;
    }

    public void setTheResourcePossibleUses(Set<ResourcePossibleUse> resourcePossibleUses) {
        this.theResourcePossibleUses = resourcePossibleUses;
    }

    public Set<ResourceMetric> getTheResourceMetrics() {
        return theResourceMetrics;
    }

    public Resource theResourceMetrics(Set<ResourceMetric> resourceMetrics) {
        this.theResourceMetrics = resourceMetrics;
        return this;
    }

    public Resource addTheResourceMetric(ResourceMetric resourceMetric) {
        this.theResourceMetrics.add(resourceMetric);
        resourceMetric.setResource(this);
        return this;
    }

    public Resource removeTheResourceMetric(ResourceMetric resourceMetric) {
        this.theResourceMetrics.remove(resourceMetric);
        resourceMetric.setResource(null);
        return this;
    }

    public void setTheResourceMetrics(Set<ResourceMetric> resourceMetrics) {
        this.theResourceMetrics = resourceMetrics;
    }

    public Set<ResourceEstimation> getTheResourceEstimations() {
        return theResourceEstimations;
    }

    public Resource theResourceEstimations(Set<ResourceEstimation> resourceEstimations) {
        this.theResourceEstimations = resourceEstimations;
        return this;
    }

    public Resource addTheResourceEstimation(ResourceEstimation resourceEstimation) {
        this.theResourceEstimations.add(resourceEstimation);
        resourceEstimation.setResource(this);
        return this;
    }

    public Resource removeTheResourceEstimation(ResourceEstimation resourceEstimation) {
        this.theResourceEstimations.remove(resourceEstimation);
        resourceEstimation.setResource(null);
        return this;
    }

    public void setTheResourceEstimations(Set<ResourceEstimation> resourceEstimations) {
        this.theResourceEstimations = resourceEstimations;
    }

    public Set<Resource> getPossesses() {
        return possesses;
    }

    public Resource possesses(Set<Resource> resources) {
        this.possesses = resources;
        return this;
    }

    public Resource addPossess(Resource resource) {
        this.possesses.add(resource);
        resource.setBelongsTo(this);
        return this;
    }

    public Resource removePossess(Resource resource) {
        this.possesses.remove(resource);
        resource.setBelongsTo(null);
        return this;
    }

    public void setPossesses(Set<Resource> resources) {
        this.possesses = resources;
    }

    public Set<ResourceInstSug> getTheResourceSuggestions() {
        return theResourceSuggestions;
    }

    public Resource theResourceSuggestions(Set<ResourceInstSug> resourceInstSugs) {
        this.theResourceSuggestions = resourceInstSugs;
        return this;
    }

    public Resource addTheResourceSuggestions(ResourceInstSug resourceInstSug) {
        this.theResourceSuggestions.add(resourceInstSug);
        resourceInstSug.getResourceSuggesteds().add(this);
        return this;
    }

    public Resource removeTheResourceSuggestions(ResourceInstSug resourceInstSug) {
        this.theResourceSuggestions.remove(resourceInstSug);
        resourceInstSug.getResourceSuggesteds().remove(this);
        return this;
    }

    public void setTheResourceSuggestions(Set<ResourceInstSug> resourceInstSugs) {
        this.theResourceSuggestions = resourceInstSugs;
    }

    public Set<Resource> getIsRequireds() {
        return isRequireds;
    }

    public Resource isRequireds(Set<Resource> resources) {
        this.isRequireds = resources;
        return this;
    }

    public Resource addIsRequired(Resource resource) {
        this.isRequireds.add(resource);
        resource.getRequires().add(this);
        return this;
    }

    public Resource removeIsRequired(Resource resource) {
        this.isRequireds.remove(resource);
        resource.getRequires().remove(this);
        return this;
    }

    public void setIsRequireds(Set<Resource> resources) {
        this.isRequireds = resources;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resource)) {
            return false;
        }
        return id != null && id.equals(((Resource) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Resource{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", mtbfTime=" + getMtbfTime() +
            ", mtbfUnitTime='" + getMtbfUnitTime() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", cost=" + getCost() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
