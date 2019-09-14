package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IWebAPSEEObjectToKnowledgeDAO;
import br.ufpa.labes.spm.domain.WebAPSEEObjectToKnowledge;

public class WebAPSEEObjectToKnowledgeDAO extends BaseDAO<WebAPSEEObjectToKnowledge, String> implements IWebAPSEEObjectToKnowledgeDAO{

	protected WebAPSEEObjectToKnowledgeDAO(Class<WebAPSEEObjectToKnowledge> businessClass) {
		super(businessClass);
	}

	public WebAPSEEObjectToKnowledgeDAO() {
		super(WebAPSEEObjectToKnowledge.class);
	}


}
