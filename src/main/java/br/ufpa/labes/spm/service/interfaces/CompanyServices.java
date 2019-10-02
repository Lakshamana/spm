package br.ufpa.labes.spm.service.interfaces;

import br.ufpa.labes.spm.service.dto.CompaniesDTO;
import br.ufpa.labes.spm.service.dto.CompanyDTO;

public interface CompanyServices {
  public CompanyDTO saveCompany(CompanyDTO companyDTO);

  public CompaniesDTO getCompanies();

  public CompaniesDTO getCompanies(String searchTerm, String socialReason);

  public Boolean removeCompany(String ident);

  public Boolean alreadyExistCNPJ(String cnpj, String ident);

  public CompanyDTO getCompany();
}
