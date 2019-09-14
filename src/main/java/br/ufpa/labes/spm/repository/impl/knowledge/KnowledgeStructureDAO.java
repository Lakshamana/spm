package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IKnowledgeStructureDAO;
import br.ufpa.labes.spm.domain.KnowledgeStructure;

public class KnowledgeStructureDAO extends BaseDAO<KnowledgeStructure, String> implements IKnowledgeStructureDAO{

	protected KnowledgeStructureDAO(Class<KnowledgeStructure> businessClass) {
		super(businessClass);
	}

	public KnowledgeStructureDAO() {
		super(KnowledgeStructure.class);
	}

}
