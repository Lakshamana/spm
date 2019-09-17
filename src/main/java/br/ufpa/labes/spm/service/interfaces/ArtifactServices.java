package org.qrconsult.spm.services.interfaces;

import java.util.Map;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.agenda.SimpleArtifactDescriptorDTO;
import org.qrconsult.spm.dtos.formArtifacts.ArtifactDTO;
import org.qrconsult.spm.dtos.formArtifacts.ArtifactsDTO;
import org.qrconsult.spm.dtos.formTypes.TypesDTO;
import org.qrconsult.spm.exceptions.DAOException;

@Remote
public interface ArtifactServices {

	public ArtifactDTO getArtifact(String artifactIdent);
	
	public ArtifactsDTO getArtifacts();
	
	public ArtifactsDTO getArtifactsWithoutRelationship(boolean composicao);
	
	public ArtifactsDTO getArtifacts(String termoBusca, String domainFilter, String projectFilter, Boolean orgFilter);
	
	public ArtifactsDTO getArtifactsThatBelongsTo(String artifactName);
	
	public ArtifactsDTO getArtifactsDerivedFrom(String artifactName);
	
	public ArtifactDTO updateBelongsTo(String artifactName);
	
	public ArtifactDTO updateDerivedFrom(String artifactName);

	public ArtifactDTO saveArtifact(ArtifactDTO artifactDTO);
	
	public Boolean removeArtifact(ArtifactDTO artifactDTO);

	public TypesDTO getArtifactTypes();

	ArtifactDTO alreadyExist(String artifactIdent) throws DAOException;
	Map<String, SimpleArtifactDescriptorDTO[]> getArtifactsForSelectedActivity(String identActivity);
}
