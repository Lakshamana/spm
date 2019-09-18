package br.ufpa.labes.spm.service.interfaces;

import javax.ejb.Remote;

import br.ufpa.labes.spm.service.dto.EmailDTO;
import br.ufpa.labes.spm.service.dto.AgentDTO;


@Remote
public interface EmailServices {

	public EmailDTO saveConfiEmail(EmailDTO emailDTO);

	public EmailDTO testeEmail(EmailDTO emailDTO);

	public EmailDTO getConfiEmail(EmailDTO emailDTO);

	public EmailDTO updateConfiEmail(EmailDTO emailDTO);

	public AgentDTO enviaSenhaEmail(EmailDTO emailDTO, AgentDTO email);

}

