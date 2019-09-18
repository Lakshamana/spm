package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.ICompanyDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.ICompanyUnitDAO;
import org.qrconsult.spm.dtos.formOrganization.CompaniesDTO;
import org.qrconsult.spm.dtos.formOrganization.CompanyDTO;
import org.qrconsult.spm.dtos.formOrganization.CompanyUnitDTO;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.Company;
import br.ufpa.labes.spm.domain.CompanyUnit;
import br.ufpa.labes.spm.service.interfaces.CompanyServices;

@Stateless
public class CompanyServicesImpl implements CompanyServices{
	private static final int SINGLE_RESULT = 1;
	private static final String COMPANY_CLASS_NAME = Company.class.getSimpleName();
	private static final String COMPANY_UNIT_CLASS_NAME = CompanyUnit.class.getSimpleName();


	ICompanyDAO companyDAO;
	ICompanyUnitDAO companyUnitDAO;
	IAgentDAO agentDAO;

	Converter converter = new ConverterImpl();

	private Query query;

	@Override
	public CompanyDTO saveCompany(CompanyDTO companyDTO) {
		try {
			Company company = null;
			System.out.println("Unidades: " + companyDTO.getTheOrganizationalUnits());
			String hql = "SELECT o FROM " + COMPANY_CLASS_NAME + " o WHERE o.ident = '" + companyDTO.getIdent() + "'";
			query = companyDAO.getPersistenceContext().createQuery(hql);
			if(query.getResultList().size() == SINGLE_RESULT) {
				company = (Company) query.getSingleResult();
			}
			if(company == null) {
				company = (Company) converter.getEntity(companyDTO, Company.class);
				company.setTheOrganizationalUnits(converterUnidades(companyDTO.getTheOrganizationalUnits(), null, company));
				companyDAO.save(company);
			} else {
				for (CompanyUnit companyUnit : company.getTheOrganizationalUnits()) {
					companyUnitDAO.delete(companyUnit);
				}
				company.setTheOrganizationalUnits(new ArrayList<CompanyUnit>());
				company.setIdent(companyDTO.getIdent());
				company.setFantasyName(companyDTO.getFantasyName());
//				company.setCnpj(company.getCnpj());
				company.setUrl(companyDTO.getUrl());
				company.setPhone(companyDTO.getPhone());
				company.setSocialReason(companyDTO.getSocialReason());
				company.setAcronym(companyDTO.getAcronym());
				company.setAddress(companyDTO.getAddress());
				company.setDescription(companyDTO.getDescription());
				company.setTheOrganizationalUnits(converterUnidades(companyDTO.getTheOrganizationalUnits(), null, company));
			}
			companyDAO.update(company);
		} catch (ImplementationException e) {
			e.printStackTrace();
		}
		return companyDTO;
	}

	private List<CompanyUnit> converterUnidades(List<CompanyUnitDTO> unidadesDTO, CompanyUnit theCommand, Company company) throws ImplementationException {
		List<CompanyUnit> unidades = new ArrayList<CompanyUnit>();
		for (CompanyUnitDTO unidadeDTO : unidadesDTO) {

			CompanyUnit companyUnit = (CompanyUnit) converter.getEntity(unidadeDTO, CompanyUnit.class);
			companyUnit.setTheAgent(agentDAO.retrieveBySecondaryKey(unidadeDTO.getTheAgent()));
			companyUnit.setTheOrganization(company);
			if (unidadeDTO.getTheCommand() != null)
				companyUnit.setTheCommand(theCommand);
			for (String agentName : unidadeDTO.getTheUnitAgents()) {
				Agent agent = agentDAO.retrieveBySecondaryKey(agentName);
				agent.getTheOrgUnits().add(companyUnit);
				companyUnit.getTheUnitAgents().add(agent);
			}

			if (unidadeDTO.getTheSubordinates().size() != 0)
				companyUnit.setTheSubordinates(converterUnidades(unidadeDTO.getTheSubordinates(), companyUnit, company));
			unidades.add(companyUnit);
		}
		return unidades;
	}

	@Override
	public CompanyDTO getCompany() {
		String hql;
		hql = "select company from " + COMPANY_CLASS_NAME + " as company";
		query = companyDAO.getPersistenceContext().createQuery(hql);
		Company company = (Company)query.getSingleResult();
		return this.convertCompanyToCompanyDTO(company);
	}

	@Override
	public CompaniesDTO getCompanies() {
		String hql;
		hql = "select company from " + COMPANY_CLASS_NAME + " as company";
		query = companyDAO.getPersistenceContext().createQuery(hql);
		List<Company> companies = query.getResultList();
		return this.convertCompaniesToCompaniesDTO(companies);
	}

	private CompaniesDTO convertCompaniesToCompaniesDTO(List<Company> companiesList) {
		CompaniesDTO companiesDTOList = new CompaniesDTO(new ArrayList<CompanyDTO>());
		for(Company company : companiesList) {
			companiesDTOList.addCompanyDTO(this.convertCompanyToCompanyDTO(company));
		}
		return companiesDTOList;
	}

	private CompanyDTO convertCompanyToCompanyDTO(Company company) {
		CompanyDTO companyDTO = new CompanyDTO();
		try {
			companyDTO = (CompanyDTO) converter.getDTO(company, CompanyDTO.class);

			for (CompanyUnit companyUnit : company.getTheOrganizationalUnits()) {
				String hql = "SELECT o.theUnitAgents FROM " + COMPANY_UNIT_CLASS_NAME + " o WHERE o.ident = '" + companyUnit.getIdent() + "'";
				query = companyDAO.getPersistenceContext().createQuery(hql);
				List<Agent> agents = query.getResultList();

				CompanyUnitDTO comUnitDTO = (CompanyUnitDTO) converter.getDTO(companyUnit, CompanyUnitDTO.class);
				if (companyUnit.getTheAgent() != null)
					comUnitDTO.setTheAgent(companyUnit.getTheAgent().getName());

				for (Agent agent : agents) {
					comUnitDTO.getTheUnitAgents().add(agent.getName());
				}


				if (companyUnit.getTheCommand() != null)
					comUnitDTO.setTheCommand((CompanyUnitDTO) converter.getDTO(companyUnit.getTheCommand(), CompanyUnitDTO.class));
				companyDTO.getTheOrganizationalUnits().add(comUnitDTO);
			}

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return companyDTO;
	}

	@Override
	public CompaniesDTO getCompanies(String searchTerm, String socialReason) {
		String hql = "select company from " + COMPANY_CLASS_NAME + " as company where company.ident like :searchTerm";
		if(socialReason == null){
			query = companyDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("searchTerm", "%"+ searchTerm + "%");
		} else {
			hql += " and company.socialReason like :socialReason";
			query = companyDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("searchTerm", "%"+ searchTerm + "%");
			query.setParameter("socialReason", "%"+socialReason+"%");
		}

		List<Company> resultado = query.getResultList();
		buscar(searchTerm);
		CompaniesDTO companiesDTO = new CompaniesDTO(new ArrayList<CompanyDTO>());
		companiesDTO = convertCompaniesToCompaniesDTO(resultado);
		return companiesDTO;
	}

	@SuppressWarnings("unchecked")
	public List<CompanyUnit> buscar(String ident){
		String hql2 = "SELECT companyUnit FROM " + CompanyUnit.class.getName() + " AS companyUnit WHERE companyUnit.theOrganization.ident = :ident";
		query = companyDAO.getPersistenceContext().createQuery(hql2);
		query.setParameter("ident", ident);
		List<CompanyUnit> lista = query.getResultList();

		return  query.getResultList();
	}

	@Override
	public Boolean removeCompany(String ident) {
		Company company = companyDAO.retrieveBySecondaryKey(ident);
		if (company != null){
			companyDAO.delete(company);
			return true;
		}
		else return false;
	}

	@Override
	public Boolean alreadyExistCNPJ(String cnpj, String ident) {
		return companyDAO.alreadyExistCNPJ(cnpj, ident);
	}
}
