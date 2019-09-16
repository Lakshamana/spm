package br.ufpa.labes.spm.service.dto.dashboard;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement(name = "cost")
public class ProjectCost implements Serializable {

  private Double realCost;

  private Double estimatedCost;

  private String link;

  public ProjectCost() {
    realCost = new Double(0);
    estimatedCost = new Double(0);
    link = "";
  }

  public void addRealCostValue(float value) {
    this.addValue(this.realCost, value);
  }

  public void addEstimatedCostValue(float value) {
    this.addValue(this.estimatedCost, value);
  }

  public void addRealCostValue(double value) {
    this.addValue(this.realCost, value);
  }

  public void addEstimatedCostValue(double value) {
    this.addValue(this.estimatedCost, value);
  }

  public void addValue(Double cost, Float value) {
    cost += value;
  }

  public void addValue(Double cost, double value) {
    cost += (new Float(value));
  }

  public Double getRealCost() {
    return realCost;
  }

  public void setRealCost(Double realCost) {
    this.realCost = realCost;
  }

  public Double getEstimatedCost() {
    return estimatedCost;
  }

  public void setEstimatedCost(Double estimatedCost) {
    this.estimatedCost = estimatedCost;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  @Override
  public String toString() {
    return "Estimated Cost: " + this.estimatedCost + "; Real Cost: " + this.realCost;
  }
}
