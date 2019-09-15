package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @OneToMany(mappedBy = "tag")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TagStats> theTagStats = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdent() {
        return ident;
    }

    public Tag ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public Set<TagStats> getTheTagStats() {
        return theTagStats;
    }

    public Tag theTagStats(Set<TagStats> tagStats) {
        this.theTagStats = tagStats;
        return this;
    }

    public Tag addTheTagStat(TagStats tagStat) {
        this.theTagStats.add(tagStat);
        tagStat.setTag(this);
        return this;
    }

    public Tag removeTheTagStat(TagStats tagStat) {
        this.theTagStats.remove(tagStat);
        tagStat.setTag(null);
        return this;
    }

    public void setTheTagStats(Set<TagStats> tagStats) {
        this.theTagStats = tagStats;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return id != null && id.equals(((Tag) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            "}";
    }
}
