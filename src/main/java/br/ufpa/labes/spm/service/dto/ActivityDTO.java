package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import br.ufpa.labes.spm.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class ActivityDTO implements Serializable {

	private String ident;
	private String state;
	private Double hoursWorked;
	private Date beginDate;
	private Date endDate;

	private String agentIdent;
	private String agentName;
	private String name;
	private String percentCompleted;
	private String duration;
	private String script;
	private String plannedBegin;
	private String plannedEnd;
	private String actualBegin;
	private String actualEnd;
	private boolean isDelegable;
	private Long id;
	private String parent;

	@IgnoreMapping
	private List<String> reqAgents;

	public ActivityDTO() {
		this.hoursWorked = 0.0;
		this.beginDate = new Date();
		this.endDate = new Date();
		this.reqAgents = new ArrayList<String>();
	}

	public ActivityDTO(String ident, String state, Double hoursWorked,
			Date beginDate, Date endDate) {
		this.ident = ident;
		this.state = state;
		this.hoursWorked = hoursWorked;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.reqAgents = new ArrayList<String>();
	}

	public ActivityDTO(String name, String ident, String state, Double hoursWorked,
			Date beginDate, Date endDate) {
		this.name = name;
		this.ident = ident;
		this.state = state;
		this.hoursWorked = hoursWorked;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.reqAgents = new ArrayList<String>();
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Double getHoursWorked() {
		return hoursWorked;
	}

	public void setHoursWorked(Double hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getAgentIdent() {
		return agentIdent;
	}

	public void setAgentIdent(String agentIdent) {
		this.agentIdent = agentIdent;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPercentCompleted() {
		return percentCompleted;
	}

	public void setPercentCompleted(String percentCompleted) {
		this.percentCompleted = percentCompleted;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getPlannedBegin() {
		return plannedBegin;
	}

	public void setPlannedBegin(String plannedBegin) {
		this.plannedBegin = plannedBegin;
	}

	public String getPlannedEnd() {
		return plannedEnd;
	}

	public void setPlannedEnd(String plannedEnd) {
		this.plannedEnd = plannedEnd;
	}

	public String getActualBegin() {
		return actualBegin;
	}

	public void setActualBegin(String actualBegin) {
		this.actualBegin = actualBegin;
	}

	public String getActualEnd() {
		return actualEnd;
	}

	public void setActualEnd(String actualEnd) {
		this.actualEnd = actualEnd;
	}

	public boolean isDelegable() {
		return isDelegable;
	}

	public void setDelegable(boolean isDelegable) {
		this.isDelegable = isDelegable;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	@XmlTransient
	public List<String> getReqAgents() {
		return reqAgents;
	}

	public void setReqAgents(List<String> reqAgents) {
		this.reqAgents = reqAgents;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ident == null) ? 0 : ident.hashCode());
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
		ActivityDTO other = (ActivityDTO) obj;
		if (ident == null) {
			if (other.ident != null)
				return false;
		} else if (!ident.equals(other.ident))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "agentIdent:" + agentIdent
				+ ", agentName:" + agentName + ", ident:" + ident + ", name:"
				+ name + ", state:" + state + ", percentCompleted:"
				+ percentCompleted + ", duration:" + duration + ", script:"
				+ script + ", plannedBegin:" + plannedBegin + ", plannedEnd:"
				+ plannedEnd + ", actualBegin:" + actualBegin + ", actualEnd:"
				+ actualEnd + ", isDelegable:" + isDelegable + "";
	}
}
