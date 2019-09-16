package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A SimpleCon. */
@Entity
@Table(name = "simple_con")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy = InheritanceType.JOINED)
public class SimpleCon extends Connection implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(unique = true)
  private Feedback theFeedbackSub;

  @OneToOne
  @JoinColumn(unique = true)
  private Sequence theSequenceSub;

  @OneToOne(mappedBy = "theSimpleConSub")
  @JsonIgnore
  private Connection theConnectionSuper;

  @ManyToOne
  @JsonIgnoreProperties("fromSimpleCons")
  private Activity fromActivity;

  @ManyToOne
  @JsonIgnoreProperties("toSimpleCons")
  private Activity toActivity;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Feedback getTheFeedbackSub() {
    return theFeedbackSub;
  }

  public SimpleCon theFeedbackSub(Feedback feedback) {
    this.theFeedbackSub = feedback;
    return this;
  }

  public void setTheFeedbackSub(Feedback feedback) {
    this.theFeedbackSub = feedback;
  }

  public Sequence getTheSequenceSub() {
    return theSequenceSub;
  }

  public SimpleCon theSequenceSub(Sequence sequence) {
    this.theSequenceSub = sequence;
    return this;
  }

  public void setTheSequenceSub(Sequence sequence) {
    this.theSequenceSub = sequence;
  }

  public Connection getTheConnectionSuper() {
    return theConnectionSuper;
  }

  public SimpleCon theConnectionSuper(Connection connection) {
    this.theConnectionSuper = connection;
    return this;
  }

  public void setTheConnectionSuper(Connection connection) {
    this.theConnectionSuper = connection;
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
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

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
    return "SimpleCon{" + "id=" + getId() + "}";
  }
}
