package br.ufpa.labes.spm.repository.impl.processModelGraphical;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Query;
import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processModelGraphic.IWebAPSEEObjectDAO;
import org.qrconsult.spm.exceptions.DAOException;
import br.ufpa.labes.spm.domain.GraphicCoordinate;
import br.ufpa.labes.spm.domain.WebAPSEEObject;

public class WebAPSEEObjectDAO extends BaseDAO<org.qrconsult.spm.model.processModelGraphical.WebAPSEEObject, String>
		implements IWebAPSEEObjectDAO {

	public WebAPSEEObjectDAO() {
		super(br.ufpa.labes.spm.domain.class);
	}

	protected WebAPSEEObjectDAO(Class<org.qrconsult.spm.model.processModelGraphical.WebAPSEEObject> businessClass) {
		super(businessClass);
	}

	public WebAPSEEObject retrieveWebAPSEEObject(Integer theReferredOid, String className) throws DAOException {

		Query query = this.getPersistenceContext().createQuery("FROM " + getBusinessClass().getName()
				+ " as obj WHERE obj.theReferredOid = :oid AND obj.className like :className");

		query.setParameter("oid", theReferredOid);
		query.setParameter("className", className);

		List retorno = query.getResultList();

		if (retorno != null) {
			if (!retorno.isEmpty()) {
				return (WebAPSEEObject) retorno.get(0);
			} else {
				return null;
			}
		} else
			return null;
	}

	public Collection<GraphicCoordinate> retrieveProcessCoordinates(String processId) throws DAOException {
		// We need to use order by clause since it is less complex to remove
		// duplicated elements in a sorted array
		// We use desc to be sure that aways the older coordinate has been
		// removed with iter.remove()
		Query query = this.getPersistenceContext().createQuery("SELECT obj.theGraphicCoordinate FROM "
				+ getBusinessClass().getName()
				+ " as obj WHERE obj.theGraphicCoordinate.theProcess=:process ORDER BY obj.theReferredOid desc, obj.className, obj.oid");
		query.setParameter("process", processId);

		List objs = query.getResultList();

		Iterator iter = objs.iterator();
		if (iter.hasNext()) {
			GraphicCoordinate predecessor = (GraphicCoordinate) iter.next();
			while (iter.hasNext()) {
				boolean removed = false;
				GraphicCoordinate gcoord = (GraphicCoordinate) iter.next();
				if (predecessor.getTheObjectReference().getTheReferredOid()
						.equals(gcoord.getTheObjectReference().getTheReferredOid())) {
					if (predecessor.getTheObjectReference().getClassName()
							.equals(gcoord.getTheObjectReference().getClassName())) {
						iter.remove();
						removed = true;
					}
				}
				if (!removed)
					predecessor = gcoord;
			}
		}
		return objs;
	}
}
