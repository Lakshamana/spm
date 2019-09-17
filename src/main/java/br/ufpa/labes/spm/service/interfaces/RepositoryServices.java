package org.qrconsult.spm.services.interfaces;

import java.util.List;

import javax.ejb.Remote;

import org.qrconsult.spm.beans.ArtifactMngContent;
import org.qrconsult.spm.beans.ArtifactMngDownload;
import org.qrconsult.spm.dtos.formRepositorios.RepositoriesDTO;
import org.qrconsult.spm.dtos.formRepositorios.RepositoryDTO;
import org.qrconsult.spm.exceptions.WebapseeException;
import org.qrconsult.spm.model.organizationPolicies.Repository;

@Remote
public interface RepositoryServices {
	public RepositoryDTO Salvar(RepositoryDTO repos);
	public Boolean remover(String ident);
	public RepositoriesDTO getRepositories();
	public List<RepositoryDTO> getRepositorys();
	public RepositoryDTO getRepository(String ident);
	public RepositoryDTO getRepos(String ident);
	public Boolean removerStructure(String ident);
	String performImport(String username, String activityIdent, String artifactIdent, String comment, ArtifactMngContent artifactContent, String repositoryIdent,String path) throws Exception;
	ArtifactMngDownload[] performCheckout(String username, String artifactIdent, String arguments) throws WebapseeException;
	boolean verifyUploadOk(String artifact_id, String normal_id);
	Repository getRepositoryByArtifact(String artifactIdent);
	String getRepositoryToImport(String processIdent, String artifactIdent);
	RepositoryDTO getRepositoryDTOByArtifact(String artifactIdent);
}
