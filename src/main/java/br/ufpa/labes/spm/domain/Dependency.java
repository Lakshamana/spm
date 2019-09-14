package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Dependency.
 */
@Entity
@Table(name = "dependency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dependency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kind_dep")
    private String kindDep;

    @OneToMany(mappedBy = "theDependency")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MultipleCon> theMultipleCons = new HashSet<>();

    @OneToMany(mappedBy = "theDependency")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sequence> theSequences = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKindDep() {
        return kindDep;
    }

    public Dependency kindDep(String kindDep) {
        this.kindDep = kindDep;
        return this;
    }

    public void setKindDep(String kindDep) {
        this.kindDep = kindDep;
    }

    public Set<MultipleCon> getTheMultipleCons() {
        return theMultipleCons;
    }

    public Dependency theMultipleCons(Set<MultipleCon> multipleCons) {
        this.theMultipleCons = multipleCons;
        return this;
    }

    public Dependency addTheMultipleCon(MultipleCon multipleCon) {
        this.theMultipleCons.add(multipleCon);
        multipleCon.setTheDependency(this);
        return this;
    }

    public Dependency removeTheMultipleCon(MultipleCon multipleCon) {
        this.theMultipleCons.remove(multipleCon);
        multipleCon.setTheDependency(null);
        return this;
    }

    public void setTheMultipleCons(Set<MultipleCon> multipleCons) {
        this.theMultipleCons = multipleCons;
    }

    public Set<Sequence> getTheSequences() {
        return theSequences;
    }

    public Dependency theSequences(Set<Sequence> sequences) {
        this.theSequences = sequences;
        return this;
    }

    public Dependency addTheSequence(Sequence sequence) {
        this.theSequences.add(sequence);
        sequence.setTheDependency(this);
        return this;
    }

    public Dependency removeTheSequence(Sequence sequence) {
        this.theSequences.remove(sequence);
        sequence.setTheDependency(null);
        return this;
    }

    public void setTheSequences(Set<Sequence> sequences) {
        this.theSequences = sequences;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dependency)) {
            return false;
        }
        return id != null && id.equals(((Dependency) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Dependency{" +
            "id=" + getId() +
            ", kindDep='" + getKindDep() + "'" +
            "}";
    }
}
