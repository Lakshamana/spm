package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A RelationshipKind.
 */
@Entity
@Table(name = "relationship_kind")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RelationshipKind implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_ident")
    private String typeIdent;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "kind")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AssetRelationship> theAssetRelationships = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeIdent() {
        return typeIdent;
    }

    public RelationshipKind typeIdent(String typeIdent) {
        this.typeIdent = typeIdent;
        return this;
    }

    public void setTypeIdent(String typeIdent) {
        this.typeIdent = typeIdent;
    }

    public String getDescription() {
        return description;
    }

    public RelationshipKind description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<AssetRelationship> getTheAssetRelationships() {
        return theAssetRelationships;
    }

    public RelationshipKind theAssetRelationships(Set<AssetRelationship> assetRelationships) {
        this.theAssetRelationships = assetRelationships;
        return this;
    }

    public RelationshipKind addTheAssetRelationship(AssetRelationship assetRelationship) {
        this.theAssetRelationships.add(assetRelationship);
        assetRelationship.setKind(this);
        return this;
    }

    public RelationshipKind removeTheAssetRelationship(AssetRelationship assetRelationship) {
        this.theAssetRelationships.remove(assetRelationship);
        assetRelationship.setKind(null);
        return this;
    }

    public void setTheAssetRelationships(Set<AssetRelationship> assetRelationships) {
        this.theAssetRelationships = assetRelationships;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RelationshipKind)) {
            return false;
        }
        return id != null && id.equals(((RelationshipKind) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RelationshipKind{" +
            "id=" + getId() +
            ", typeIdent='" + getTypeIdent() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
