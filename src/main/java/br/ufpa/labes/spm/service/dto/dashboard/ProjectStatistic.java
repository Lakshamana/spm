package br.ufpa.labes.spm.service.dto.dashboard;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.ufpa.labes.spm.service.dto.ProjectDTO;

@SuppressWarnings("serial")
@XmlRootElement(name = "projectStatistic")
public class ProjectStatistic implements Serializable {
  private ProjectDTO project;

  private Float finishedTasksPercentage;

  private Float delayedTasksPercentage;

  private ProjectCost cost;

  private Integer totalTasks;
  private Integer delayedTasks;
  private Integer finishedTasks;

  private Time estimatedDuration;
  private Time realDuration;

  public ProjectStatistic() {
    this.cost = new ProjectCost();
  }

  public void generateCostLink(Long id) {
    String url = id.toString();
  }

  public ProjectDTO getProject() {
    return project;
  }

  public void setProject(ProjectDTO project) {
    this.project = project;
  }

  @XmlElement(name = "finishedPercentage")
  public Float getFinishedTasksPercentage() {
    return finishedTasksPercentage;
  }

  public void setFinishedTasksPercentage(Float finishedTasksPercentage) {
    this.finishedTasksPercentage = finishedTasksPercentage;
  }

  @XmlElement(name = "delayedPercentage")
  public Float getDelayedTasksPercentage() {
    return delayedTasksPercentage;
  }

  public void setDelayedTasksPercentage(Float delayedTasksPercentage) {
    this.delayedTasksPercentage = delayedTasksPercentage;
  }

  @XmlElement(name = "realCost")
  public Double getProjectRealCost() {
    return cost.getRealCost();
  }

  public void setProjectRealCost(Double projectRealCost) {
    this.cost.setRealCost(projectRealCost);
  }

  @XmlElement(name = "estimatedCost")
  public Double getProjectEstimatedCost() {
    return cost.getEstimatedCost();
  }

  public void setProjectEstimatedCost(Double projectEstimatedCost) {
    this.cost.setEstimatedCost(projectEstimatedCost);
  }

  @XmlElement(name = "costLink")
  public String getProjectCost() {
    return cost.getLink();
  }

  public void setProjectCost(String costLink) {
    this.cost.setLink(costLink);
  }

  @XmlElement(name = "total")
  public Integer getTotalTasks() {
    return totalTasks;
  }

  public void setTotalTasks(Integer totalTasks) {
    this.totalTasks = totalTasks;
  }

  @XmlElement(name = "delayed")
  public Integer getDelayedTasks() {
    return delayedTasks;
  }

  public void setDelayedTasks(Integer delayedTasks) {
    this.delayedTasks = delayedTasks;
  }

  @XmlElement(name = "finished")
  public Integer getFinishedTasks() {
    return finishedTasks;
  }

  public void setFinishedTasks(Integer finishedTasks) {
    this.finishedTasks = finishedTasks;
  }

  public Time getEstimatedDuration() {
    return estimatedDuration;
  }

  public void setEstimatedDuration(Time estimatedDuration) {
    this.estimatedDuration = estimatedDuration;
  }

  public Time getRealDuration() {
    return realDuration;
  }

  public void setRealDuration(Time realDuration) {
    this.realDuration = realDuration;
  }

  @Override
  public String toString() {
    return "Finished tasks: "
        + this.finishedTasksPercentage
        + "%; Delayed tasks: "
        + this.delayedTasksPercentage
        + "%";
  }
}
