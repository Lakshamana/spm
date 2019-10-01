package br.ufpa.labes.spm.service.interfaces;


import br.ufpa.labes.spm.service.dto.EmailDTO;
import br.ufpa.labes.spm.service.dto.AgentDTO;


public interface EmailServices {

	public EmailDTO saveConfiEmail(EmailDTO emailDTO);

	public EmailDTO testeEmail(EmailDTO emailDTO);

	public EmailDTO getConfiEmail(EmailDTO emailDTO);

	public EmailDTO updateConfiEmail(EmailDTO emailDTO);

	public AgentDTO enviaSenhaEmail(EmailDTO emailDTO, AgentDTO email);

}

