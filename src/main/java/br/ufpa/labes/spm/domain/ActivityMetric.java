package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A ActivityMetric. */
@Entity
@Table(name = "activity_metric")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActivityMetric extends Metric implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JsonIgnoreProperties("theActivityMetrics")
  private Activity theActivity;

  @OneToOne(mappedBy = "theActivityMetricSub")
  @JsonIgnore
  private Metric theMetricSuper;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Activity getTheActivity() {
    return theActivity;
  }

  public ActivityMetric theActivity(Activity activity) {
    this.theActivity = activity;
    return this;
  }

  public void setTheActivity(Activity activity) {
    this.theActivity = activity;
  }

  public Metric getTheMetricSuper() {
    return theMetricSuper;
  }

  public ActivityMetric theMetricSuper(Metric metric) {
    this.theMetricSuper = metric;
    return this;
  }

  public void setTheMetricSuper(Metric metric) {
    this.theMetricSuper = metric;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ActivityMetric)) {
      return false;
    }
    return id != null && id.equals(((ActivityMetric) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "ActivityMetric{" + "id=" + getId() + "}";
  }
}
