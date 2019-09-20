package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DriversDTO  implements Serializable {

	private List<DriverDTO> drivers = new ArrayList<DriverDTO>();



	public DriversDTO(List<DriverDTO> drivers) {
		this.drivers = drivers;
	}

	public boolean isEmpty(){
		if (drivers.size() == 0)
			return true;
		else return false;
	}

}
