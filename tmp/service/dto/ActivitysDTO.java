package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ActivitysDTO implements Serializable {

	private List<ActivityDTO> activitys;

	public ActivitysDTO() {
		this.activitys = new ArrayList<ActivityDTO>();
	}

	public ActivitysDTO(List<ActivityDTO> activitys) {
		this.activitys = activitys;
	}

	public boolean addActivity(ActivityDTO activityDTO) {
		return this.activitys.add(activityDTO);
	}

	public boolean addActivitys(ActivitysDTO activitysDTO) {
		return this.activitys.addAll(activitysDTO.getActivitys());
	}

	public boolean isEmpty() {
		return this.activitys.isEmpty();
	}

	public boolean contains(ActivityDTO activityDTO) {
		return this.activitys.contains(activityDTO);
	}

	public boolean containsAll(ActivitysDTO activitysDTO) {
		return this.activitys.containsAll(activitysDTO.getActivitys());
	}

	public void clear() {
		this.activitys.clear();
	}

	public ActivityDTO get(int index) {
		return this.activitys.get(index);
	}

	public boolean remove(ActivityDTO activityDTO) {
		return this.activitys.remove(activityDTO);
	}

	public ActivityDTO remove(int index) {
		return this.activitys.remove(index);
	}

	public List<ActivityDTO> getActivitys() {
		return activitys;
	}

	@Override
	public String toString() {
		return activitys.toString();
	}
}
