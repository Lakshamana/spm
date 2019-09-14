package br.ufpa.labes.spm.repository.interfaces.knowledge;


import javax.ejb.Local;
import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.KnowledgeStructure;

@Local
public interface IKnowledgeStructureDAO extends IBaseDAO<KnowledgeStructure, String>{

}
