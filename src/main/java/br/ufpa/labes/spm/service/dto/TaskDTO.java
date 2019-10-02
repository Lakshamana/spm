package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.ufpa.labes.spm.annotations.IgnoreMapping;
import br.ufpa.labes.spm.service.dto.dashboard.Time;

@SuppressWarnings("serial")
@XmlRootElement(name = "task")
public class TaskDTO implements Serializable {
  private Long id; //

  private String name;

  private String localState;

  private LocalDate beginDate;

  private LocalDate endDate;

  private Float workingHours;

  private Time estimatedTime;

  private Time realWorkingTime;

  private LocalDate dateDelegatedTo;

  private LocalDate dateDelegatedFrom;

  @IgnoreMapping private Float howLong;

  @IgnoreMapping private String howLongUnit;

  @IgnoreMapping private LocalDate plannedBegin;

  @IgnoreMapping private LocalDate plannedEnd;

  @IgnoreMapping private String script;

  @IgnoreMapping private String theNormal;

  @IgnoreMapping private String agent;

  //	@IgnoreMapping
  //	private Integer estimatedHours;

  //	@IgnoreMapping
  //	private Integer estimatedMinutes;

  public TaskDTO() {}

  public TaskDTO(
      Long id,
      String name,
      String localState,
      LocalDate beginDate,
      LocalDate endDate,
      Float workingHours,
      LocalDate dateDelegatedTo,
      LocalDate dateDelegatedFrom,
      String theNormal) {
    this.id = id;
    this.name = name;
    ;
    this.localState = localState;
    this.beginDate = beginDate;
    this.endDate = endDate;
    this.workingHours = workingHours;
    this.dateDelegatedTo = dateDelegatedTo;
    this.dateDelegatedFrom = dateDelegatedFrom;
    this.theNormal = theNormal;
  }

  public TaskDTO(
      Long id,
      String name,
      String localState,
      LocalDate beginDate,
      LocalDate endDate,
      Float workingHours,
      LocalDate dateDelegatedTo,
      LocalDate dateDelegatedFrom,
      Float howLong,
      String howLongUnit,
      LocalDate plannedBegin,
      LocalDate plannedEnd,
      String script,
      String theNormal,
      String agent) {
    this.id = id;
    this.name = name;
    this.localState = localState;
    this.beginDate = beginDate;
    this.endDate = endDate;
    this.workingHours = workingHours;
    this.dateDelegatedTo = dateDelegatedTo;
    this.dateDelegatedFrom = dateDelegatedFrom;
    this.script = script;
    this.theNormal = theNormal;
    this.agent = agent;
    this.howLong = howLong;
    this.howLongUnit = howLongUnit;
    this.plannedBegin = plannedBegin;
    this.plannedEnd = plannedEnd;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocalState() {
    return localState;
  }

  public void setLocalState(String localState) {
    this.localState = localState;
  }

  public LocalDate getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(LocalDate beginDate) {
    this.beginDate = beginDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public Float getWorkingHours() {
    return workingHours;
  }

  public void setWorkingHours(Float workingHours) {
    this.workingHours = workingHours;
  }

  public LocalDate getDateDelegatedTo() {
    return dateDelegatedTo;
  }

  public void setDateDelegatedTo(LocalDate dateDelegatedTo) {

    this.dateDelegatedTo = dateDelegatedTo;
  }

  public LocalDate getDateDelegatedFrom() {
    return dateDelegatedFrom;
  }

  public void setDateDelegatedFrom(LocalDate dateDelegatedFrom) {
    this.dateDelegatedFrom = dateDelegatedFrom;
  }

  public String getTheNormal() {
    return theNormal;
  }

  public void setTheNormal(String theNormal) {
    this.theNormal = theNormal;
  }

  public String getScript() {
    return script;
  }

  public void setScript(String script) {
    this.script = script;
  }

  @XmlTransient
  public String getAgent() {
    return agent;
  }

  public void setAgent(String agent) {
    this.agent = agent;
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

  //	@XmlTransient
  //	public Integer getEstimatedHours() {
  //		return estimatedHours;
  //	}
  //
  //	public void setEstimatedHours(Integer estimatedHours) {
  //		this.estimatedHours = estimatedHours;
  //	}
  //
  //	@XmlTransient
  //	public Integer getEstimatedMinutes() {
  //		return estimatedMinutes;
  //	}
  //
  //	public void setEstimatedMinutes(Integer estimatedMinutes) {
  //		this.estimatedMinutes = estimatedMinutes;
  //	}

  public Time getEstimatedTime() {
    return estimatedTime;
  }

  public void setEstimatedTime(Time estimatedTime) {
    this.estimatedTime = estimatedTime;
  }

  public Time getRealWorkingTime() {
    return realWorkingTime;
  }

  public void setRealWorkingTime(Time realWorkingTime) {
    this.realWorkingTime = realWorkingTime;
  }

  @Override
  public String toString() {
    return super.toString();

    //		String beginDate = this.beginDate != null
    //		? this.beginDate.toString() : "null";
    //
    //		String sdateDelegatedFrom = this.dateDelegatedFrom != null
    //		? this.dateDelegatedFrom.toString() : "null";
    //
    //		String sdateDelegatedTo = this.dateDelegatedTo != null
    //		? this.dateDelegatedTo.toString() : "null";
    //
    //		String endDate = this.endDate != null
    //		? this.endDate.toString() : "null";
    //
    //		String actid = this.theNormal != null
    //		? this.theNormal : "null";
    //
    //		String s = "+Task : --> LocalState: " + this.localState + "|beginDate : "
    //		+ beginDate + "| endDate : " + endDate + "| dateDelegatedFrom"
    //		+ sdateDelegatedFrom + "| dateDelegatedTo : "
    //		+ sdateDelegatedTo + " | workingHours : " + this.workingHours
    //		+ "| Activity : ID :"+actid;
    //
    //		return (s);

  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    TaskDTO other = (TaskDTO) obj;
    if (name == null) {
      if (other.name != null) return false;
    } else if (!name.equals(other.name)) return false;
    return true;
  }
}
