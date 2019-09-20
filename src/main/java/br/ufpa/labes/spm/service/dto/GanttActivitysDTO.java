package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@SuppressWarnings("serial")
public class GanttActivitysDTO implements Serializable {
	private List<GanttActivityDTO> ganttActivitys;

	public GanttActivitysDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public List<GanttActivityDTO> getGanttActivitys() {
		if(ganttActivitys == null){
			ganttActivitys = new ArrayList<GanttActivityDTO>();
		}
		return ganttActivitys;
	}


	public void setGanttActivitys(List<GanttActivityDTO> ganttActivitys) {
		this.ganttActivitys = ganttActivitys;
	}


	public boolean addGanttActivity(GanttActivityDTO ganttActivityDTO) {
		return this.getGanttActivitys().add(ganttActivityDTO);
	}

	public boolean addGanttActivitysDTO(GanttActivitysDTO ganttActivitysDTO) {
		return this.getGanttActivitys().addAll(ganttActivitysDTO.getGanttActivitys());
	}

	public boolean removeGanttActivity(GanttActivityDTO GanttActivityDTO) {
		return this.ganttActivitys.remove(GanttActivityDTO);
	}

	public GanttActivityDTO getGanttActivityDTO(int index) {
		return this.ganttActivitys.get(index);
	}

	public boolean isEmpty() {
		return this.ganttActivitys.isEmpty();
	}

	public int size() {
		return this.ganttActivitys.size();
	}
}
