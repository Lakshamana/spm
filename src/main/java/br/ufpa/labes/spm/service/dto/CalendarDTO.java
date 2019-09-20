package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;




@SuppressWarnings("serial")
public class CalendarDTO implements Serializable{


	private Integer oid;


	private String name;


	private ArrayList<String> notWorkingDays;



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



	public ArrayList<String> getNotWorkingDays() {
		return notWorkingDays;
	}

	public void setNotWorkingDays(ArrayList<String> notWorkingDays) {
		this.notWorkingDays = notWorkingDays;
	}

}
