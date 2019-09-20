package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class AgendaEventDTO implements Serializable {

	@IgnoreMapping
	private String catalogEvent;

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


}
