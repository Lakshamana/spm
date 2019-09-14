package br.ufpa.labes.spm.repository.interfaces.processModelGraphic;

import java.util.Collection;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import org.qrconsult.spm.exceptions.DAOException;
import br.ufpa.labes.spm.domain.GraphicCoordinate;

@Local
public interface IGraphicCoordinateDAO extends IBaseDAO<GraphicCoordinate,String>{

	public Collection<GraphicCoordinate> getProcessCoordinates(String processId) throws DAOException;

}
