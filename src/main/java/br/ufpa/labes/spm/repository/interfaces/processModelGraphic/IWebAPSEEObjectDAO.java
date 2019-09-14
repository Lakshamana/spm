package br.ufpa.labes.spm.repository.interfaces.processModelGraphic;

import java.util.Collection;


import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import org.qrconsult.spm.exceptions.DAOException;
import br.ufpa.labes.spm.domain.GraphicCoordinate;
import br.ufpa.labes.spm.domain.WebAPSEEObject;

public interface IWebAPSEEObjectDAO extends IBaseDAO<WebAPSEEObject, String>{

	public WebAPSEEObject retrieveWebAPSEEObject(Integer theReferredOid, String className) throws DAOException;
	public Collection<GraphicCoordinate> retrieveProcessCoordinates(String processId) throws DAOException;

}
