package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A TagStats.
 */
@Entity
@Table(name = "tag_stat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TagStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count")
    private Long count;

    @ManyToOne
    @JsonIgnoreProperties("theTagStats")
    private Tag tag;

    @ManyToOne
    @JsonIgnoreProperties("tagStats")
    private Asset theAsset;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCount() {
        return count;
    }

    public TagStats count(Long count) {
        this.count = count;
        return this;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Tag getTag() {
        return tag;
    }

    public TagStats tag(Tag tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Asset getTheAsset() {
        return theAsset;
    }

    public TagStats theAsset(Asset asset) {
        this.theAsset = asset;
        return this;
    }

    public void setTheAsset(Asset asset) {
        this.theAsset = asset;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagStats)) {
            return false;
        }
        return id != null && id.equals(((TagStats) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TagStats{" +
            "id=" + getId() +
            ", count=" + getCount() +
            "}";
    }
}
