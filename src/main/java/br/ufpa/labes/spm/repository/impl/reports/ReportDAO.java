package br.ufpa.labes.spm.repository.impl.reports;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.processModels.ProcessDAO;
import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.repository.interfaces.IReportDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IProjectDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessDAO;
import org.qrconsult.spm.exceptions.DAOException;
import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Decomposed;
import br.ufpa.labes.spm.domain.Plain;
import br.ufpa.labes.spm.domain.Process;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.AgentPlaysRole;
import br.ufpa.labes.spm.domain.WorkGroup;
import br.ufpa.labes.spm.domain.Role;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.AgendaEvent;
import br.ufpa.labes.spm.domain.Company;
import br.ufpa.labes.spm.domain.Project;
import br.ufpa.labes.spm.domain.InvolvedArtifact;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.domain.ReqWorkGroup;
import br.ufpa.labes.spm.domain.RequiredPeople;
import br.ufpa.labes.spm.domain.RequiredResource;
import br.ufpa.labes.spm.domain.ActivityEstimation;
import br.ufpa.labes.spm.domain.AgentMetric;
import br.ufpa.labes.spm.domain.ArtifactMetric;
import br.ufpa.labes.spm.domain.ResourceMetric;
import br.ufpa.labes.spm.domain.ProcessModel;
import br.ufpa.labes.spm.domain.Consumable;
import br.ufpa.labes.spm.domain.Exclusive;
import br.ufpa.labes.spm.domain.Resource;
import br.ufpa.labes.spm.domain.Shareable;
import br.ufpa.labes.spm.domain.ProcessAgenda;
import br.ufpa.labes.spm.domain.Task;
import org.qrconsult.spm.services.impl.CriticalPathMethod;

public class ReportDAO implements IReportDAO{

	private static final String ACTIVITY_METRIC_DEFINITION_NAME = "Activity Effort";

	@PersistenceContext(unitName = "SPMPU")
	private EntityManager em;

	IProjectDAO dao;

	IProcessDAO processDAO;

	@Override
	public List<Object[]> getAgentsReportData(Date atDate) {
		String queryString = "from " + Agent.class.getName() + " as agent " +
				 "where agent.isActive is true order by agent.name";

		Query query = this.getPersistenceContext().createQuery(queryString);

		List<Agent> agents = query.getResultList();
		System.out.println(agents);
		List<Object[]> result = new ArrayList<Object[]>();

		if (agents == null || agents.isEmpty() )
			return result;

		for ( Agent agent : agents ) {
			Object[] entry = new Object[ 4 ];

			entry[0] = agent.getIdent();
			entry[1] = agent.getName();
			entry[2] = new Double(agent.getCostHour());

			if (atDate != null){
				entry[3] = new Integer(getAgentWorkloadAt(agent.getIdent(), atDate));
			}
			else {
				entry[ 3 ] = Integer.valueOf(getAgentWorkLoad( agent.getIdent()));
				entry[3] = 0;
			}

			result.add( entry );
		}
		return result;
	}

	public List<Object[]> getActivitiesByProcessReportData( String processIdent ) {
		String activitiesHql = "select normal.ident, normal.name, " +
						"normal.theEnactionDescription.state, " +
						"normal.plannedBegin, normal.plannedEnd, " +
						"normal.theEnactionDescription.actualBegin, " +
						"normal.theEnactionDescription.actualEnd " +
						"from " + Normal.class.getName() + " as normal " +
						"where normal.ident like '" + processIdent + ".%' " +
						"and normal.isVersion is null " +
						"order by normal.ident";

		String tasksHql = "select task.theNormal.ident, pAgenda.theTaskAgenda.theAgent.ident, " +
							"pAgenda.theTaskAgenda.theAgent.name, task.localState, task.workingHours " +
							"from " + ProcessAgenda.class.getName() + " as pAgenda " +
							"joinCon pAgenda.theTask as task " +
							"where task.theNormal.ident like '" + processIdent + ".%' " +
							"order by task.theNormal.ident, pAgenda.theTaskAgenda.theAgent.name";

		Query query = this.getPersistenceContext().createQuery(activitiesHql);
		List<Object[]> normals = query.getResultList();

		query = this.getPersistenceContext().createQuery( tasksHql );
		List<Object[]> tasks = query.getResultList();

		if (tasks == null || tasks.isEmpty()) {
			String reqPeopleHql = "select rp.theNormal.ident, rp.theAgent.ident, rp.theAgent.name " +
									"from " + ReqAgent.class.getName() + " as rp " +
									"where rp.theNormal.ident like '" + processIdent + ".%' " +
									"order by rp.theNormal.ident, rp.theAgent.ident";

			query = this.getPersistenceContext().createQuery(reqPeopleHql);

			tasks = query.getResultList();
		}

		List<Object[]> toReturn = new ArrayList<Object[]>();
			if ( normals == null || normals.isEmpty() ) {
			return toReturn;
		}

		Object[] currentTask = null;

		if ( !tasks.isEmpty() )
			currentTask = tasks.remove( 0 );

		for ( Object[] result : normals ) {
			if ( result == null )
				continue;

			Object[] entry = new Object[ 10 ];

			entry[ 0 ] = result[ 0 ];
			entry[ 1 ] = result[ 1 ];
			entry[ 2 ] = result[ 2 ];
			entry[ 3 ] = result[ 3 ];
			entry[ 4 ] = result[ 4 ];
			entry[ 5 ] = result[ 5 ];
			entry[ 6 ] = result[ 6 ];
			entry[ 7 ] = this.getActivityHourEstimation((String)entry[0]);
			entry[ 8 ] = 0.0;

			List<Object[]> tasksToAdd = new ArrayList<Object[]>();

			while ( currentTask != null && currentTask[ 0 ].equals( entry[ 0 ] ) ) {
				try {
					if ( (Float)currentTask[ 4 ] == 0 ) {
						currentTask[ 4 ] = (Float) getWorkingHoursForTask( (String)currentTask[ 0 ], (String)currentTask[ 1 ] );
					}

					entry[ 8 ] = (Double)entry[ 8 ] + new Double( (Float)currentTask[ 4 ] );
				} catch (ArrayIndexOutOfBoundsException e) { }

				tasksToAdd.add( currentTask );

				if ( !tasks.isEmpty() )
					currentTask = tasks.remove( 0 );
				else currentTask = null;
			}

			entry[ 9 ] = tasksToAdd;

			toReturn.add( entry );
		}

		Collections.sort(toReturn, new OrderByPlannedBeginWorkGroupedByActivity(processIdent) {
			public int compare(Object o1, Object o2) {
				Object[] os1 = (Object[])o1, os2 = (Object[])o2;

				Activity a1 = null;
				Activity a2 = null;
					a1 = (Activity) actMap.get((String)os1[0]);
				a2 = (Activity) actMap.get((String)os2[0]);

				return super.compare(a1, a2);
			}
		});


		return toReturn;
	}

	public float getWorkingHoursForTask( String normalIdent, String agentIdent )
	{
		AgendaEvent[] event = this.getAgendaEventsForTask( normalIdent, agentIdent );

		if ( event == null )
			return 0.0f;

		float totalElapsedTime = 0.0f;

		boolean isCounting = false;

		long startTimeMillis = -1;
		long endTimeMillis = -1;
		long elapsedTime = -1;

		for (int i = 0; i < event.length; i++) {
			if (event[i].getTheCatalogEvents().getDescription().equals("ToActive")) {
				isCounting = true;

				startTimeMillis = event[i].getWhen().getTime();
			} else if (event[i].getTheCatalogEvents().getDescription().equals("ToFinished")
					|| event[i].getTheCatalogEvents().getDescription().equals("ToPaused")
					|| event[i].getTheCatalogEvents().getDescription().equals("ToFailed")) {
				isCounting = false;

				if (startTimeMillis != -1) {
					endTimeMillis = event[i].getWhen().getTime();

					elapsedTime = endTimeMillis - startTimeMillis;

					totalElapsedTime += ((float) elapsedTime / (1000 * 60 * 60));
				}
			}
		}

		if (isCounting) {
			Date date = new Date();

			endTimeMillis = date.getTime();

			elapsedTime = endTimeMillis - startTimeMillis;

			totalElapsedTime += ((float) elapsedTime / (1000 * 60 * 60));
		}

		return totalElapsedTime;
	}

	public AgendaEvent[] getAgendaEventsForTask( String normalIdent, String agentIdent )
	{
			String hql = "from " +
							AgendaEvent.class.getName() + " event " +
							"where ( event.theTask.theProcessAgenda.theTaskAgenda.theAgent.ident=:agentID ) " +
							"and ( event.theTask.theNormal.ident=:normalID ) order by event.oid";

			Query query = this.getPersistenceContext().createQuery( hql );

			query.setParameter( "agentID", agentIdent );
			query.setParameter( "normalID", normalIdent );

			List<AgendaEvent> list = query.getResultList();

			AgendaEvent[] result = new AgendaEvent[ list.size() ];

			result = list.toArray( result );

			return result;
	}

	public List<Object[]> getAgentsByRoleReportData() {
		String hql = "from " + AgentPlaysRole.class.getName() + " as agentPlaysRole " +
						"order by agentPlaysRole.theRole.name, agentPlaysRole.theAgent.name";

		Query query = this.getPersistenceContext().createQuery( hql );

		List<AgentPlaysRole> agentsPlaysRoles = query.getResultList();

		List<Object[]> result = new ArrayList<Object[]>();
		System.out.println(result);
		if ( agentsPlaysRoles == null || agentsPlaysRoles.isEmpty() ) {
			return result;
		}

		for ( AgentPlaysRole agentPlaysRole : agentsPlaysRoles ) {
			if ( agentPlaysRole == null )
				continue;

			Object[] entry = new Object[ 3 ];

			Agent agent = agentPlaysRole.getTheAgent();
			Role role = agentPlaysRole.getTheRole();

			if ( agent == null || role == null )
				continue;

			if(agent.isActive()){
				entry[ 0 ] = role.getName();
				entry[ 1 ] = agent.getName();
				entry[ 2 ] = agentPlaysRole.getSince_date();

				result.add( entry );
			}
		}
		return result;
	}

	@Override
	public List<Object[]> getAgentsByWorkGroupReportData() {
		String hql = "from " + WorkGroup.class.getName() + " as group " +
				"order by group.name";
		Query query = this.getPersistenceContext().createQuery(hql);

		List<WorkGroup> groups = query.getResultList();

		List<Object[]> result = new ArrayList<Object[]>();

		if ( groups == null || groups.isEmpty() ) {
			return result;
		}

		for ( WorkGroup group : groups ) {
			if ( group == null )
				continue;

			List<Agent> agents = new ArrayList<Agent>();
			agents.addAll( group.getTheAgent() );

			Collections.sort( agents, new Comparator<Agent>() {

				public int compare(Agent o1, Agent o2) {
					return o1.getIdent().compareToIgnoreCase( o2.getIdent() );
				}

			});

			for ( Agent agent : agents ) {
				if(agent.isActive()){
					Object[] entry = new Object[ 2 ];

					entry[ 0 ] = group.getName();
					entry[ 1 ] = agent.getName();

					result.add( entry );
				}
			}
		}
		return result;
	}

	@Override
	public List<Object[]> getProjectArtifactsReportData(String projectIdent) {
		List<Project> projects = new ArrayList<Project>();

		List<Object[]> result = new ArrayList<Object[]>();

		if ( projectIdent != null )
			projects.add( (Project) dao.retrieveBySecondaryKey( projectIdent ) );
		else projects.addAll( dao.findAll() );

		if ( projects.isEmpty() )
			return result;

		for ( Project project : projects ) {

			Collection<Artifact> artifacts = project.getFinalArtifacts();
			for ( Artifact artifact : artifacts ) {
				Object[] entry = new Object[ 4 ];

				entry[ 0 ] = project.getName();

				br.ufpa.labes.spm.domain.getTheSystem();

				if ( system != null )
					entry[ 1 ] = project.getTheSystem().getName();
				else entry[ 1 ] = "";

				entry[ 2 ] = artifact.getIdent();
				entry[ 3 ] = artifact.getTheArtifactType().getIdent();
				result.add( entry );
			}
		}


		Collections.sort(result, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				int result = o1[0].toString().compareToIgnoreCase(o2[0].toString());

				if (result == 0)
					return o1[1].toString().compareToIgnoreCase(o2[1].toString());

				if (result == 0)
					return o1[2].toString().compareToIgnoreCase(o2[2].toString());

				return result;
			}
		});

		return result;
	}

	public int getAgentWorkloadAt( String agentIdent, Date atDate ) {

		String queryString = "from " + ReqAgent.class.getName() + " as reqAgent " +
								 "where reqAgent.theAgent.ident = :agentIdent";

		Query query = this.getPersistenceContext().createQuery(queryString);
		query.setParameter( "agentIdent", agentIdent );

		List<ReqAgent> reqAgents = query.getResultList();

		int workload = 0;

		if ( reqAgents == null || reqAgents.isEmpty() )
			return workload;

		for ( ReqAgent reqAgent : reqAgents ) {
			if ( reqAgent == null )
				continue;

			Normal normal = reqAgent.getTheNormal();

			if ( normal == null )
				continue;

			Date plannedBegin = normal.getPlannedBegin();
			Date plannedEnd = normal.getPlannedEnd();

			if ( plannedBegin != null && atDate.compareTo( plannedBegin ) >= 0 ){
				if ( plannedEnd != null && atDate.compareTo( plannedEnd ) <= 0 ){
					workload++;
				}
			}
		}

		return workload;
	}

	private double getActivityHourEstimation(String actIdent){
		String queryString = "from " + ActivityEstimation.class.getName() +
				" as e where e.activity.ident = '" + actIdent + "'" +
				" and e.metricDefinition.name = '" + ACTIVITY_METRIC_DEFINITION_NAME + "'";

		Query query = this.getPersistenceContext().createQuery(queryString);

		List<ActivityEstimation> estimations = query.getResultList();

		if ( estimations == null || estimations.isEmpty() )
			return 0.0;

		ActivityEstimation lastEstimation = estimations.get( estimations.size() - 1 );

		return lastEstimation.getValue();
	}


	@Override
	public EntityManager getPersistenceContext() {
		return em;
	}

	private class OrderByPlannedBeginWorkGroupedByActivity implements Comparator<Object> {

		public String process;
		public HashMap<String, Activity> actMap = new HashMap<String, Activity>();

		public OrderByPlannedBeginWorkGroupedByActivity(String process) {
			this.process = process;

			String queryStr = "from " + Activity.class.getName() + " as act where act.ident like :ident";

			Query query = getPersistenceContext().createQuery(queryStr);
			query.setParameter("ident", process + ".%");

			List<Activity> acts = query.getResultList();

			actMap = new HashMap<String, Activity>();

			for (Activity act : acts) {
				actMap.put(act.getIdent(), act);
			}
		}

		public int compare(Object o1, Object o2) {
			Activity a1 = (Activity)o1, a2 = (Activity)o2;

			String ident1 = a1.getIdent(),
					ident2 = a2.getIdent();

			if (ident1.equals(ident2))
				return 0;

			// a.b.c < a.b.c.d
			if (ident1.contains(ident2))
				return 1;
			else if (ident2.contains(ident1))
				return -1;

			// if ident1 == "a.b.c.d" && ident2 == "a.e"
			// then { ident1 == "a.b"; ident2 = "a.e" }
			String[] tokens1 = ident1.split("\\.");
			String[] tokens2 = ident2.split("\\.");

			ident1 = "";
			ident2 = "";

			int i = 0;

			while(tokens1[i].equals(tokens2[i])) {
				ident1 += tokens1[i] + ".";
				ident2 += tokens2[i] + ".";

				i++;
			}

			ident1 += tokens1[i];
			ident2 += tokens2[i];

			// retrieve the activities if necessary
			if (!ident1.equals(a1.getIdent())) {
				a1 = (Activity) actMap.get(ident1);
			}

			if (!ident2.equals(a2.getIdent())) {
				a2 = (Activity) actMap.get(ident2);
			}

			// compare by planned begin
			OrderByPlannedBegin order = new OrderByPlannedBegin(process);

			return order.compare(a1, a2);
		}
	}

	private class OrderByPlannedBegin implements Comparator<Activity> {

		public HashMap<String, Collection<Activity>> actMap = new HashMap<String, Collection<Activity>>();

		public OrderByPlannedBegin (String process) { // create cache of activities
		/*try {
			ProcessDAO pDao = new ProcessDAO();
			processModels.classes.Process p = (Process) pDao.findBySecondaryKey(process);

			build(p.getTheProcessModel());
		} catch (DAOException e) {
			e.printStackTrace();
		}*/

			String queryStr = "from " + Activity.class.getName() + " as act where act.ident like :ident and act.isVersion is null";

			Query query = getPersistenceContext().createQuery(queryStr);
			query.setParameter("ident", process + ".%");

			List<Activity> acts = query.getResultList();

			actMap = new HashMap<String, Collection<Activity>>();

			for (Activity act : acts) {
				String pModelIdent = act.getIdent().substring(0, act.getIdent().lastIndexOf("."));

				Collection<Activity> pModelActs = actMap.get(pModelIdent);

				if (pModelActs == null) {
					pModelActs = new ArrayList<Activity>();
					actMap.put(pModelIdent, pModelActs);
				}

				pModelActs.add(act);
			}
		}

		/*private void build(ProcessModel model) {
		Collection<Activity> acts = model.getTheActivity();

		actMap.put("model", acts);

		for (Activity act : acts) {
			if (act instanceof Decomposed)
				buildact.getTheDecomposedSub().getTheReferedProcessModel());
		}
	}*/

		public int compare(Activity a1, Activity a2) {
			Date plannedBegin1 = new Date(),
			plannedEnd1 = new Date(),
    	    plannedBegin2 = new Date(),
    	    plannedEnd2 = new Date();

			String ident1 = a1.getIdent();
			String ident2 = a2.getIdent();

			if(a1 instanceof Normal){
				plannedBegin1 = ( (Normal) a1).getPlannedBegin();
				plannedEnd1 =   ( (Normal) a1).getPlannedEnd();
			}else if(a1 instanceof Decomposed){
				plannedBegin1 = getFirstPlannedBeginFromProcessModel(
    				           ( (Decomposed) a1).getTheReferedProcessModel()
    				           );
				plannedEnd1 =   getLatestPlannedEndFromProcessModel(
    				           ( (Decomposed) a1).getTheReferedProcessModel()
    				           );
			}

			if(a2 instanceof Normal){
				plannedBegin2 = ( (Normal) a2).getPlannedBegin();
				plannedEnd2 =   ( (Normal) a2).getPlannedEnd();
			}else if(a2 instanceof Decomposed){
				plannedBegin2 = getFirstPlannedBeginFromProcessModel(
    				           ( (Decomposed) a2).getTheReferedProcessModel()
    				           );
				plannedEnd2 =   getLatestPlannedEndFromProcessModel(
						( (Decomposed) a2).getTheReferedProcessModel()
    				           );
			}

			if ( plannedBegin1 == null )
				return -1;

			if ( plannedBegin2 == null )
				return 1;

			if(plannedBegin1.compareTo(plannedBegin2) != 0){

				return plannedBegin1.compareTo(plannedBegin2);

			}else if(plannedEnd1.compareTo(plannedEnd2) != 0)

				return plannedEnd1.compareTo(plannedEnd2);

			return ident1.compareTo(ident2);
		}

		public Date getFirstPlannedBeginFromProcessModel(ProcessModel pModel){
			Collection actsFromPModel = actMap.get((pModel.getTheProcess() != null ? pModel.getTheProcess().getIdent() : pModel.getTheDecomposed().getIdent()));

			if (actsFromPModel == null) return null;

			Date firstBeginFromPModel = null;

			for(Object obj : actsFromPModel){
				if(obj == null) continue;

				Activity act = (Activity)obj;

				Date planBegin = null;

				if(act instanceof Normal){
					Normal normalAct = (Normal) act;

					planBegin = normalAct.getPlannedBegin();

				}else if(act instanceof Decomposed){
					Decomposed decAct = (Decomposed)act;

					planBegin =
							getFirstPlannedBeginFromProcessModel(decAct.getTheReferedProcessModel());
				}

				if(firstBeginFromPModel == null){
					firstBeginFromPModel = planBegin;
				}else if(isAfter(firstBeginFromPModel, planBegin)){
					firstBeginFromPModel = planBegin;
				}
			}

			return firstBeginFromPModel;
		}

		private boolean isAfter(Date date1, Date date2){
			if(date1 == null){
				return false;
			}
			else {
				if(date2 == null){
					return true;
				}
				else{
					if(date1.getTime() > date2.getTime()){
						return true;
					}
					return false;
				}
			}
		}

		public Date getLatestPlannedEndFromProcessModel(ProcessModel pModel){
			Collection actsFromPModel = actMap.get((pModel.getTheProcess() != null ? pModel.getTheProcess().getIdent() : pModel.getTheDecomposed().getIdent()));

			Date lastFinishFromPModel = null;

			if(actsFromPModel != null){
				for(Object obj: actsFromPModel){
					if(obj == null) continue;

					Activity act = (Activity)obj;

					Date planEnd = null;

					if (act.getThePlainSub().getTheNormalSub() != null) {
						Normal normalAct = act.getThePlainSub().getTheNormalSub();

						planEnd = normalAct.getPlannedEnd();

					}else if (act.getTheDecomposedSub()) {
						Decomposed decAct = (Decomposed)act;

						planEnd =
								getLatestPlannedEndFromProcessModel(decAct.getTheReferedProcessModel());
					}

					if(lastFinishFromPModel == null){
						lastFinishFromPModel = planEnd;
					}else if(isAfter(planEnd, lastFinishFromPModel)){
						lastFinishFromPModel = planEnd;
					}
				}
			}

			return lastFinishFromPModel;
		}
	}

	public String getAgentWorkLoad(String agIdent) {
		String hql_task = " select count(task) "
			+ " from "
			+ Task.class.getName()
			+ " as task "
			+ "where "
			+ "(task.localState = :stateReady or task.localState = :stateActive "
			+ "or task.localState = :stateWaiting or task.localState = :statePaused) and task.theProcessAgenda.theTaskAgenda.theAgent.ident=:agIdent";

		Query query = this.getPersistenceContext().createQuery(hql_task);

		query.setParameter("agIdent", agIdent);

		query.setParameter("stateReady", "Ready");
		query.setParameter("stateActive", "Active");
		query.setParameter("stateWaiting", "Active");
		query.setParameter("statePaused", "Paused");

		Collection result = query.getResultList();

		/**convert value to string result*/
		Iterator ite = result.iterator();
		Object obj = ite.next();

		return String.valueOf(obj);
	}

	@Override
	public List<Object[]> getActivitiesByAgentsReportData(String agentIdent , Date beginDate, Date endDate , String role , boolean allStates) {
		String ReqWorkGroupHql = "select ReqWorkGroup.oid from " + ReqWorkGroup.class.getName() + " as ReqWorkGroup joinCon ReqWorkGroup.theWorkGroup.theAgent as agent where agent.ident= :agentIdent";

		String reqAgentHql = "select reqAgent.oid from " + ReqAgent.class.getName() + " as reqAgent where reqAgent.theAgent.ident = :agentIdent";
		System.out.println();
		String hql = "select task.theProcessAgenda.theTaskAgenda.theAgent.ident, task.theProcessAgenda.theTaskAgenda.theAgent.name, " +
						"reqPeople, task.theProcessAgenda.theTaskAgenda.theAgent.costHour, " +
						"task.theProcessAgenda.theProcess.ident, task.theNormal.ident, task.theNormal.name, task.localState, " +
						"task.theNormal.theEnactionDescription.actualEnd, task.theNormal.plannedEnd, " +
						"task.theNormal.theEnactionDescription.actualBegin, task.theNormal.plannedBegin " +
						"from " + Task.class.getName() + " as task joinCon task.theNormal.theRequiredPeople as reqPeople " +
						"where reqPeople.theNormal.ident = task.theNormal.ident " + "and task.theProcessAgenda.theTaskAgenda.theAgent.isActive is true " +
						(agentIdent != null ? "and task.theProcessAgenda.theTaskAgenda.theAgent.ident= :agentIdent and (reqPeople.oid in (" + ReqWorkGroupHql + ") or reqPeople.oid in (" + reqAgentHql + ")) " : " ") +
						(beginDate!=null ? "and task.theNormal.plannedBegin >= :beginDate " : " ") +
						(endDate!=null ? "and task.theNormal.plannedEnd <= :endDate " : " ") +
						(!allStates ? "and (task.theNormal.theEnactionDescription.state like :active or task.theNormal.theEnactionDescription.state like :ready or task.theNormal.theEnactionDescription.state like :waiting) " : " ") +
						"order by task.theProcessAgenda.theTaskAgenda.theAgent.name, task.theProcessAgenda.theProcess.ident, task.theNormal.name";

		Query query = this.getPersistenceContext().createQuery(hql);

		if ( agentIdent != null )
			query.setParameter( "agentIdent", agentIdent );
		if(beginDate!=null)
			query.setParameter( "beginDate", beginDate );
		if(endDate!=null)
			query.setParameter( "endDate", endDate );
		if(!allStates){
			query.setParameter("active", Plain);
			query.setParameter("ready", Plain.READY);
			query.setParameter("waiting", Plain.WAITING);
		}

		List<Object[]> tasks = query.getResultList();
		System.out.println(tasks);
		List<Object[]> toReturn = new ArrayList<Object[]>();

		if ( tasks == null || tasks.isEmpty() ) {
			return toReturn;
		}

		for ( Object[] result : tasks ) {
			if ( result == null )
				continue;

			Object[] entry = new Object[ 13 ];

			entry[ 0 ] = result[ 0 ] != null ? result[ 0 ] : "";
			entry[ 1 ] = result[ 1 ] != null ? result[ 1 ] : "";

			if (result[2] != null) {
				if (result[2] instanceof ReqWorkGroup) {
					if(role!=null)
						continue;

					ReqWorkGroup ReqWorkGroup = (ReqWorkGroup)result[2];

					WorkGroup group = ReqWorkGroup.getTheWorkGroup();

					entry[2] = group != null ? group.getIdent() : "";
				}
				else {
					ReqAgent reqAgent = (ReqAgent)result[2];

					String roleIdent = reqAgent.getTheRole().getIdent();
					if(role!=null && !role.equals(roleIdent))
						continue;

					entry[2] = roleIdent;
				}
			}
			else entry[2] = "";

			entry[ 3 ] = new Double( (Float)( result[ 3 ] != null ? result[ 3 ] : 0.0 ) );

			entry[ 4 ] = getAgentWorkLoad( (String)result[ 0 ] );

			String actIdent = (String)result[ 5 ];

			entry[ 5 ] = result[ 4 ] != null ? result[ 4 ] : "";
			entry[ 6 ] = result[ 6 ] != null ? result[ 6 ] : "";

			StringTokenizer tokenizer = new StringTokenizer( actIdent, "." );

			tokenizer.nextToken();
			while( tokenizer.hasMoreTokens() )
			{
				entry[ 6 ] = "    " + entry[ 6 ];
				tokenizer.nextToken();
			}

			entry[ 7 ] = result[ 7 ] != null ? result[ 7 ] : "";

			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			if ( result[ 8 ] != null && result[ 9 ] != null) {
				Calendar actualEnd = Calendar.getInstance();
				actualEnd.setTime( (Date)result[ 8 ] );

				Calendar plannedEnd = Calendar.getInstance();
				plannedEnd.setTime( (Date)result[ 9 ] );

				actualEnd.set( Calendar.HOUR, 0 );
				actualEnd.set( Calendar.MINUTE, 0 );
				actualEnd.set( Calendar.SECOND, 0 );
				actualEnd.set( Calendar.AM_PM, Calendar.AM );

				plannedEnd.set( Calendar.HOUR, 0 );
				plannedEnd.set( Calendar.MINUTE, 0 );
				plannedEnd.set( Calendar.SECOND, 0 );
				plannedEnd.set( Calendar.AM_PM, Calendar.AM );

				long delayInMillis = actualEnd.getTimeInMillis() - plannedEnd.getTimeInMillis();
				int delayInDays = (int)( (double)delayInMillis / ( 1000 * 60 * 60 * 24 ) );

				entry[ 8 ] = delayInDays;
				Date actualEndDate = actualEnd.getTime();
				entry[ 9 ] = formatter.format(actualEndDate);
			}
			else {
				entry[ 8 ] = 0;
				entry[ 9 ] = "";
			}

			if(result[ 9 ] != null){
				Calendar plannedEnd = Calendar.getInstance();
				plannedEnd.setTime( (Date)result[ 9 ] );

				plannedEnd.set( Calendar.HOUR, 0 );
				plannedEnd.set( Calendar.MINUTE, 0 );
				plannedEnd.set( Calendar.SECOND, 0 );
				plannedEnd.set( Calendar.AM_PM, Calendar.AM );

				Date plannedEndDate = plannedEnd.getTime();
				entry[10] = formatter.format(plannedEndDate);
			}else entry[10] = "";

			if(result[10]!=null){
				Calendar actualBegin = Calendar.getInstance();

				actualBegin.setTime( (Date)result[ 10 ] );

				actualBegin.set( Calendar.HOUR, 0 );
				actualBegin.set( Calendar.MINUTE, 0 );
				actualBegin.set( Calendar.SECOND, 0 );
				actualBegin.set( Calendar.AM_PM, Calendar.AM );

				Date actualBeginDate = actualBegin.getTime();
				entry[11] = formatter.format(actualBeginDate);
			}else entry[11] = "";

			if(result[11]!=null){
				Calendar plannedBegin = Calendar.getInstance();

				plannedBegin.setTime( (Date)result[ 11 ] );

				plannedBegin.set( Calendar.HOUR, 0 );
				plannedBegin.set( Calendar.MINUTE, 0 );
				plannedBegin.set( Calendar.SECOND, 0 );
				plannedBegin.set( Calendar.AM_PM, Calendar.AM );

				Date plannedBeginDate = plannedBegin.getTime();
				entry[12] = formatter.format(plannedBeginDate);
			}else entry[12] = "";

			toReturn.add( entry );
		}


		return null;
	}

	@Override
	public List<Object[]> getCostDeviationReportData(String processIdent) {
		String queryString = "from " + Normal.class.getName() + " as normal " +
				"where normal.ident like '" + processIdent + ".%' " +
				"order by normal.ident";

		Query query = this.getPersistenceContext().createQuery(queryString);

		List<Normal> normals = query.getResultList();
		List<Object[]> result = new ArrayList<Object[]>();

		if ( normals == null || normals.isEmpty() )
			return result;

		Collections.sort(normals, new OrderByPlannedBeginWorkGroupedByActivity(processIdent));

		for ( Normal normal : normals ) {
			if ( normal == null )
				continue;

			Object[] entry = new Object[ 4 ];

			double estimatedHours = getActivityHourEstimation( normal.getIdent());
			double estimatedCost = 0;
			double realCost = 0;

			Collection<RequiredPeople> reqPeople = normal.getTheRequiredPeople();

			for ( RequiredPeople requiredPeople : reqPeople ) {
				if ( requiredPeople instanceof ReqWorkGroup ){
					ReqWorkGroup ReqWorkGroup = (ReqWorkGroup)requiredPeople;
					if(ReqWorkGroup.getTheWorkGroup() == null)
						continue;

					for ( Agent agent : (Collection<Agent>)ReqWorkGroup.getTheWorkGroup().getTheAgent() ) {
						if ( agent == null )
							continue;

						estimatedCost += agent.getCostHour() * estimatedHours;
						realCost += agent.getCostHour() * getWorkingHoursForTask( normal.getIdent(), agent.getIdent() );
					}
				}
				else if ( requiredPeople instanceof ReqAgent ){
					Agent agent = ( (ReqAgent)requiredPeople ).getTheAgent();

					if ( agent == null )
						continue;

					estimatedCost += agent.getCostHour() * estimatedHours;
					realCost += agent.getCostHour() * getWorkingHoursForTask( normal.getIdent(), agent.getIdent() );
				}
			}

			entry[ 0 ] = processIdent;
			entry[ 1 ] = normal.getName();

			StringTokenizer tokenizer = new StringTokenizer( normal.getIdent(), "." );

			tokenizer.nextToken();
			while( tokenizer.hasMoreTokens() ){
				entry[ 1 ] = "    " + entry[ 1 ];
				tokenizer.nextToken();
			}

			entry[ 2 ] = realCost;
			entry[ 3 ] = estimatedCost;

			result.add( entry );
		}
		return result;
	}

	@Override
	public List<Object[]> getResourceStatesReportData() {
		String queryString = "from " + Resource.class.getName() + " as resource order by resource.ident";
		Query query = this.getPersistenceContext().createQuery( queryString );

		List<Resource> resources = query.getResultList();
		List<Object[]> result = new ArrayList<Object[]>();

		if ( resources == null || resources.isEmpty() )
			return result;

		for ( Resource resource : resources ) {
			if ( resource == null )
				continue;

			Object[] entry = new Object[ 6 ];

			entry[ 0 ] = resource.getIdent();
			entry[ 1 ] = resource.getTheResourceType().getIdent();

			if ( resource instanceof Consumable ) {
				Consumable consumable = (Consumable)resource;

				entry[ 2 ] = "Consumable";
				entry[ 3 ] = new Double( consumable.getCost() );
				entry[ 4 ] = consumable.getUnit();
				entry[ 5 ] = consumable.getState();
			}
			else if ( resource instanceof Exclusive ) {
				Exclusive exclusive = (Exclusive)resource;

				entry[ 2 ] = "Exclusive";
				entry[ 3 ] = new Double( exclusive.getCost() );
				entry[ 4 ] = exclusive.getUnitOfCost();
				entry[ 5 ] = exclusive.getState();
			}
			else if ( resource instanceof Shareable ) {
				Shareable shareable = (Shareable)resource;

				entry[ 2 ] = "Shareable";
				entry[ 3 ] = new Double( shareable.getCost() );
				entry[ 4 ] = shareable.getUnitOfCost();
				entry[ 5 ] = shareable.getState();
			}

			result.add( entry );
		}

		return result;
	}

	@Override
	public List<Object[]> getKnowledgeItensReportData(Date initialDate,	Date finalDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String di = format.format(initialDate);
		try {
			initialDate = format.parse(di);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String queryString = "select item.ident, item.date, item.status, item.theAgent.ident, item.theAgent.name from " + KnowledgeItem.class.getName() + " as item " +
							 "where item.date >= :initialDate AND item.date <= :finalDate order by item.ident";

		Query query = this.getPersistenceContext().createQuery( queryString );
		query.setParameter("initialDate", initialDate);
		query.setParameter("finalDate", finalDate);

		List<Object[]> result = query.getResultList();

		return result;
	}

	@Override
	public List<Object[]> getAgentMetricsReportData(String agentId) {
		String queryString = "select metric.agent.ident, metric.agent.name, metric.metricDefinition.name, " +
							"metric.value, metric.unit, metric.periodBegin, metric.periodEnd " +
							"from " + AgentMetric.class.getName() + " as metric where metric.agent.isActive is true " +
							( agentId != null ? "and metric.agent.ident = :agentId " : "" ) +
							"order by metric.agent.name";

		Query query = this.getPersistenceContext().createQuery( queryString );

		if ( agentId != null )
			query.setParameter( "agentId", agentId );

		List<Object[]> metrics = query.getResultList();

		if ( metrics == null || metrics.isEmpty() )
			return new ArrayList<Object[]>();

		return metrics;
	}

	@Override
	public List<Object[]> getArtifactMetricsReportData(String artifactIdent) {
		String queryString = "select metric.artifact.ident, metric.artifact.theArtifactType.ident, " +
				"metric.metricDefinition.name, metric.value, metric.unit, " +
				"metric.periodBegin, metric.periodEnd " +
				"from " + ArtifactMetric.class.getName() + " as metric " +
				( artifactIdent != null ? "where metric.artifact.ident = :artifactIdent " : "" ) +
				"order by metric.artifact.ident";

		Query query = this.getPersistenceContext().createQuery( queryString );

		if ( artifactIdent != null )
			query.setParameter( "artifactIdent", artifactIdent );

		List<Object[]> metrics = query.getResultList();

		if ( metrics == null || metrics.isEmpty() )
			return new ArrayList<Object[]>();
		return metrics;
	}

	@Override
	public List<Object[]> getResourceMetricsReportData(String resourceIdent) {
		String queryString = "select metric.resource.ident, metric.resource.theResourceType.ident, " +
				"metric.metricDefinition.name, metric.value, metric.unit, " +
			 	"metric.periodBegin, metric.periodEnd " +
			 	"from " + ResourceMetric.class.getName() + " as metric " +
			 	( resourceIdent != null ? "where metric.resource.ident = :resourceIdent " : "" ) +
			 	"order by metric.resource.ident";

		Query query = this.getPersistenceContext().createQuery( queryString );

		if ( resourceIdent != null )
			query.setParameter( "resourceIdent", resourceIdent );

		List<Object[]> metrics = query.getResultList();

		if ( metrics == null || metrics.isEmpty() )
			return new ArrayList<Object[]>();

		return metrics;
	}

	@Override
	public List<Object[]> getScheduleData(String processIdent) {
		br.ufpa.labes.spm.domain.retrieveBySecondaryKey( processIdent );
		ProcessModel model = process.getTheProcessModel();

		List<Activity> activities = getActivitiesFromPModelOrderedByPlannedBegin( processIdent, model );

		List<Object[]> resultList = new ArrayList<Object[]>();

		if ( activities == null || activities.isEmpty() ) {
			return resultList;
		}

		CriticalPathMethod criticalPath = new CriticalPathMethod();
		List<String> criticalPathActs;
		try {

			criticalPathActs = criticalPath.getCriticalPath( processIdent, processDAO );
			if ( criticalPathActs == null )
				criticalPathActs = new ArrayList<String>();

			for ( Activity activity : activities )
			{
				if ( activity == null )
					continue;

				Object[] actEntry = new Object[ 10 ];

				actEntry[ 0 ] = processIdent;
				actEntry[ 1 ] = activity.getIdent();
				actEntry[ 2 ] = activity.getName();

				if ( criticalPathActs.contains( activity.getIdent() ) )
					actEntry[ 2 ] = "*" + actEntry[ 2 ];

				StringTokenizer tokenizer = new StringTokenizer( activity.getIdent(), "." );

				tokenizer.nextToken();
				while( tokenizer.hasMoreTokens() )
				{
					actEntry[ 2 ] = "    " + actEntry[ 2 ];
					tokenizer.nextToken();
				}

				if ( activity instanceof Normal )
				{
					Normal normal = (Normal)activity;

					actEntry[ 3 ] = normal.getTheEnactionDescription().getState();

					actEntry[ 4 ] = normal.getPlannedBegin();

					actEntry[ 5 ] = normal.getPlannedEnd();

					actEntry[ 6 ] = getActivityHourEstimation( activity.getIdent());

					actEntry[ 7 ] = normal.getTheEnactionDescription().getActualBegin();

					actEntry[ 8 ] = normal.getTheEnactionDescription().getActualEnd();

					if ( actEntry[ 3 ].equals( Plain.ACTIVE ) ||
							actEntry[ 3 ].equals( Plain.PAUSED ) ||
							actEntry[ 3 ].equals( Plain.FAILED ) ||
							actEntry[ 3 ].equals( Plain.FINISHED ) ) {
						actEntry[ 9 ] = getWorkedHoursForActivity( activity.getIdent());
					}
					else actEntry[ 9 ] = 0.0;
				}
				else if ( activity instanceof Decomposed )
				{
					Decomposed decomposed = (Decomposed)activity;

					actEntry[ 2 ] = actEntry[ 2 ] + "<D>";

					actEntry[ 3 ] = decomposed.getTheReferedProcessModel().getPmState();

					actEntry[ 6 ] = 0.0;

					actEntry[ 9 ] = 0.0;
				}

				resultList.add( actEntry );
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}

	private double getWorkedHoursForActivity( String actIdent) {
		String queryString = "from " + Task.class.getName() + " as t where t.theNormal.ident = '" + actIdent + "'";

		Query query = this.getPersistenceContext().createQuery( queryString );

		List<Task> tasks = query.getResultList();

		if ( tasks == null || tasks.isEmpty() )
			return 0.0;

		double workedHours = 0.0;
		int taskCount = 0;

		for ( Task task : tasks ) {
			if ( task != null )
				taskCount++;
			else continue;

			if ( task.getWorkingHours() != 0.0 )
			{
				workedHours += task.getWorkingHours();
			}
			else
			{
				String agentIdent = task.getTheProcessAgenda().getTheTaskAgenda().getTheAgent().getIdent();

				workedHours += getWorkingHoursForTask( actIdent, agentIdent );
			}
		}

		return workedHours;
	}


	private List<Activity> getActivitiesFromPModelOrderedByPlannedBegin(String process, ProcessModel pModel) {
		Collection<Activity> acts = pModel.getTheActivity();

		List<Activity> listOfActs = new LinkedList<Activity>();

		for( Object obj: acts ) {
			if ( obj == null )
				continue;

			Activity act = (Activity)obj;

			listOfActs.add(act);
		}

		Collections.sort( listOfActs, new OrderByPlannedBegin(process) );

		List<Activity> listOfActsExpanded = new LinkedList<Activity>();
		for(Activity act: listOfActs){
			listOfActsExpanded.add(act);

      if(act instanceof Decomposed){
				Decomposed decAct = (Decomposed) act;
				ProcessModel decPModel = decAct.getTheReferedProcessModel();

				List listOfActsFromDecomposed =
					this.getActivitiesFromPModelOrderedByPlannedBegin(process, decPModel);

				listOfActsExpanded.addAll(listOfActsFromDecomposed);
			}
		}

		return listOfActsExpanded;
	}

	@Override
	public List<Object[]> getDocumentManagementPlanData(String processIdent) {

		br.ufpa.labes.spm.domain.retrieveBySecondaryKey( processIdent );
		ProcessModel model = process.getTheProcessModel();

		List<Activity> activities = getActivitiesFromPModelOrderedByPlannedBegin( processIdent, model );

		List<Object[]> result = new ArrayList<Object[]>();

		if ( activities == null || activities.isEmpty() )
			return result;

		for ( Activity activity : activities ) {
			String activityIdent = activity.getIdent();

			List<String> agents = null;
			List<String> inputArtifacts = null;
			List<String> outputArtifacts = null;

			if ( activity instanceof Decomposed )
			{
				agents = new ArrayList<String>();
				inputArtifacts = new ArrayList<String>();
				outputArtifacts = new ArrayList<String>();
			}
			else
			{
				String agentQueryString = "select reqAgent.theAgent.name " +
											"from " + ReqAgent.class.getName() + " as reqAgent " +
											"where reqAgent.theNormal.ident = :normalIdent " +
											"order by reqAgent.theAgent.name";

				Query agentQuery = this.getPersistenceContext().createQuery( agentQueryString );
				agentQuery.setParameter( "normalIdent", activityIdent );

				agents = agentQuery.getResultList();

				String inputArtifactQueryString = "select involvedArtifact.theArtifact.ident " +
													"from " + InvolvedArtifact.class.getName() + " as involvedArtifact " +
													"where involvedArtifact.inInvolvedArtifacts.ident = :normalIdent " +
													"order by involvedArtifact.theArtifact.ident";

				Query inputArtifactQuery = this.getPersistenceContext().createQuery( inputArtifactQueryString );
				inputArtifactQuery.setParameter( "normalIdent", activityIdent );

				inputArtifacts = inputArtifactQuery.getResultList();

				String outputArtifactQueryString = "select involvedArtifact.theArtifact.ident " +
									"from " + InvolvedArtifact.class.getName() + " as involvedArtifact " +
									"where involvedArtifact.outInvolvedArtifacts.ident = :normalIdent " +
									"order by involvedArtifact.theArtifact.ident";

				Query outputArtifactQuery = this.getPersistenceContext().createQuery( outputArtifactQueryString );
				outputArtifactQuery.setParameter( "normalIdent", activityIdent );

				outputArtifacts = outputArtifactQuery.getResultList();
			}

			Object[] entry = new Object[ 7 ];

			entry[ 0 ] = processIdent;
			entry[ 1 ] = activity.getName();

			if ( activity instanceof Decomposed )
				entry[ 1 ] = entry[ 1 ] + "<D>";

			StringTokenizer tokenizer = new StringTokenizer( activity.getIdent(), "." );

			tokenizer.nextToken();
			while( tokenizer.hasMoreTokens() )
			{
				entry[ 1 ] = "    " + entry[ 1 ];
				tokenizer.nextToken();
			}

			if ( activity instanceof Normal )
			{
				Normal normal = (Normal)activity;

				entry[ 2 ] = normal.getPlannedBegin();
				entry[ 3 ] = normal.getPlannedEnd();
			}

			entry[ 4 ] = ( !agents.isEmpty() ? agents.get( 0 ) : null );
			entry[ 5 ] = ( !inputArtifacts.isEmpty() ? inputArtifacts.get( 0 ) : null );
			entry[ 6 ] = ( !outputArtifacts.isEmpty() ? outputArtifacts.get( 0 ) : null );

			result.add( entry );

			int counter = 1;

			while( counter < agents.size() || counter < inputArtifacts.size() || counter < outputArtifacts.size() ) {
				entry = new Object[ 7 ];

				entry[ 0 ] = processIdent;
				entry[ 1 ] = "";
				entry[ 2 ] = null;
				entry[ 3 ] = null;
				entry[ 4 ] = ( counter < agents.size() ? agents.get( counter ) : null );
				entry[ 5 ] = ( counter < inputArtifacts.size() ? inputArtifacts.get( counter ) : null );
				entry[ 6 ] = ( counter < outputArtifacts.size() ? outputArtifacts.get( counter ) : null );

				result.add( entry );

				counter++;
			}
		}

		return result;
	}

	@Override
	public List<Object[]> getHumanResourcesPlanData(String processIdent) {

		String ReqWorkGroupHql = "select ReqWorkGroup from " + ReqWorkGroup.class.getName() + " as ReqWorkGroup joinCon ReqWorkGroup.theWorkGroup.theAgent as agent " +
								"where agent.ident = task.theProcessAgenda.theTaskAgenda.theAgent.ident";

		String reqAgentHql = "select reqAgent from " + ReqAgent.class.getName() + " as reqAgent " +
								"where reqAgent.theAgent.ident = task.theProcessAgenda.theTaskAgenda.theAgent.ident";

		String queryString = "select task.theNormal.ident, task.theNormal.name, " +
									"reqPeople, task.theProcessAgenda.theTaskAgenda.theAgent.ident, " +
									"task.theProcessAgenda.theTaskAgenda.theAgent.name, task.workingHours " +
								"from " + Task.class.getName() + " as task " +
								"joinCon task.theNormal.theRequiredPeople as reqPeople " +
								"where task.theNormal.ident like :processIdent " +
								"and task.theNormal.ident.isVersion is null " +
								"and (reqPeople in (" + ReqWorkGroupHql + ") or reqPeople in (" + reqAgentHql + ")) " +
								"order by task.theNormal.ident";

		Query query = this.getPersistenceContext().createQuery( queryString );
		query.setParameter("processIdent", processIdent + ".%");

		List<Object[]> activities = query.getResultList();
		List<Object[]> result = new ArrayList<Object[]>();

		if ( activities == null || activities.isEmpty() )
			return result;

		HashMap<String, Object[]> resultMap = new HashMap<String, Object[]>();

		for ( Object[] activity : activities ) {
			Object[] entry = resultMap.get((String)activity[0]);

			if (entry == null) {
				entry = new Object[3];

				entry[ 0 ] = (String)activity[ 0 ];
				entry[ 1 ] = (String)activity[ 1 ];

				StringTokenizer tokenizer = new StringTokenizer( (String)activity[ 0 ], "." );
				tokenizer.nextToken();

				while( tokenizer.hasMoreTokens() )
				{
					entry[ 1 ] = "    " + entry[ 1 ];
					tokenizer.nextToken();
				}

				entry[2] = new ArrayList<Object[]>();

				resultMap.put((String)activity[0], entry);
			}

			Object[] agentEntry = new Object[3];

			if (activity[2] != null) {
				if (activity[2] instanceof ReqWorkGroup) {
					ReqWorkGroup ReqWorkGroup = (ReqWorkGroup)activity[2];

					agentEntry[0] = ReqWorkGroup.getTheWorkGroup().getName();
				}
				else {
					ReqAgent reqAgent = (ReqAgent)activity[2];

					agentEntry[0] = reqAgent.getTheRole().getName();
				}
			}
			else {
				agentEntry[0] = "";
			}

			agentEntry[ 1 ] = activity[ 4 ];

			agentEntry[ 2 ] = new Double( (Float)activity[ 5 ] );

			if ( ( (Double)agentEntry[ 2 ] ) == 0 ) {
				agentEntry[ 2 ] = new Double( (Float) getWorkingHoursForTask( (String)activity[ 0 ], (String)activity[ 3 ] ) );
			}

			((ArrayList<Object[]>)entry[2]).add( agentEntry );
		}

		result.addAll(resultMap.values());

		Collections.sort(result, new OrderByPlannedBeginWorkGroupedByActivity(processIdent) {
			public int compare(Object o1, Object o2) {
				Object[] os1 = (Object[])o1, os2 = (Object[])o2;

				Activity a1 = null;
				Activity a2 = null;

				a1 = (Activity) actMap.get((String)os1[0]);
				a2 = (Activity) actMap.get((String)os2[0]);

				System.out.println("A1: " + a1);
				System.out.println("A2: " + a2);

				return super.compare(a1, a2);
			}
		});

		return result;
	}

	public List<Object[]> getAllocableActivitiesData(String processIdent) {
		String allocableActivitiesQueryString = "from " + Normal.class.getName() + " as normal " +
													"where normal.autoAllocable is true and size(normal.theRequiredPeople) = 0 " +
													"and normal.ident like '" + processIdent + ".%' " +
													"and normal.isVersion is null " +
													"order by normal.ident";

		Query allocableActivitiesQuery = this.getPersistenceContext().createQuery(allocableActivitiesQueryString);

		List<Normal> allocableActivities = allocableActivitiesQuery.getResultList();
		List<Object[]> activities = new ArrayList<Object[]>();

		for (Normal allocableActivity:allocableActivities) {
			Object[] entry = new Object[3];

			entry[0] = allocableActivity.getIdent();
			entry[1] = allocableActivity.getName();

			entry[2] = this.getActivityHourEstimation((String)entry[0]);

			activities.add(entry);
		}

		return activities;
	}

	@Override
	public List<Object[]> getResourcesPlanData(String processIdent) {
		String exclusiveQueryString = "select exclusive.name, exclusive.description, " +
												"reqResource.theNormal.plannedBegin, reqResource.theNormal.plannedEnd " +
										"from " + RequiredResource.class.getName() + " as reqResource, " +
													Exclusive.class.getName() + " as exclusive " +
										"where reqResource.theNormal.ident like '" + processIdent + ".%' " +
											"and reqResource.theResource.ident = exclusive.ident " +
										"order by exclusive.name";

		Query exclusiveQuery = this.getPersistenceContext().createQuery( exclusiveQueryString );

		List<Object[]> exclusives = exclusiveQuery.getResultList();

		String consumableQueryString = "select consumable.name, consumable.description, " +
												"reqResource.theNormal.plannedBegin, reqResource.theNormal.plannedEnd " +
										"from " + RequiredResource.class.getName() + " as reqResource, " +
													Consumable.class.getName() + " as consumable " +
										"where reqResource.theNormal.ident like '" + processIdent + ".%' " +
											"and reqResource.theResource.ident = consumable.ident " +
										"order by consumable.name";

		Query consumableQuery = this.getPersistenceContext().createQuery( consumableQueryString );

		List<Object[]> consumables = consumableQuery.getResultList();

		String shareableQueryString = "select shareable.name, shareable.description, " +
											"reqResource.theNormal.plannedBegin, reqResource.theNormal.plannedEnd " +
										"from " + RequiredResource.class.getName() + " as reqResource, " +
													Shareable.class.getName() + " as shareable " +
										"where reqResource.theNormal.ident like '" + processIdent + ".%' " +
											"and reqResource.theResource.ident = shareable.ident " +
										"order by shareable.name";

		Query shareableQuery = this.getPersistenceContext().createQuery( shareableQueryString );

		List<Object[]> shareables = shareableQuery.getResultList();

		if ( exclusives == null )
			exclusives = new ArrayList<Object[]>();

		if ( consumables == null )
			consumables = new ArrayList<Object[]>();

		if ( shareables == null )
			shareables = new ArrayList<Object[]>();

		List<Object[]> result = new ArrayList<Object[]>();

		Object[] entry = new Object[ 4 ];

		entry[ 0 ] = processIdent;
		entry[ 1 ] = exclusives;
		entry[ 2 ] = consumables;
		entry[ 3 ] = shareables;

		result.add( entry );

		return result;
	}

	@Override
	public List<Object[]> getProjectsBySystemReportData(String systemIdent) {
		String queryString = "from " + Project.class.getName() + " as project " +
				 "where project.theSystem is not null " +
				 ( systemIdent != null ? "and project.theSystem.ident = :systemIdent " : "" ) +
				 "order by project.theSystem.ident, project.ident";

		Query query = this.getPersistenceContext().createQuery( queryString );

		if ( systemIdent != null )
			query.setParameter( "systemIdent", systemIdent );

		List<Project> projects = query.getResultList();
		List<Object[]> result = new ArrayList<Object[]>();

		if ( projects == null || projects.isEmpty() )
			return result;

		for ( Project project : projects ) {
			if ( project == null )
				continue;

			Company organization = project.getTheSystem().getTheOrganization();

			Object[] entry = new Object[ 5 ];

			entry[ 0 ] = project.getTheSystem().getIdent();
			entry[ 1 ] = ( organization != null ? organization.getIdent() : "" );
			entry[ 2 ] = project.getIdent();
			entry[ 3 ] = project.getBegin_date();
			entry[ 4 ] = project.getEnd_date();

			result.add( entry );
		}

		return result;
	}

	@Override
	public List<Object[]> getWorkBreakdownStructureData(String processIdent) {
		Process process = processDAO.retrieveBySecondaryKey( processIdent );
		ProcessModel model = process.getTheProcessModel();

		List<Activity> activities = getActivitiesFromPModelOrderedByPlannedBegin( processIdent, model );

		List<Object[]> result = new ArrayList<Object[]>();

		if ( activities == null || activities.isEmpty() )
			return result;

		for ( Activity activity : activities ) {
			if ( activity == null )
				continue;

			Object[] entry = new Object[ 2 ];

			entry[ 0 ] = processIdent;
			entry[ 1 ] = activity.getName() + ( activity instanceof Decomposed ? " <D>" : "" );

			StringTokenizer tokenizer = new StringTokenizer( activity.getIdent(), "." );

			tokenizer.nextToken();
			while( tokenizer.hasMoreTokens() )
			{
				entry[ 1 ] = "    " + entry[ 1 ];
				tokenizer.nextToken();
			}

			result.add( entry );
		}

		return result;
	}

	@Override
	public List<Object[]> getResourcesCostPlanData(String processIdent) {
		String exclusiveQueryString = "select exclusive.name, exclusive.description, exclusive.cost " +
										"from " + RequiredResource.class.getName() + " as reqResource, " +
													Exclusive.class.getName() + " as exclusive " +
										"where reqResource.theNormal.ident like '" + processIdent + ".%' " +
											"and reqResource.theResource.ident = exclusive.ident " +
										"order by exclusive.name";

		Query exclusiveQuery = this.getPersistenceContext().createQuery( exclusiveQueryString );

		List<Object[]> exclusives = exclusiveQuery.getResultList();

		String consumableQueryString = "select consumable.name, consumable.description, " +
												"reqResource.amountNeeded, consumable.cost " +
										"from " + RequiredResource.class.getName() + " as reqResource, " +
													Consumable.class.getName() + " as consumable " +
										"where reqResource.theNormal.ident like '" + processIdent + ".%' " +
											"and reqResource.theResource.ident = consumable.ident " +
										"order by consumable.name";

		Query consumableQuery = this.getPersistenceContext().createQuery( consumableQueryString );

		List<Object[]> consumables = consumableQuery.getResultList();

		String shareableQueryString = "select shareable.name, shareable.description, " +
											"reqResource.theNormal.plannedBegin, reqResource.theNormal.plannedEnd " +
										"from " + RequiredResource.class.getName() + " as reqResource, " +
													Shareable.class.getName() + " as shareable " +
										"where reqResource.theNormal.ident like '" + processIdent + ".%' " +
											"and reqResource.theResource.ident = shareable.ident " +
										"order by shareable.name";

		Query shareableQuery = this.getPersistenceContext().createQuery( shareableQueryString );

		List<Object[]> shareables = shareableQuery.getResultList();

		String tasksQueryString = "from " + Task.class.getName() + " as task " +
									"where task.theNormal.ident like '" + processIdent + ".%'";

		Query tasksQuery = this.getPersistenceContext().createQuery( tasksQueryString );

		List<Task> tasks = tasksQuery.getResultList();

		String allocableActivitiesQueryString = "from " + Normal.class.getName() + " as normal " +
												"where normal.autoAllocable is true and size(normal.theRequiredPeople) = 0 " +
												"and normal.ident like '" + processIdent + ".%' " +
												"and normal.isVersion is null " +
												"order by normal.ident";

		Query allocableActivitiesQuery = this.getPersistenceContext().createQuery(allocableActivitiesQueryString);

		List<Normal> allocableActivities = allocableActivitiesQuery.getResultList();

		if ( exclusives == null )
			exclusives = new ArrayList<Object[]>();

		if ( consumables == null )
			consumables = new ArrayList<Object[]>();

		if ( shareables == null )
			shareables = new ArrayList<Object[]>();

		List<Object[]> humans = new ArrayList<Object[]>();
		List<Object[]> activities = new ArrayList<Object[]>();

		for (Normal allocableActivity:allocableActivities) {
			Object[] entry = new Object[3];

			entry[0] = allocableActivity.getIdent();
			entry[1] = allocableActivity.getName();

			entry[2] = this.getActivityHourEstimation((String)entry[0]);

			activities.add(entry);
		}

		if ( tasks != null )
		{
			HashMap<String, Object[]> humansHash = new HashMap<String, Object[]>();
			HashMap<String, Integer> numAgentsPerNormal = new HashMap<String, Integer>();

			for (Task currentTask : tasks ) {
				if ( currentTask == null )
					continue;

				Normal normal = currentTask.getTheNormal();
				Agent agent = currentTask.getTheProcessAgenda().getTheTaskAgenda().getTheAgent();

				// Count number of agents working in the normal
				Integer numAgents = numAgentsPerNormal.get(normal.getIdent());

				if (numAgents == null) {
					numAgents = 0;

					for (Object people : normal.getTheRequiredPeople()) {
						if (people instanceof ReqAgent)
							numAgents++;
						else if (people instanceof ReqWorkGroup) {
							ReqWorkGroup ReqWorkGroup = (ReqWorkGroup)people;
							numAgents += ReqWorkGroup.getTheWorkGroup().getTheAgent().size();
						}
					}

					numAgentsPerNormal.put(normal.getIdent(), numAgents);
				}
				//

				if ( normal == null || agent == null )
					continue;

				double workingHours = currentTask.getWorkingHours();

				if (workingHours == 0) {
					workingHours = getWorkingHoursForTask(normal.getIdent(), agent.getIdent());
				}

				if ( humansHash.get( agent.getIdent() ) == null )
				{
					Object[] entry = new Object[ 4 ];

					entry[ 0 ] = agent.getName();
					entry[ 1 ] = new Double( agent.getCostHour() );
					entry[ 2 ] = new Double( workingHours );
					entry[ 3 ] = this.getActivityHourEstimation( normal.getIdent()) / numAgents;

					humansHash.put( agent.getIdent(), entry );
				}
				else
				{
					Object[] entry = humansHash.get( agent.getIdent() );

					entry[ 2 ] = (Double)entry[ 2 ] + workingHours;

					double hourEstimation = this.getActivityHourEstimation( normal.getIdent());
					entry[ 3 ] = (Double)entry[ 3 ] + hourEstimation / numAgents;
				}
			}

			humans.addAll( humansHash.values() );

			Collections.sort(humans, new Comparator<Object[]>() {

				public int compare(Object[] o1, Object[] o2) {
					return o1[0].toString().compareToIgnoreCase(o2.toString());
				}

			});
		}

		List<Object[]> result = new ArrayList<Object[]>();

		Object[] entry = new Object[ 6 ];

		entry[ 0 ] = processIdent;
		entry[ 1 ] = exclusives;
		entry[ 2 ] = consumables;
		entry[ 3 ] = shareables;
		entry[ 4 ] = humans;
		entry[ 5 ] = activities;

		result.add( entry );

		return result;
	}
}
