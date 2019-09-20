
package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ArtifactsDTO implements Serializable {
	private List<ArtifactDTO> artifacts;

	public ArtifactsDTO() {

	}

	public ArtifactsDTO(List<ArtifactDTO> artifacts) {
		this.artifacts = artifacts;
	}

	public boolean addArtifactDTO(ArtifactDTO artifactDTO) {
		return artifacts.add(artifactDTO);
	}

	public boolean addArtifactsDTO(List<ArtifactDTO> artifactsDTO) {
		return artifacts.addAll(artifactsDTO);
	}

	public boolean removeArtifactDTO(ArtifactDTO artifactDTO) {
		return artifacts.remove(artifactDTO);
	}

	public ArtifactDTO getArtifactDTO(int index) {
		return artifacts.get(index);
	}

	public List<String> getArtifactsNames() {
		List<String> artifactsNames = new ArrayList<String>();

		for(ArtifactDTO artifactDTO : artifacts) {
			artifactsNames.add(artifactDTO.getName());
		}

		return artifactsNames;
	}

	public List<String> getArtifactsIdents() {
		List<String> artifactsIdents = new ArrayList<String>();

		for(ArtifactDTO artifactDTO : artifacts) {
			artifactsIdents.add(artifactDTO.getIdent());
		}

		return artifactsIdents;
	}

	public boolean isEmpty() {
		return this.artifacts.isEmpty();
	}

	public int size() {
		return this.artifacts.size();
	}
}
