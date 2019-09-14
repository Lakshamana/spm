package br.ufpa.labes.spm.repository.interfaces.resources;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Resource;

@Local
public interface IResourceDAO  extends IBaseDAO<Resource, String>{

}
