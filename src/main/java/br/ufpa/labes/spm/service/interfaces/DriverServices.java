package br.ufpa.labes.spm.service.interfaces;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.driver.DriverDTO;
import org.qrconsult.spm.dtos.formAgent.ConfigurationDTO;
import org.qrconsult.spm.dtos.formOrganization.CompanyDTO;

@Remote
public interface DriverServices {

public void saveDriver(DriverDTO driver, CompanyDTO companyOid);

public DriverDTO getDriverForCompany(Integer company_oid);

public DriverDTO updateConfiDriver(DriverDTO driverDTO, CompanyDTO companyDTO);

public String enviaArquivoGoogle(DriverDTO driverDTO, String projectName);

public boolean enviaArquivoGoogleDrive(DriverDTO driverDTO, String code);

}

