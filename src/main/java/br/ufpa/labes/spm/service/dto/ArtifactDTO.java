package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class ArtifactDTO implements Serializable {

	private Long oid;
	private String ident;
	private String description;
	private String name;
	private Boolean isTemplate;
	private Boolean isActive;
	private String pathName;
	private String fileName;
	@IgnoreMapping
	private String localVersion;
	@IgnoreMapping
	private String remoteVersion;
	@IgnoreMapping
	private String theArtifactType;
	@IgnoreMapping
	private Collection<String> possess;
	@IgnoreMapping
	private List<String> derivedTo;

	public ArtifactDTO() {
		this.oid = null;
		this.ident = "";
		this.description = "";
		this.name = "";
		this.pathName = "";
		this.possess = new ArrayList<String>();
		this.derivedTo = new ArrayList<String>();
	}

	public Integer getId() {
		return oid;
	}
	public void setId(Long oid) {
		this.oid = oid;
	}
	public String getIdent() {
		return ident;
	}
	public void setIdent(String ident) {
		this.ident = ident;
	}
	public String getRemoteVersion() {
		return remoteVersion;
	}
	public void setRemoteVersion(String remoteVersion) {
		this.remoteVersion = remoteVersion;
	}

	public String getLocalVersion() {
		return localVersion;
	}
	public void setLocalVersion(String localVersion) {
		this.localVersion = localVersion;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public Boolean isIsTemplate() {
		return isTemplate;
	}

	public void setIsTemplate(Boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	public Boolean isIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Collection<String> getPossess() {
		return possess;
	}

	public void setPossess(Collection<String> possess) {
		this.possess = possess;
	}

	public Collection<String> getDerivedTo() {
		return derivedTo;
	}

	public void setDerivedTo(List<String> derivedTo) {
		this.derivedTo = derivedTo;
	}

	public String getTheArtifactType() {
		return theArtifactType;
	}

	public void setTheArtifactType(String theArtifactType) {
		this.theArtifactType = theArtifactType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((theArtifactType == null) ? 0 : theArtifactType.hashCode());
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
		ArtifactDTO other = (ArtifactDTO) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (theArtifactType == null) {
			if (other.theArtifactType != null)
				return false;
		} else if (!theArtifactType.equals(other.theArtifactType))
			return false;
		return true;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
