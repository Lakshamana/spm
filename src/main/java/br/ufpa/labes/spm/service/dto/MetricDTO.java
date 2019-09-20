package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;



public class MetricDTO implements Serializable{



	private Integer oid;


	private Float value;


	private String unit;


	private Date periodBegin;


	private Date periodEnd;

	@IgnoreMapping
	private String metricDefinition;


	private Integer index;


	public MetricDTO() {
		oid = null;
		value = null;
		unit = "";

		metricDefinition = "";
	}


	public Integer getOid() {
		return oid;
	}


	public void setOid(Integer oid) {
		this.oid = oid;
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
