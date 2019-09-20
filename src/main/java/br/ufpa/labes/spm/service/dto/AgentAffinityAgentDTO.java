package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class AgentAffinityAgentDTO implements Serializable {

	private Integer oid;

	private Integer degree;
	@IgnoreMapping
	private String toAffinity;
	@IgnoreMapping
	private String fromAffinity;

	public AgentAffinityAgentDTO() {}

	public AgentAffinityAgentDTO(Integer degree, String toAffinity, String fromAffinity) {
		this.degree = degree;
		this.toAffinity = toAffinity;
		this.fromAffinity = fromAffinity;
	}

	public Integer getId() {
		return oid;
	}

	public void setId\(Long oid) {
		this.oid = oid;
	}

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public String getToAffinity() {
		return toAffinity;
	}

	public void setToAffinity(String toAffinity) {
		this.toAffinity = toAffinity;
	}

	public String getFromAffinity() {
		return fromAffinity;
	}

	public void setFromAffinity(String fromAffinity) {
		this.fromAffinity = fromAffinity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fromAffinity == null) ? 0 : fromAffinity.hashCode());
		result = prime * result
				+ ((toAffinity == null) ? 0 : toAffinity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgentAffinityAgentDTO other = (AgentAffinityAgentDTO) obj;
		if (fromAffinity == null) {
			if (other.fromAffinity != null)
				return false;
		} else if (!fromAffinity.equals(other.fromAffinity))
			return false;
		if (toAffinity == null) {
			if (other.toAffinity != null)
				return false;
		} else if (!toAffinity.equals(other.toAffinity))
			return false;
		return true;
	}


}
