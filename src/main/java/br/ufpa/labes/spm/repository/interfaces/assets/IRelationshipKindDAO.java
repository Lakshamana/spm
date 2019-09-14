package br.ufpa.labes.spm.repository.interfaces.assets;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.RelationshipKind;


@Local
public interface IRelationshipKindDAO extends IBaseDAO<RelationshipKind, String> {
}
