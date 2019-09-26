package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A SimpleCon.
 */
@Entity
@Table(name = "simple_con")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SimpleCon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("fromSimpleCons")
    private Activity toActivity;

    @ManyToOne
    @JsonIgnoreProperties("toSimpleCons")
    private Activity fromActivity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getToActivity() {
        return toActivity;
    }

    public SimpleCon toActivity(Activity activity) {
        this.toActivity = activity;
        return this;
    }

    public void setToActivity(Activity activity) {
        this.toActivity = activity;
    }

    public Activity getFromActivity() {
        return fromActivity;
    }

    public SimpleCon fromActivity(Activity activity) {
        this.fromActivity = activity;
        return this;
    }

    public void setFromActivity(Activity activity) {
        this.fromActivity = activity;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleCon)) {
            return false;
        }
        return id != null && id.equals(((SimpleCon) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SimpleCon{" +
            "id=" + getId() +
            "}";
    }
}
