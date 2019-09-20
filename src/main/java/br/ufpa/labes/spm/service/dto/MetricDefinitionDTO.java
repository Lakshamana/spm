package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;


@SuppressWarnings("serial")
public class MetricDefinitionDTO implements Serializable{

	private Integer oid;


	private String name;


	private String description;


	private String kind;


	private Float rangeFrom;


	private Float rangeTo;


	private String howToMeasure;

	@IgnoreMapping
	private String metricType;


	@IgnoreMapping
	private List<MetricDTO> metric;

	@IgnoreMapping
	private List<EstimationDTO> estimation;

	@IgnoreMapping
	private List<String> units;


	public MetricDefinitionDTO() {

	}

	public Integer getOid() {
		return oid;
	}


	public void setOid(Integer oid) {
		this.oid = oid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getKind() {
		return kind;
	}


	public void setKind(String kind) {
		this.kind = kind;
	}


	public Float getRangeFrom() {
		return rangeFrom;
	}


	public void setRangeFrom(Float rangeFrom) {
		this.rangeFrom = rangeFrom;
	}


	public Float getRangeTo() {
		return rangeTo;
	}


	public void setRangeTo(Float rangeTo) {
		this.rangeTo = rangeTo;
	}


	public String getHowToMeasure() {
		return howToMeasure;
	}


	public void setHowToMeasure(String howToMeasure) {
		this.howToMeasure = howToMeasure;
	}


	public String getMetricType() {
		return metricType;
	}


	public void setMetricType(String metricType) {
		this.metricType = metricType;
	}


	public Collection<MetricDTO> getMetric() {
		return metric;
	}


	public void setMetric(List<MetricDTO> metric) {
		this.metric = metric;
	}


	public List<EstimationDTO> getEstimation() {
		return estimation;
	}


	public void setEstimation(List<EstimationDTO> estimation) {
		this.estimation = estimation;
	}


	public List<String> getUnits() {
		return units;
	}


	public void setUnits(List<String> units) {
		this.units = units;
	}



}
