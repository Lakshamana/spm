package br.ufpa.labes.spm.repository.interfaces.log;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.CatalogEvents;

@Local
public interface ICatalogEventsDAO extends IBaseDAO<CatalogEvents, Integer>{

}
