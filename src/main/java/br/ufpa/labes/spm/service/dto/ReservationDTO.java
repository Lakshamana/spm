package br.ufpa.labes.spm.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Reservation} entity. */
public class ReservationDTO implements Serializable {

  private Long id;

  private LocalDate fromDate;

  private LocalDate toDate;

  private Long theExclusiveId;

  private Long theNormalId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getFromDate() {
    return fromDate;
  }

  public void setFromDate(LocalDate fromDate) {
    this.fromDate = fromDate;
  }

  public LocalDate getToDate() {
    return toDate;
  }

  public void setToDate(LocalDate toDate) {
    this.toDate = toDate;
  }

  public Long getTheExclusiveId() {
    return theExclusiveId;
  }

  public void setTheExclusiveId(Long exclusiveId) {
    this.theExclusiveId = exclusiveId;
  }

  public Long getTheNormalId() {
    return theNormalId;
  }

  public void setTheNormalId(Long normalId) {
    this.theNormalId = normalId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ReservationDTO reservationDTO = (ReservationDTO) o;
    if (reservationDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), reservationDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ReservationDTO{"
        + "id="
        + getId()
        + ", fromDate='"
        + getFromDate()
        + "'"
        + ", toDate='"
        + getToDate()
        + "'"
        + ", theExclusive="
        + getTheExclusiveId()
        + ", theNormal="
        + getTheNormalId()
        + "}";
  }
}
