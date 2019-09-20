package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;



public class EstimationDTO implements Serializable{

	private Integer oid;
	private Float value;
	private String unit;
	@IgnoreMapping
	private String metricDefinition;


	public EstimationDTO() {
		oid = null;
		value = null;
		unit = "";
		metricDefinition = null;
	}


	public Integer getOid() {
		return oid;
	}


	public void setOid(Integer oid) {
		this.oid = oid;
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
