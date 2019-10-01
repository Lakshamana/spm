package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Date;

public interface AgendaEventFormatter extends Serializable {
	public String formatDate(Date data);
}
