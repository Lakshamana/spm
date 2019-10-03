package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A AssetRelationship.
 */
@Entity
@Table(name = "asset_relationship")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AssetRelationship implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties("theAssetRelationships")
    private RelationshipKind kind;

    @ManyToOne
    @JsonIgnoreProperties("relatedAssets")
    private Asset asset;

    @ManyToOne
    @JsonIgnoreProperties("relatedByAssets")
    private Asset relatedAsset;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public AssetRelationship description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RelationshipKind getKind() {
        return kind;
    }

    public AssetRelationship kind(RelationshipKind relationshipKind) {
        this.kind = relationshipKind;
        return this;
    }

    public void setKind(RelationshipKind relationshipKind) {
        this.kind = relationshipKind;
    }

    public Asset getAsset() {
        return asset;
    }

    public AssetRelationship asset(Asset asset) {
        this.asset = asset;
        return this;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Asset getRelatedAsset() {
        return relatedAsset;
    }

    public AssetRelationship relatedAsset(Asset asset) {
        this.relatedAsset = asset;
        return this;
    }

    public void setRelatedAsset(Asset asset) {
        this.relatedAsset = asset;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetRelationship)) {
            return false;
        }
        return id != null && id.equals(((AssetRelationship) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AssetRelationship{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
