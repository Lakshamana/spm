package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;




@SuppressWarnings("serial")
public class CalendarDTO implements Serializable{


	private Long oid;


	private String name;


	private ArrayList<String> notWorkingDays;



	public Integer getId() {
		return oid;
	}

	public void setId(Long oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public ArrayList<String> getNotWorkingDays() {
		return notWorkingDays;
	}

	public void setNotWorkingDays(ArrayList<String> notWorkingDays) {
		this.notWorkingDays = notWorkingDays;
	}

}
