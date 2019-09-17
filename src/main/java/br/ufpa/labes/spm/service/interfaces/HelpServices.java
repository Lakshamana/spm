package org.qrconsult.spm.services.interfaces;

import java.util.List;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.help.HelpTopicDTO;

@Remote
public interface HelpServices {
	public List<HelpTopicDTO> getHelpTopics();

	public HelpTopicDTO saveTopic(HelpTopicDTO helpTopicDTO);

	public HelpTopicDTO getHelpTopicByToken(String token);
	
	public void removeTopic(HelpTopicDTO helpTopicDTO);

}
