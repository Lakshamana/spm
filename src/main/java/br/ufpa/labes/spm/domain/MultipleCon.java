package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A MultipleCon.
 */
@Entity
@Table(name = "multiple_con")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy=InheritanceType.JOINED)
public class MultipleCon extends Connection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fired")
    private Boolean fired;

    @ManyToOne
    @JsonIgnoreProperties("theMultipleCons")
    private Dependency theDependency;

    @OneToMany(mappedBy = "fromMultipleCon")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BranchCon> toBranchCons = new HashSet<>();

    @OneToMany(mappedBy = "theMultipleCon")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BranchConCondToMultipleCon> theBranchConCondToMultipleCons = new HashSet<>();

    @ManyToMany(mappedBy = "toMultipleCons")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ArtifactCon> theArtifactCons = new HashSet<>();

    @ManyToMany(mappedBy = "toMultipleCons")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<BranchANDCon> theBranchANDS = new HashSet<>();

    @ManyToMany(mappedBy = "fromMultipleCons")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<JoinCon> theJoinCons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isFired() {
        return fired;
    }

    public MultipleCon fired(Boolean fired) {
        this.fired = fired;
        return this;
    }

    public void setFired(Boolean fired) {
        this.fired = fired;
    }

    public Dependency getTheDependency() {
        return theDependency;
    }

    public MultipleCon theDependency(Dependency dependency) {
        this.theDependency = dependency;
        return this;
    }

    public void setTheDependency(Dependency dependency) {
        this.theDependency = dependency;
    }

    public Set<BranchCon> getToBranchCons() {
        return toBranchCons;
    }

    public MultipleCon toBranchCons(Set<BranchCon> branchCons) {
        this.toBranchCons = branchCons;
        return this;
    }

    public MultipleCon addToBranchCon(BranchCon branchCon) {
        this.toBranchCons.add(branchCon);
        branchCon.setFromMultipleCon(this);
        return this;
    }

    public MultipleCon removeToBranchCon(BranchCon branchCon) {
        this.toBranchCons.remove(branchCon);
        branchCon.setFromMultipleCon(null);
        return this;
    }

    public void setToBranchCons(Set<BranchCon> branchCons) {
        this.toBranchCons = branchCons;
    }

    public Set<BranchConCondToMultipleCon> getTheBranchConCondToMultipleCons() {
        return theBranchConCondToMultipleCons;
    }

    public MultipleCon theBranchConCondToMultipleCons(Set<BranchConCondToMultipleCon> branchConCondToMultipleCons) {
        this.theBranchConCondToMultipleCons = branchConCondToMultipleCons;
        return this;
    }

    public MultipleCon addTheBranchConCondToMultipleCon(BranchConCondToMultipleCon branchConCondToMultipleCon) {
        this.theBranchConCondToMultipleCons.add(branchConCondToMultipleCon);
        branchConCondToMultipleCon.setTheMultipleCon(this);
        return this;
    }

    public MultipleCon removeTheBranchConCondToMultipleCon(BranchConCondToMultipleCon branchConCondToMultipleCon) {
        this.theBranchConCondToMultipleCons.remove(branchConCondToMultipleCon);
        branchConCondToMultipleCon.setTheMultipleCon(null);
        return this;
    }

    public void setTheBranchConCondToMultipleCons(Set<BranchConCondToMultipleCon> branchConCondToMultipleCons) {
        this.theBranchConCondToMultipleCons = branchConCondToMultipleCons;
    }

    public Set<ArtifactCon> getTheArtifactCons() {
        return theArtifactCons;
    }

    public MultipleCon theArtifactCons(Set<ArtifactCon> artifactCons) {
        this.theArtifactCons = artifactCons;
        return this;
    }

    public MultipleCon addTheArtifactCon(ArtifactCon artifactCon) {
        this.theArtifactCons.add(artifactCon);
        artifactCon.getToMultipleCons().add(this);
        return this;
    }

    public MultipleCon removeTheArtifactCon(ArtifactCon artifactCon) {
        this.theArtifactCons.remove(artifactCon);
        artifactCon.getToMultipleCons().remove(this);
        return this;
    }

    public void setTheArtifactCons(Set<ArtifactCon> artifactCons) {
        this.theArtifactCons = artifactCons;
    }

    public Set<BranchANDCon> getTheBranchANDS() {
        return theBranchANDS;
    }

    public MultipleCon theBranchANDS(Set<BranchANDCon> branchANDCons) {
        this.theBranchANDS = branchANDCons;
        return this;
    }

    public MultipleCon addTheBranchAND(BranchANDCon branchANDCon) {
        this.theBranchANDS.add(branchANDCon);
        branchANDCon.getToMultipleCons().add(this);
        return this;
    }

    public MultipleCon removeTheBranchAND(BranchANDCon branchANDCon) {
        this.theBranchANDS.remove(branchANDCon);
        branchANDCon.getToMultipleCons().remove(this);
        return this;
    }

    public void setTheBranchANDS(Set<BranchANDCon> branchANDCons) {
        this.theBranchANDS = branchANDCons;
    }

    public Set<JoinCon> getTheJoinCons() {
        return theJoinCons;
    }

    public MultipleCon theJoinCons(Set<JoinCon> joinCons) {
        this.theJoinCons = joinCons;
        return this;
    }

    public MultipleCon addTheJoinCon(JoinCon joinCon) {
        this.theJoinCons.add(joinCon);
        joinCon.getFromMultipleCons().add(this);
        return this;
    }

    public MultipleCon removeTheJoinCon(JoinCon joinCon) {
        this.theJoinCons.remove(joinCon);
        joinCon.getFromMultipleCons().remove(this);
        return this;
    }

    public void setTheJoinCons(Set<JoinCon> joinCons) {
        this.theJoinCons = joinCons;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MultipleCon)) {
            return false;
        }
        return id != null && id.equals(((MultipleCon) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MultipleCon{" +
            "id=" + getId() +
            ", fired='" + isFired() + "'" +
            "}";
    }
}
