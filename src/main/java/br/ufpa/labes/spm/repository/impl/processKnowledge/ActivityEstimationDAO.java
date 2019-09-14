package br.ufpa.labes.spm.repository.impl.processKnowledge;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IActivityEstimationDAO;
import br.ufpa.labes.spm.domain.ActivityEstimation;

@Stateless
public class ActivityEstimationDAO extends BaseDAO<ActivityEstimation, Integer> implements IActivityEstimationDAO{

	private static final String ACTIVITY_METRIC_DEFINITION_NAME = "Activity Effort";

	protected ActivityEstimationDAO(Class<ActivityEstimation> businessClass) {
		super(businessClass);
	}

	public ActivityEstimationDAO() {
		super(ActivityEstimation.class);
	}

	public float getHoursEstimationForActivity(String normalIdent) {

		String queryString = "from " + ActivityEstimation.class.getName()
				+ " as e where e.activity.ident = '" + normalIdent + "'"
				+ " and e.metricDefinition.name = '"
				+ ACTIVITY_METRIC_DEFINITION_NAME + "'";

		Query query = getPersistenceContext().createQuery(queryString);

		List<ActivityEstimation> estimations = query.getResultList();

		if (estimations == null || estimations.isEmpty())
			return 0.0f;

		ActivityEstimation lastEstimation = estimations
				.get(estimations.size() - 1);

		return lastEstimation.getValue();
	}

}
