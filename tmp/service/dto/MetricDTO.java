package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Date;

import br.ufpa.labes.spm.annotations.IgnoreMapping;



public class MetricDTO implements Serializable{



	private Long id;


	private Float value;


	private String unit;


	private Date periodBegin;


	private Date periodEnd;

	@IgnoreMapping
	private String metricDefinition;


	private Integer index;


	public MetricDTO() {
		id = null;
		value = null;
		unit = "";

		metricDefinition = "";
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public float getValue() {
		return value;
	}


	public void setValue(float value) {
		this.value = value;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public Date getPeriodBegin() {
		return periodBegin;
	}


	public void setPeriodBegin(Date periodBegin) {
		this.periodBegin = periodBegin;
	}


	public Date getPeriodEnd() {
		return periodEnd;
	}


	public void setPeriodEnd(Date periodEnd) {
		this.periodEnd = periodEnd;
	}


	public String getMetricDefinition() {
		return metricDefinition;
	}


	public void setMetricDefinition(String metricDefinition) {
		this.metricDefinition = metricDefinition;
	}


	public Integer getIndex() {
		return index;
	}


	public void setIndex(Integer index) {
		this.index = index;
	}



}
