package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

public class MetricDefinitionsDTO implements Serializable {
  private String[] nameMetricsDefinitions;
  private String[] typeMetrics;

  public MetricDefinitionsDTO() {
    this.nameMetricsDefinitions = new String[0];
    this.typeMetrics = new String[0];
  }

  public String[] getNameMetricsDefinitions() {
    return nameMetricsDefinitions;
  }

  public void setNameMetricsDefinitions(String[] nameMetricsDefinitions) {
    this.nameMetricsDefinitions = nameMetricsDefinitions;
  }

  public String[] getTypeMetrics() {
    return typeMetrics;
  }

  public void setTypeMetrics(String[] typeMetrics) {
    this.typeMetrics = typeMetrics;
  }
}
