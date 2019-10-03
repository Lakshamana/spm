package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @ManyToOne
    @JsonIgnoreProperties("theReservations")
    private Exclusive theExclusive;

    @ManyToOne
    @JsonIgnoreProperties("theReservations")
    private Normal theNormal;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public Reservation fromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public Reservation toDate(LocalDate toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Exclusive getTheExclusive() {
        return theExclusive;
    }

    public Reservation theExclusive(Exclusive exclusive) {
        this.theExclusive = exclusive;
        return this;
    }

    public void setTheExclusive(Exclusive exclusive) {
        this.theExclusive = exclusive;
    }

    public Normal getTheNormal() {
        return theNormal;
    }

    public Reservation theNormal(Normal normal) {
        this.theNormal = normal;
        return this;
    }

    public void setTheNormal(Normal normal) {
        this.theNormal = normal;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        return id != null && id.equals(((Reservation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            "}";
    }

    public void removeFromTheNormal(){
      if (this.theNormal!=null){
        this.theNormal.removeTheReservation(this);
      }
    }

    public void insertIntoTheNormal(Normal theNormal){
      theNormal.addTheReservation(this);
    }

    public void removeFromTheExclusive(){
      if (this.theExclusive!=null){
        this.theExclusive.removeTheReservation(this);
      }
    }

    public void insertIntoTheExclusive(Exclusive theExclusive){
      theExclusive.addTheReservation(this);
    }


}
