package br.ufpa.labes.spm.repository.interfaces.resources;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Consumable;

@Local
public interface IConsumableDAO extends IBaseDAO<Consumable, String>{

}
