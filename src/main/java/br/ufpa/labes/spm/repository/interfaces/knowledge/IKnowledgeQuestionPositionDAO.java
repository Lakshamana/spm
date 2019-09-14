package br.ufpa.labes.spm.repository.interfaces.knowledge;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.KnowledgeQuestionPosition;

@Local
public interface IKnowledgeQuestionPositionDAO extends IBaseDAO<KnowledgeQuestionPosition, Integer>{

}
