package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

import br.ufpa.labes.spm.annotations.IgnoreMapping;

public class EstimationDTO implements Serializable {

  private Long id;
  private Float value;
  private String unit;
  @IgnoreMapping private String metricDefinition;

  public EstimationDTO() {
    id = null;
    value = null;
    unit = "";
    metricDefinition = null;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Float getValue() {
    return value;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getMetricDefinition() {
    return metricDefinition;
  }

  public void setMetricDefinition(String metricDefinition) {
    this.metricDefinition = metricDefinition;
  }
}
