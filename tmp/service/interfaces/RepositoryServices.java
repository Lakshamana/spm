package br.ufpa.labes.spm.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import org.qrconsult.spm.beans.ArtifactMngContent;
import org.qrconsult.spm.beans.ArtifactMngDownload;
import br.ufpa.labes.spm.service.dto.RepositoriesDTO;
import br.ufpa.labes.spm.service.dto.RepositoryDTO;
import br.ufpa.labes.spm.exceptions.WebapseeException;
import br.ufpa.labes.spm.domain.Repository;

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
