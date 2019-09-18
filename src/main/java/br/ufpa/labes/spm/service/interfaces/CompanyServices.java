package br.ufpa.labes.spm.service.interfaces;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.formOrganization.CompaniesDTO;
import org.qrconsult.spm.dtos.formOrganization.CompanyDTO;

@Remote
public interface CompanyServices {
	public CompanyDTO saveCompany(CompanyDTO companyDTO);
	public CompaniesDTO getCompanies();
	public CompaniesDTO getCompanies(String searchTerm, String socialReason);
	public Boolean removeCompany(String ident);
	public Boolean alreadyExistCNPJ(String cnpj, String ident);
	public CompanyDTO getCompany();
}
