package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import br.ufpa.labes.spm.domain.enumeration.ExclusiveStatus;

/**
 * A Exclusive.
 */
@Entity
@Table(name = "exclusive")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Exclusive implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "exclusive_status")
    private ExclusiveStatus exclusiveStatus;

    @Column(name = "unit_of_cost")
    private String unitOfCost;

    @OneToOne(mappedBy = "theExclusiveSub")
    @JsonIgnore
    private Resource theResourceSuper;

    @OneToMany(mappedBy = "theExclusive")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reservation> theReservations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExclusiveStatus getExclusiveStatus() {
        return exclusiveStatus;
    }

    public Exclusive exclusiveStatus(ExclusiveStatus exclusiveStatus) {
        this.exclusiveStatus = exclusiveStatus;
        return this;
    }

    public void setExclusiveStatus(ExclusiveStatus exclusiveStatus) {
        this.exclusiveStatus = exclusiveStatus;
    }

    public String getUnitOfCost() {
        return unitOfCost;
    }

    public Exclusive unitOfCost(String unitOfCost) {
        this.unitOfCost = unitOfCost;
        return this;
    }

    public void setUnitOfCost(String unitOfCost) {
        this.unitOfCost = unitOfCost;
    }

    public Resource getTheResourceSuper() {
        return theResourceSuper;
    }

    public Exclusive theResourceSuper(Resource resource) {
        this.theResourceSuper = resource;
        return this;
    }

    public void setTheResourceSuper(Resource resource) {
        this.theResourceSuper = resource;
    }

    public Set<Reservation> getTheReservations() {
        return theReservations;
    }

    public Exclusive theReservations(Set<Reservation> reservations) {
        this.theReservations = reservations;
        return this;
    }

    public Exclusive addTheReservation(Reservation reservation) {
        this.theReservations.add(reservation);
        reservation.setTheExclusive(this);
        return this;
    }

    public Exclusive removeTheReservation(Reservation reservation) {
        this.theReservations.remove(reservation);
        reservation.setTheExclusive(null);
        return this;
    }

    public void setTheReservations(Set<Reservation> reservations) {
        this.theReservations = reservations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exclusive)) {
            return false;
        }
        return id != null && id.equals(((Exclusive) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Exclusive{" +
            "id=" + getId() +
            ", exclusiveStatus='" + getExclusiveStatus() + "'" +
            ", unitOfCost='" + getUnitOfCost() + "'" +
            "}";
    }
}
