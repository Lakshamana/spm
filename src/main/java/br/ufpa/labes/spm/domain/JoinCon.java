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
 * A JoinCon.
 */
@Entity
@Table(name = "join_con")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class JoinCon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kind_join_con")
    private String kindJoinCon;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "join_con_from_multiple_con",
               joinColumns = @JoinColumn(name = "join_con_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "from_multiple_con_id", referencedColumnName = "id"))
    private Set<MultipleCon> fromMultipleCons = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("fromJoinCons")
    private Activity toActivity;

    @ManyToMany(mappedBy = "toJoinCons")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Activity> fromActivities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKindJoinCon() {
        return kindJoinCon;
    }

    public JoinCon kindJoinCon(String kindJoinCon) {
        this.kindJoinCon = kindJoinCon;
        return this;
    }

    public void setKindJoinCon(String kindJoinCon) {
        this.kindJoinCon = kindJoinCon;
    }

    public Set<MultipleCon> getFromMultipleCons() {
        return fromMultipleCons;
    }

    public JoinCon fromMultipleCons(Set<MultipleCon> multipleCons) {
        this.fromMultipleCons = multipleCons;
        return this;
    }

    public JoinCon addFromMultipleCon(MultipleCon multipleCon) {
        this.fromMultipleCons.add(multipleCon);
        multipleCon.getTheJoinCons().add(this);
        return this;
    }

    public JoinCon removeFromMultipleCon(MultipleCon multipleCon) {
        this.fromMultipleCons.remove(multipleCon);
        multipleCon.getTheJoinCons().remove(this);
        return this;
    }

    public void setFromMultipleCons(Set<MultipleCon> multipleCons) {
        this.fromMultipleCons = multipleCons;
    }

    public Activity getToActivity() {
        return toActivity;
    }

    public JoinCon toActivity(Activity activity) {
        this.toActivity = activity;
        return this;
    }

    public void setToActivity(Activity activity) {
        this.toActivity = activity;
    }

    public Set<Activity> getFromActivities() {
        return fromActivities;
    }

    public JoinCon fromActivities(Set<Activity> activities) {
        this.fromActivities = activities;
        return this;
    }

    public JoinCon addFromActivity(Activity activity) {
        this.fromActivities.add(activity);
        activity.getToJoinCons().add(this);
        return this;
    }

    public JoinCon removeFromActivity(Activity activity) {
        this.fromActivities.remove(activity);
        activity.getToJoinCons().remove(this);
        return this;
    }

    public void setFromActivities(Set<Activity> activities) {
        this.fromActivities = activities;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JoinCon)) {
            return false;
        }
        return id != null && id.equals(((JoinCon) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "JoinCon{" +
            "id=" + getId() +
            ", kindJoinCon='" + getKindJoinCon() + "'" +
            "}";
    }
}
