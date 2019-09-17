package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import br.ufpa.labes.spm.domain.enumeration.ConsumableStatus;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Consumable} entity. */
public class ConsumableDTO implements Serializable {

  private Long id;

  private String unit;

  private ConsumableStatus consumableStatus;

  private Float totalQuantity;

  private Float amountUsed;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public ConsumableStatus getConsumableStatus() {
    return consumableStatus;
  }

  public void setConsumableStatus(ConsumableStatus consumableStatus) {
    this.consumableStatus = consumableStatus;
  }

  public Float getTotalQuantity() {
    return totalQuantity;
  }

  public void setTotalQuantity(Float totalQuantity) {
    this.totalQuantity = totalQuantity;
  }

  public Float getAmountUsed() {
    return amountUsed;
  }

  public void setAmountUsed(Float amountUsed) {
    this.amountUsed = amountUsed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConsumableDTO consumableDTO = (ConsumableDTO) o;
    if (consumableDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), consumableDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ConsumableDTO{"
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
