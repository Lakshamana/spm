package br.ufpa.labes.spm.repository.impl.log;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.log.ICatalogEventsDAO;
import br.ufpa.labes.spm.domain.CatalogEvents;

@Stateless
public class CatalogEventsDAO extends BaseDAO<CatalogEvents, Integer> implements ICatalogEventsDAO{

	protected CatalogEventsDAO(Class<CatalogEvents> businessClass) {
		super(businessClass);
	}

	public CatalogEventsDAO() {
		super(CatalogEvents.class);
	}


}
