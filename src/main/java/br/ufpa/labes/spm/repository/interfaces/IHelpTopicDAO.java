package br.ufpa.labes.spm.repository.interfaces;


import java.util.List;

import javax.persistence.EntityManager;

import br.ufpa.labes.spm.repository.impl.HelpTopicDAO;
import org.qrconsult.spm.dtos.help.HelpTopicDTO;
import br.ufpa.labes.spm.domain.HelpTopic;

public interface IHelpTopicDAO extends IBaseDAO<HelpTopic, String>{
	public List<HelpTopic> getHelpTopics();

	public long countTopics();
}
