package br.ufpa.labes.spm.repository.interfaces.organizationPolicies;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Node;
import br.ufpa.labes.spm.domain.Repository;

@Local
public interface INodeDAO extends IBaseDAO<Node, String>{

}
