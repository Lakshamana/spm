package br.ufpa.labes.spm.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Normal} entity. */
public class NormalDTO implements Serializable {

  private Long id;

  private Float howLong;

  private String howLongUnit;

  private LocalDate plannedBegin;

  private LocalDate plannedEnd;

  @Lob private String script;

  private Boolean delegable;

  private Boolean autoAllocable;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Float getHowLong() {
    return howLong;
  }

  public void setHowLong(Float howLong) {
    this.howLong = howLong;
  }

  public String getHowLongUnit() {
    return howLongUnit;
  }

  public void setHowLongUnit(String howLongUnit) {
    this.howLongUnit = howLongUnit;
  }

  public LocalDate getPlannedBegin() {
    return plannedBegin;
  }

  public void setPlannedBegin(LocalDate plannedBegin) {
    this.plannedBegin = plannedBegin;
  }

  public LocalDate getPlannedEnd() {
    return plannedEnd;
  }

  public void setPlannedEnd(LocalDate plannedEnd) {
    this.plannedEnd = plannedEnd;
  }

  public String getScript() {
    return script;
  }

  public void setScript(String script) {
    this.script = script;
  }

  public Boolean isDelegable() {
    return delegable;
  }

  public void setDelegable(Boolean delegable) {
    this.delegable = delegable;
  }

  public Boolean isAutoAllocable() {
    return autoAllocable;
  }

  public void setAutoAllocable(Boolean autoAllocable) {
    this.autoAllocable = autoAllocable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NormalDTO normalDTO = (NormalDTO) o;
    if (normalDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), normalDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "NormalDTO{"
        + "id="
        + getId()
        + ", howLong="
        + getHowLong()
        + ", howLongUnit='"
        + getHowLongUnit()
        + "'"
        + ", plannedBegin='"
        + getPlannedBegin()
        + "'"
        + ", plannedEnd='"
        + getPlannedEnd()
        + "'"
        + ", script='"
        + getScript()
        + "'"
        + ", delegable='"
        + isDelegable()
        + "'"
        + ", autoAllocable='"
        + isAutoAllocable()
        + "'"
        + "}";
  }
}
