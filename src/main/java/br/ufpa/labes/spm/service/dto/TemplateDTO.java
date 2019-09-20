package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.List;

import br.ufpa.labes.spm.service.dto.ProcessModelDTO;

@SuppressWarnings("serial")
public class TemplateDTO implements Serializable {
	private String ident;
	private String templateState;
	private List<ProcessModelDTO> theInstances;

	public String getTemplateState() {
		return templateState;
	}

	public void setTemplateState(String templateState) {
		this.templateState = templateState;
	}

	public List<ProcessModelDTO> getTheInstances() {
		return theInstances;
	}

	public void setTheInstances(List<ProcessModelDTO> theInstances) {
		this.theInstances = theInstances;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

}
