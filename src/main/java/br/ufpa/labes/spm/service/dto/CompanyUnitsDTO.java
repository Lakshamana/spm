package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CompanyUnitsDTO implements Serializable{
	private List<CompanyUnitDTO> companyUnits;

	public CompanyUnitsDTO(){
		setCompanyUnits(new ArrayList<CompanyUnitDTO>());
	}

	public boolean addCompanyUnitDTO(CompanyUnitDTO companyUnitDTO) {
		return companyUnits.add(companyUnitDTO);
	}

	public List<CompanyUnitDTO> getCompanyUnits() {
		return companyUnits;
	}

	public void setCompanyUnits(List<CompanyUnitDTO> companyUnits) {
		this.companyUnits = companyUnits;
	}

	public int size(){
		return companyUnits.size();
	}
}
