package org.qrconsult.spm.services.interfaces;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.email.EmailDTO;
import org.qrconsult.spm.dtos.formAgent.AgentDTO;


@Remote
public interface EmailServices {

	public EmailDTO saveConfiEmail(EmailDTO emailDTO);

	public EmailDTO testeEmail(EmailDTO emailDTO);

	public EmailDTO getConfiEmail(EmailDTO emailDTO);

	public EmailDTO updateConfiEmail(EmailDTO emailDTO);

	public AgentDTO enviaSenhaEmail(EmailDTO emailDTO, AgentDTO email);

}

