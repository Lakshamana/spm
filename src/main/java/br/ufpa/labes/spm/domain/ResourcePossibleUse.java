package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A ResourcePossibleUse.
 */
@Entity
@Table(name = "resource_possible_use")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ResourcePossibleUse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "begin")
    private LocalDate begin;

    @Column(name = "end")
    private LocalDate end;

    @Column(name = "amount_needed")
    private Float amountNeeded;

    @ManyToOne
    @JsonIgnoreProperties("theResourcePossibleUses")
    private Resource theResource;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public ResourcePossibleUse begin(LocalDate begin) {
        this.begin = begin;
        return this;
    }

    public void setBegin(LocalDate begin) {
        this.begin = begin;
    }

    public LocalDate getEnd() {
        return end;
    }

    public ResourcePossibleUse end(LocalDate end) {
        this.end = end;
        return this;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Float getAmountNeeded() {
        return amountNeeded;
    }

    public ResourcePossibleUse amountNeeded(Float amountNeeded) {
        this.amountNeeded = amountNeeded;
        return this;
    }

    public void setAmountNeeded(Float amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    public Resource getTheResource() {
        return theResource;
    }

    public ResourcePossibleUse theResource(Resource resource) {
        this.theResource = resource;
        return this;
    }

    public void setTheResource(Resource resource) {
        this.theResource = resource;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourcePossibleUse)) {
            return false;
        }
        return id != null && id.equals(((ResourcePossibleUse) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ResourcePossibleUse{" +
            "id=" + getId() +
            ", begin='" + getBegin() + "'" +
            ", end='" + getEnd() + "'" +
            ", amountNeeded=" + getAmountNeeded() +
            "}";
    }
}
