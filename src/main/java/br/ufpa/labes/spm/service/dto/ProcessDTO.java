package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.List;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;
import br.ufpa.labes.spm.service.dto.ActivitysDTO;

@SuppressWarnings("serial")
public class ProcessDTO implements Serializable{
	private Integer oid;
	private String ident;
	private String pState;
	@IgnoreMapping
	private List<String> tasks;
	@IgnoreMapping
	private List<String> tasksIdents;
	@IgnoreMapping
	private ActivitysDTO activitys;

	public ProcessDTO() {}

	public ProcessDTO(String ident, String pState, List<String> tasks) {
		this.ident = ident;
		this.pState = pState;
		this.tasks = tasks;
	}

	public Integer getId() {
		return oid;
	}

	public void setId\(Long oid) {
		this.oid = oid;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getPState() {
		return pState;
	}

	public void setPState(String pState) {
		this.pState = pState;
	}

	public List<String> getTasks() {
		return tasks;
	}

	public void setTasks(List<String> tasks) {
		this.tasks = tasks;
	}

	public ActivitysDTO getActivitys() {
		return activitys;
	}

	public void setActivitys(ActivitysDTO activitys) {
		this.activitys = activitys;
	}

	public List<String> getTasksIdents() {
		return tasksIdents;
	}

	public void setTasksIdents(List<String> tasksIdents) {
		this.tasksIdents = tasksIdents;
	}

	@Override
	public String toString() {
		return "Name: " + this.ident + "; activitys: " + tasks.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ident == null) ? 0 : ident.hashCode());
		result = prime * result + ((pState == null) ? 0 : pState.hashCode());
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
		ProcessDTO other = (ProcessDTO) obj;
		if (ident == null) {
			if (other.ident != null)
				return false;
		} else if (!ident.equals(other.ident))
			return false;
		if (pState == null) {
			if (other.pState != null)
				return false;
		} else if (!pState.equals(other.pState))
			return false;
		return true;
	}


}
