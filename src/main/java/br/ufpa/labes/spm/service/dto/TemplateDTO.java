package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class TemplateDTO implements Serializable {
  private String ident;
  private String templateState;
  private Long id;
  private List<ProcessModelDTO> theInstances;

  public String getTemplateState() {
    return templateState;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
