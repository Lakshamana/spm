package br.ufpa.labes.spm.service.interfaces;

import java.util.List;


import br.ufpa.labes.spm.service.dto.HelpTopicDTO;

@Remote
public interface HelpServices {
	public List<HelpTopicDTO> getHelpTopics();

	public HelpTopicDTO saveTopic(HelpTopicDTO helpTopicDTO);

	public HelpTopicDTO getHelpTopicByToken(String token);

	public void removeTopic(HelpTopicDTO helpTopicDTO);

}
