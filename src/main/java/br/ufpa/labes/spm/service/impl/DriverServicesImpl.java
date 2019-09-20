package br.ufpa.labes.spm.service.impl;


import java.io.IOException;
import java.util.Arrays;

import javax.persistence.Query;


import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.driver.IDriverDAO;
import br.ufpa.labes.spm.service.dto.DriverDTO;
import br.ufpa.labes.spm.service.dto.SpmConfigurationDTO;
import br.ufpa.labes.spm.service.dto.CompanyDTO;
import br.ufpa.labes.spm.domain.Driver;
import br.ufpa.labes.spm.domain.Company;
import br.ufpa.labes.spm.service.interfaces.DriverServices;

//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
//import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
//import com.google.api.client.http.FileContent;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson.JacksonFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//import com.google.api.services.drive.model.File;


public class DriverServicesImpl  implements DriverServices {

   IDriverDAO driverDAO;
	private Query query;
	Converter converter = new ConverterImpl();
	private static final String DRIVER_CLASSNAME = Driver.class.getName();
    private Driver driver;
   private Company company;
 //  private GoogleAuthorizationCodeFlow flow;
  // private  HttpTransport httpTransport;
   //private JsonFactory jsonFactory;

	@Override
	public void saveDriver(DriverDTO driverDTO, CompanyDTO companyDTO) {

		company = this.convertCompanyDTOToCompany(companyDTO);
		driver =  new  Driver();
		try {

			driver = this.convertDriverDTOToDriver(driverDTO);
			driver.setCompany(company);
			driverDAO.daoSave(driver);
			driver = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public DriverDTO getDriverForCompany(Integer company_oid) {
		System.out.println("chegou no company");
		query = driverDAO
				.getPersistenceContext()
				.createQuery("SELECT driver FROM "
								+ DRIVER_CLASSNAME
								+ " AS driver "
								+ "WHERE driver.company.oid = :company_oid");

		query.setParameter("company_oid", company_oid);
		System.out.println("o driver é"+convertDriverToDriverDTO((Driver)query.getSingleResult()));
		return convertDriverToDriverDTO((Driver)query.getSingleResult());
	}

	private Driver convertDriverDTOToDriver(DriverDTO driverDTO) {
		try {

			Driver driver = new Driver();
			driver = (Driver) converter.getEntity(driverDTO, driver);

			return driver;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Company convertCompanyDTOToCompany(CompanyDTO companyDTO) {
		try {

			Company company = new Company();
			company = (Company) converter.getEntity(companyDTO, company);

			return company;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

	private DriverDTO convertDriverToDriverDTO(Driver driver) {
		try {
			DriverDTO driverDTO = new DriverDTO();
			driverDTO = (DriverDTO) converter.getDTO(driver, DriverDTO.class);

			return driverDTO;
		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public DriverDTO updateConfiDriver(DriverDTO driverDTO,CompanyDTO companyDTO) {
		company = this.convertCompanyDTOToCompany(companyDTO);
		driver = new Driver();

		try {

			driver = this.convertDriverDTOToDriver(driverDTO);
			driver.setCompany(company);
			driverDAO.update(driver);
			driver = null;
		} catch (Exception e) {
			e.printStackTrace();
		}


		return driverDTO;
	}


//	public String enviaArquivoGoogle(DriverDTO driverDTO, String projectName){

	//	  httpTransport = new NetHttpTransport();
	  //    jsonFactory = new JacksonFactory();

	    //  flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,driverDTO.getAppKeyGoogle(), driverDTO.getAppSecretGoogle(), Arrays.asList(DriveScopes.DRIVE)).setAccessType("online").setApprovalPrompt("auto").build();

	      //String autorizacao = flow.newAuthorizationUrl().setRedirectUri(driverDTO.getRequestUrl()).build();

	//	return autorizacao;




	//}

	public boolean enviaArquivoGoogleDrive(DriverDTO driverDTO, String code){
	return true;



	}

	@Override
	public String enviaArquivoGoogle(DriverDTO driverDTO, String projectName) {
		// TODO Auto-generated method stub
		return null;
	}


	}

