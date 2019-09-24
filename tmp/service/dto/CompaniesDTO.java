package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CompaniesDTO implements Serializable{
	private List<CompanyDTO> companies;

	public CompaniesDTO(){}

	public CompaniesDTO(List<CompanyDTO> companies) {
		super();
		this.companies = companies;
	}

	public List<CompanyDTO> getCompanies() {
		return companies;
	}

	public void setCompanies(List<CompanyDTO> companies) {
		this.companies = companies;
	}

	public boolean addCompanyDTO(CompanyDTO companyDTO) {
		return companies.add(companyDTO);
	}

	public List<String> getCompaniesNames() {
		List<String> companiesNames = new ArrayList<String>();

		for (CompanyDTO companyDTO : companies) {
			companiesNames.add(companyDTO.getFantasyName());
		}

		return companiesNames;
	}

	public boolean isEmpty() {
		return this.companies.isEmpty();
	}

	public CompanyDTO getCompanyDTO(int index) {
		return companies.get(index);
	}
}
