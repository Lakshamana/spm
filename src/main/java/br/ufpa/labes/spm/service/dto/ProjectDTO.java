package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufpa.labes.spm.annotations.IgnoreMapping;

@SuppressWarnings("serial")
@XmlRootElement(name="project")
public class ProjectDTO implements Serializable {
	private Long id;
	private String ident;
	private String description;
	private String name;
	private LocalDate begin_date;
	private LocalDate end_date;
	private boolean active;

	@IgnoreMapping
	private String process;
	@IgnoreMapping
	private ProcessDTO processDTO;
	@IgnoreMapping
	private Integer estimatedHours;
	@IgnoreMapping
	private Integer estimatedMinutes;
	@IgnoreMapping
	private String theSystem;
	@IgnoreMapping
	private String processRefered;
	@IgnoreMapping
	private Collection<String> artifacts;
	@IgnoreMapping
	private Collection<String> agents;
	@IgnoreMapping
	private String pState;

	public ProjectDTO() {
//		this.begin_date = new LocalDate();
		this.artifacts = new ArrayList<String>();
		this.agents = new ArrayList<String>();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIdent() {
		return ident;
	}
	public void setIdent(String ident) {
		this.ident = ident;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(LocalDate begin_date) {
		this.begin_date = begin_date;
	}

	public String getBegin_dateString() {
		String date = "";
		if(begin_date != null)
			date = begin_date.getDayOfMonth() + "-" + begin_date.getMonth() + "-" + begin_date.getYear();

		return date;
	}

	public LocalDate getEnd_date() {
		return end_date;
	}

	public void setEnd_date(LocalDate end_date) {
		this.end_date = end_date;
	}

	public String getTheSystem() {
		return theSystem;
	}
	public void setTheSystem(String theSystem) {
		this.theSystem = theSystem;
	}
	public String getProcessRefered() {
		return processRefered;
	}
	public void setProcessRefered(String processRefered) {
		this.processRefered = processRefered;
	}
	public Collection<String> getArtifacts() {
		return artifacts;
	}
	public void setArtifacts(Collection<String> artifacts) {
		this.artifacts = artifacts;
	}

	public Collection<String> getAgents() {
		return agents;
	}

	public void setAgents(Collection<String> agents) {
		this.agents = agents;
	}

	public String getpState() {
		return pState;
	}

	public void setpState(String pState) {
		this.pState = pState;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ProcessDTO getProcessDTO() {
		return processDTO;
	}

	public void setProcessDTO(ProcessDTO processDTO) {
		this.processDTO = processDTO;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public Integer getEstimatedHours() {
		return estimatedHours;
	}

	public void setEstimatedHours(Integer estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public Integer getEstimatedMinutes() {
		return estimatedMinutes;
	}

	public void setEstimatedMinutes(Integer estimatedMinutes) {
		this.estimatedMinutes = estimatedMinutes;
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
		ProjectDTO other = (ProjectDTO) obj;
		if (ident == null) {
			if (other.ident != null)
				return false;
		} else if (!ident.equals(other.ident))
			return false;
		return true;
	}

}
