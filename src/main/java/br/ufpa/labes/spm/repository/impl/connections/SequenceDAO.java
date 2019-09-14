package br.ufpa.labes.spm.repository.impl.connections;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.ISequenceDAO;
import br.ufpa.labes.spm.domain.Sequence;

@Stateless
public class SequenceDAO extends BaseDAO<Sequence, String> implements ISequenceDAO{

	protected SequenceDAO(Class<Sequence> businessClass) {
		super(businessClass);
	}

	public SequenceDAO() {
		super(Sequence.class);
	}


}
