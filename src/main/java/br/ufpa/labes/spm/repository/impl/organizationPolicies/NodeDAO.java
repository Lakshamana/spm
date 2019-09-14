package br.ufpa.labes.spm.repository.impl.organizationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.INodeDAO;
import br.ufpa.labes.spm.domain.Node;
import br.ufpa.labes.spm.domain.Structure;

@Stateless
public class NodeDAO extends BaseDAO<Node, String> implements INodeDAO{

	protected NodeDAO(Class<Node> businessClass) {
		super(businessClass);
	}

	public NodeDAO() {
		super(Node.class);
	}
}
