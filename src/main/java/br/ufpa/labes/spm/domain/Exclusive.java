package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "state")
    private String state;

    @Column(name = "unit_of_cost")
    private String unitOfCost;

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

    public String getState() {
        return state;
    }

    public Exclusive state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
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
            ", state='" + getState() + "'" +
            ", unitOfCost='" + getUnitOfCost() + "'" +
            "}";
    }
}
