package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A PrimitiveParam.
 */
@Entity
@Table(name = "primitive_param")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PrimitiveParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "thePrimitiveParamSub")
    @JsonIgnore
    private Parameter theParameterSuper;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parameter getTheParameterSuper() {
        return theParameterSuper;
    }

    public PrimitiveParam theParameterSuper(Parameter parameter) {
        this.theParameterSuper = parameter;
        return this;
    }

    public void setTheParameterSuper(Parameter parameter) {
        this.theParameterSuper = parameter;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrimitiveParam)) {
            return false;
        }
        return id != null && id.equals(((PrimitiveParam) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PrimitiveParam{" +
            "id=" + getId() +
            "}";
    }
}
