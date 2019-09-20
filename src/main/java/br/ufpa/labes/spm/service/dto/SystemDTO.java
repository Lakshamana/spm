package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Collection;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;
import br.ufpa.labes.spm.service.dto.ProjectDTO;


@SuppressWarnings("serial")
public class SystemDTO implements Serializable{
	private Integer oid;

	private String ident;

	private String name;

	private String description;

	@IgnoreMapping
	private Collection<ProjectDTO> projetos;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<ProjectDTO> getProjetos() {
		return projetos;
	}

	public void setProjetos(Collection<ProjectDTO> projetos) {
		this.projetos = projetos;
	}


}
