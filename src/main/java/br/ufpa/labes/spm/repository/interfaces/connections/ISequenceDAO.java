package br.ufpa.labes.spm.repository.interfaces.connections;

import javax.ejb.Local;
import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Sequence;

@Local
public interface ISequenceDAO extends IBaseDAO<Sequence, String>{

}
