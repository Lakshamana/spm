package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import br.ufpa.labes.spm.domain.enumeration.ShareableStatus;

/**
 * A Shareable.
 */
@Entity
@Table(name = "shareable")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Shareable extends Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "shareable_status")
    private ShareableStatus shareableStatus;

    @Column(name = "unit_of_cost")
    private String unitOfCost;

    @OneToOne(mappedBy = "theShareableSub")
    @JsonIgnore
    private Resource theResourceSuper;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShareableStatus getShareableStatus() {
        return shareableStatus;
    }

    public Shareable shareableStatus(ShareableStatus shareableStatus) {
        this.shareableStatus = shareableStatus;
        return this;
    }

    public void setShareableStatus(ShareableStatus shareableStatus) {
        this.shareableStatus = shareableStatus;
    }

    public String getUnitOfCost() {
        return unitOfCost;
    }

    public Shareable unitOfCost(String unitOfCost) {
        this.unitOfCost = unitOfCost;
        return this;
    }

    public void setUnitOfCost(String unitOfCost) {
        this.unitOfCost = unitOfCost;
    }

    public Resource getTheResourceSuper() {
        return theResourceSuper;
    }

    public Shareable theResourceSuper(Resource resource) {
        this.theResourceSuper = resource;
        return this;
    }

    public void setTheResourceSuper(Resource resource) {
        this.theResourceSuper = resource;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shareable)) {
            return false;
        }
        return id != null && id.equals(((Shareable) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Shareable{" +
            "id=" + getId() +
            ", shareableStatus='" + getShareableStatus() + "'" +
            ", unitOfCost='" + getUnitOfCost() + "'" +
            "}";
    }
}
