package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ToolDefinition.
 */
@Entity
@Table(name = "tool_definition")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ToolDefinition implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties("theToolDefinitions")
    private ToolType theToolType;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "tool_definition_the_artifact_types",
               joinColumns = @JoinColumn(name = "tool_definition_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "the_artifact_types_id", referencedColumnName = "id"))
    private Set<Type> theArtifactTypes = new HashSet<>();

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

    public ToolDefinition ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getName() {
        return name;
    }

    public ToolDefinition name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public ToolDefinition description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ToolType getTheToolType() {
        return theToolType;
    }

    public ToolDefinition theToolType(ToolType toolType) {
        this.theToolType = toolType;
        return this;
    }

    public void setTheToolType(ToolType toolType) {
        this.theToolType = toolType;
    }

    public Set<Type> getTheArtifactTypes() {
        return theArtifactTypes;
    }

    public ToolDefinition theArtifactTypes(Set<Type> types) {
        this.theArtifactTypes = types;
        return this;
    }

    public ToolDefinition addTheArtifactTypes(Type type) {
        this.theArtifactTypes.add(type);
        type.getTheToolDefinitionToArtifactTypes().add(this);
        return this;
    }

    public ToolDefinition removeTheArtifactTypes(Type type) {
        this.theArtifactTypes.remove(type);
        type.getTheToolDefinitionToArtifactTypes().remove(this);
        return this;
    }

    public void setTheArtifactTypes(Set<Type> types) {
        this.theArtifactTypes = types;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToolDefinition)) {
            return false;
        }
        return id != null && id.equals(((ToolDefinition) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ToolDefinition{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
