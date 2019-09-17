package br.ufpa.labes.spm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import br.ufpa.labes.spm.domain.enumeration.ConsumableStatus;

/** A Consumable. */
@Entity
@Table(name = "consumable")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Consumable extends Resource implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "unit")
  private String unit;

  @Enumerated(EnumType.STRING)
  @Column(name = "consumable_status")
  private ConsumableStatus consumableStatus;

  @Column(name = "total_quantity")
  private Float totalQuantity;

  @Column(name = "amount_used")
  private Float amountUsed;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUnit() {
    return unit;
  }

  public Consumable unit(String unit) {
    this.unit = unit;
    return this;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public ConsumableStatus getConsumableStatus() {
    return consumableStatus;
  }

  public Consumable consumableStatus(ConsumableStatus consumableStatus) {
    this.consumableStatus = consumableStatus;
    return this;
  }

  public void setConsumableStatus(ConsumableStatus consumableStatus) {
    this.consumableStatus = consumableStatus;
  }

  public Float getTotalQuantity() {
    return totalQuantity;
  }

  public Consumable totalQuantity(Float totalQuantity) {
    this.totalQuantity = totalQuantity;
    return this;
  }

  public void setTotalQuantity(Float totalQuantity) {
    this.totalQuantity = totalQuantity;
  }

  public Float getAmountUsed() {
    return amountUsed;
  }

  public Consumable amountUsed(Float amountUsed) {
    this.amountUsed = amountUsed;
    return this;
  }

  public void setAmountUsed(Float amountUsed) {
    this.amountUsed = amountUsed;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Consumable)) {
      return false;
    }
    return id != null && id.equals(((Consumable) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Consumable{"
        + "id="
        + getId()
        + ", unit='"
        + getUnit()
        + "'"
        + ", consumableStatus='"
        + getConsumableStatus()
        + "'"
        + ", totalQuantity="
        + getTotalQuantity()
        + ", amountUsed="
        + getAmountUsed()
        + "}";
  }
}
