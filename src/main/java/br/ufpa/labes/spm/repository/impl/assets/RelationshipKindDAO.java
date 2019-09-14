package br.ufpa.labes.spm.repository.impl.assets;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.assets.IRelationshipKindDAO;
import br.ufpa.labes.spm.domain.RelationshipKind;


public class RelationshipKindDAO extends BaseDAO<RelationshipKind, String> implements IRelationshipKindDAO {

	public RelationshipKindDAO() {
		super(RelationshipKind.class);
	}

}
