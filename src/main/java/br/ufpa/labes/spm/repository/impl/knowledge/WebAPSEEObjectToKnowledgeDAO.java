package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;

public class WebAPSEEObjectToKnowledgeDAO extends BaseDAO<WebAPSEEObjectToKnowledge, String> implements IWebAPSEEObjectToKnowledgeDAO{

	protected WebAPSEEObjectToKnowledgeDAO(Class<WebAPSEEObjectToKnowledge> businessClass) {
		super(businessClass);
	}

	public WebAPSEEObjectToKnowledgeDAO() {
		super(WebAPSEEObjectToKnowledge.class);
	}


}
