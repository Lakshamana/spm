package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Date;

import br.ufpa.labes.spm.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class AgendaEventDTO implements Serializable {

	@IgnoreMapping
  private String catalogEvent;

  private Long id;

	private Date when;

	public AgendaEventDTO() {}

	public AgendaEventDTO(String catalogEvent, Date when) {
		super();
		this.catalogEvent = catalogEvent;
		this.when = when;
	}

	public String getCatalogEvent() {
		return catalogEvent;
	}

	public void setCatalogEvent(String catalogEvent) {
		this.catalogEvent = catalogEvent;
	}

	public Date getWhen() {
		return when;
	}

	public void setWhen(Date when) {
		this.when = when;
	}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
