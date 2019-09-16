package br.ufpa.labes.spm.repository.impl.organizationPolicies;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.INodeDAO;
import br.ufpa.labes.spm.domain.Node;

public class NodeDAO extends BaseDAO<Node, String> implements INodeDAO {

  protected NodeDAO(Class<Node> businessClass) {
    super(businessClass);
  }

  public NodeDAO() {
    super(Node.class);
  }
}
