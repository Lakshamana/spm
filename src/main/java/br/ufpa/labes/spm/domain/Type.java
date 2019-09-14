package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Type.
 */
@Entity
@Table(name = "type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "user_defined")
    private Boolean userDefined;

    @OneToOne
    @JoinColumn(unique = true)
    private AbilityType theAbilityTypeSub;

    @OneToOne
    @JoinColumn(unique = true)
    private ActivityType theActivityTypeSub;

    @OneToOne
    @JoinColumn(unique = true)
    private ArtifactType theArtifactTypeSub;

    @OneToOne
    @JoinColumn(unique = true)
    private ConnectionType theConnectionTypeSub;

    @OneToOne
    @JoinColumn(unique = true)
    private EventType theEventTypeSub;

    @OneToOne
    @JoinColumn(unique = true)
    private WorkGroupType theWorkGroupTypeSub;

    @OneToOne
    @JoinColumn(unique = true)
    private MetricType theMetricTypeSub;

    @OneToOne
    @JoinColumn(unique = true)
    private ResourceType theResourceTypeSub;

    @OneToOne
    @JoinColumn(unique = true)
    private RoleType theRoleTypeSub;

    @OneToOne
    @JoinColumn(unique = true)
    private ToolType theToolTypeSub;

    @ManyToOne
    @JsonIgnoreProperties("subTypes")
    private Type superType;

    @OneToMany(mappedBy = "workGroupTypeRequired")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WorkGroupInstSug> sugToReqWorkGroups = new HashSet<>();

    @OneToMany(mappedBy = "requiredResourceType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstantiationSuggestion> instSugToTypes = new HashSet<>();

    @OneToMany(mappedBy = "superType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Type> subTypes = new HashSet<>();

    @ManyToMany(mappedBy = "theArtifactTypes")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ToolDefinition> theToolDefinitionToArtifactTypes = new HashSet<>();

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

    public Type ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getDescription() {
        return description;
    }

    public Type description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isUserDefined() {
        return userDefined;
    }

    public Type userDefined(Boolean userDefined) {
        this.userDefined = userDefined;
        return this;
    }

    public void setUserDefined(Boolean userDefined) {
        this.userDefined = userDefined;
    }

    public AbilityType getTheAbilityTypeSub() {
        return theAbilityTypeSub;
    }

    public Type theAbilityTypeSub(AbilityType abilityType) {
        this.theAbilityTypeSub = abilityType;
        return this;
    }

    public void setTheAbilityTypeSub(AbilityType abilityType) {
        this.theAbilityTypeSub = abilityType;
    }

    public ActivityType getTheActivityTypeSub() {
        return theActivityTypeSub;
    }

    public Type theActivityTypeSub(ActivityType activityType) {
        this.theActivityTypeSub = activityType;
        return this;
    }

    public void setTheActivityTypeSub(ActivityType activityType) {
        this.theActivityTypeSub = activityType;
    }

    public ArtifactType getTheArtifactTypeSub() {
        return theArtifactTypeSub;
    }

    public Type theArtifactTypeSub(ArtifactType artifactType) {
        this.theArtifactTypeSub = artifactType;
        return this;
    }

    public void setTheArtifactTypeSub(ArtifactType artifactType) {
        this.theArtifactTypeSub = artifactType;
    }

    public ConnectionType getTheConnectionTypeSub() {
        return theConnectionTypeSub;
    }

    public Type theConnectionTypeSub(ConnectionType connectionType) {
        this.theConnectionTypeSub = connectionType;
        return this;
    }

    public void setTheConnectionTypeSub(ConnectionType connectionType) {
        this.theConnectionTypeSub = connectionType;
    }

    public EventType getTheEventTypeSub() {
        return theEventTypeSub;
    }

    public Type theEventTypeSub(EventType eventType) {
        this.theEventTypeSub = eventType;
        return this;
    }

    public void setTheEventTypeSub(EventType eventType) {
        this.theEventTypeSub = eventType;
    }

    public WorkGroupType getTheWorkGroupTypeSub() {
        return theWorkGroupTypeSub;
    }

    public Type theWorkGroupTypeSub(WorkGroupType workGroupType) {
        this.theWorkGroupTypeSub = workGroupType;
        return this;
    }

    public void setTheWorkGroupTypeSub(WorkGroupType workGroupType) {
        this.theWorkGroupTypeSub = workGroupType;
    }

    public MetricType getTheMetricTypeSub() {
        return theMetricTypeSub;
    }

    public Type theMetricTypeSub(MetricType metricType) {
        this.theMetricTypeSub = metricType;
        return this;
    }

    public void setTheMetricTypeSub(MetricType metricType) {
        this.theMetricTypeSub = metricType;
    }

    public ResourceType getTheResourceTypeSub() {
        return theResourceTypeSub;
    }

    public Type theResourceTypeSub(ResourceType resourceType) {
        this.theResourceTypeSub = resourceType;
        return this;
    }

    public void setTheResourceTypeSub(ResourceType resourceType) {
        this.theResourceTypeSub = resourceType;
    }

    public RoleType getTheRoleTypeSub() {
        return theRoleTypeSub;
    }

    public Type theRoleTypeSub(RoleType roleType) {
        this.theRoleTypeSub = roleType;
        return this;
    }

    public void setTheRoleTypeSub(RoleType roleType) {
        this.theRoleTypeSub = roleType;
    }

    public ToolType getTheToolTypeSub() {
        return theToolTypeSub;
    }

    public Type theToolTypeSub(ToolType toolType) {
        this.theToolTypeSub = toolType;
        return this;
    }

    public void setTheToolTypeSub(ToolType toolType) {
        this.theToolTypeSub = toolType;
    }

    public Type getSuperType() {
        return superType;
    }

    public Type superType(Type type) {
        this.superType = type;
        return this;
    }

    public void setSuperType(Type type) {
        this.superType = type;
    }

    public Set<WorkGroupInstSug> getSugToReqWorkGroups() {
        return sugToReqWorkGroups;
    }

    public Type sugToReqWorkGroups(Set<WorkGroupInstSug> workGroupInstSugs) {
        this.sugToReqWorkGroups = workGroupInstSugs;
        return this;
    }

    public Type addSugToReqWorkGroup(WorkGroupInstSug workGroupInstSug) {
        this.sugToReqWorkGroups.add(workGroupInstSug);
        workGroupInstSug.setWorkGroupTypeRequired(this);
        return this;
    }

    public Type removeSugToReqWorkGroup(WorkGroupInstSug workGroupInstSug) {
        this.sugToReqWorkGroups.remove(workGroupInstSug);
        workGroupInstSug.setWorkGroupTypeRequired(null);
        return this;
    }

    public void setSugToReqWorkGroups(Set<WorkGroupInstSug> workGroupInstSugs) {
        this.sugToReqWorkGroups = workGroupInstSugs;
    }

    public Set<InstantiationSuggestion> getInstSugToTypes() {
        return instSugToTypes;
    }

    public Type instSugToTypes(Set<InstantiationSuggestion> instantiationSuggestions) {
        this.instSugToTypes = instantiationSuggestions;
        return this;
    }

    public Type addInstSugToType(InstantiationSuggestion instantiationSuggestion) {
        this.instSugToTypes.add(instantiationSuggestion);
        instantiationSuggestion.setRequiredResourceType(this);
        return this;
    }

    public Type removeInstSugToType(InstantiationSuggestion instantiationSuggestion) {
        this.instSugToTypes.remove(instantiationSuggestion);
        instantiationSuggestion.setRequiredResourceType(null);
        return this;
    }

    public void setInstSugToTypes(Set<InstantiationSuggestion> instantiationSuggestions) {
        this.instSugToTypes = instantiationSuggestions;
    }

    public Set<Type> getSubTypes() {
        return subTypes;
    }

    public Type subTypes(Set<Type> types) {
        this.subTypes = types;
        return this;
    }

    public Type addSubType(Type type) {
        this.subTypes.add(type);
        type.setSuperType(this);
        return this;
    }

    public Type removeSubType(Type type) {
        this.subTypes.remove(type);
        type.setSuperType(null);
        return this;
    }

    public void setSubTypes(Set<Type> types) {
        this.subTypes = types;
    }

    public Set<ToolDefinition> getTheToolDefinitionToArtifactTypes() {
        return theToolDefinitionToArtifactTypes;
    }

    public Type theToolDefinitionToArtifactTypes(Set<ToolDefinition> toolDefinitions) {
        this.theToolDefinitionToArtifactTypes = toolDefinitions;
        return this;
    }

    public Type addTheToolDefinitionToArtifactType(ToolDefinition toolDefinition) {
        this.theToolDefinitionToArtifactTypes.add(toolDefinition);
        toolDefinition.getTheArtifactTypes().add(this);
        return this;
    }

    public Type removeTheToolDefinitionToArtifactType(ToolDefinition toolDefinition) {
        this.theToolDefinitionToArtifactTypes.remove(toolDefinition);
        toolDefinition.getTheArtifactTypes().remove(this);
        return this;
    }

    public void setTheToolDefinitionToArtifactTypes(Set<ToolDefinition> toolDefinitions) {
        this.theToolDefinitionToArtifactTypes = toolDefinitions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Type)) {
            return false;
        }
        return id != null && id.equals(((Type) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Type{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", description='" + getDescription() + "'" +
            ", userDefined='" + isUserDefined() + "'" +
            "}";
    }
}
