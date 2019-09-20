package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;
import br.ufpa.labes.spm.service.dto.ProjectDTO;
import br.ufpa.labes.spm.service.dto.SimpleUpdateDTO;

@SuppressWarnings("serial")
public class ProjectCopyDTO implements Serializable {

	private String oldIdent;
	private String newIdent;

	@IgnoreMapping
	private Collection<SimpleUpdateDTO> artifacts;
	@IgnoreMapping
	private boolean reuse;

	public ProjectCopyDTO() {}

	public ProjectCopyDTO(String copyName, ProjectDTO projectDTO) {
		copyFromProject(copyName, projectDTO);
		this.artifacts = Collections.emptyList();
		this.reuse = true;
	}

	public ProjectCopyDTO(String copyName, ProjectDTO projectDTO, List<SimpleUpdateDTO> artifacts) {
		copyFromProject(copyName, projectDTO);
		this.artifacts = artifacts;
		this.reuse = false;
	}

	private void copyFromProject(String copyName, ProjectDTO projectDTO) {
		this.newIdent = copyName;
		this.oldIdent = projectDTO.getIdent();
	}

	public String getOldIdent() {
		return oldIdent;
	}

	public void setOldIdent(String oldIdent) {
		this.oldIdent = oldIdent;
	}

	public String getNewIdent() {
		return newIdent;
	}

	public void setNewIdent(String newIdent) {
		this.newIdent = newIdent;
	}

	public boolean isReuse() {
		return reuse;
	}

	public void setReuse(boolean reuse) {
		this.reuse = reuse;
	}

	public Collection<SimpleUpdateDTO> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(Collection<SimpleUpdateDTO> artifacts) {
		this.artifacts = artifacts;
	}

	public SimpleUpdateDTO[] getSimpleUpdateDTOArray() {
		SimpleUpdateDTO[] toArray = new SimpleUpdateDTO[this.artifacts.size()];

		return this.artifacts.toArray(toArray);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((newIdent == null) ? 0 : newIdent.hashCode());
		result = prime * result
				+ ((oldIdent == null) ? 0 : oldIdent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectCopyDTO other = (ProjectCopyDTO) obj;
		if (newIdent == null) {
			if (other.newIdent != null)
				return false;
		} else if (!newIdent.equals(other.newIdent))
			return false;
		if (oldIdent == null) {
			if (other.oldIdent != null)
				return false;
		} else if (!oldIdent.equals(other.oldIdent))
			return false;
		return true;
	}

}
