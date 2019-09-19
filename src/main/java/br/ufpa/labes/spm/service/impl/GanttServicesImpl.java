package br.ufpa.labes.spm.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;

import br.ufpa.labes.spm.repository.interfaces.activities.IActivityDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IProjectDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessDAO;
import br.ufpa.labes.spm.service.dto.ActivityDTO;
import br.ufpa.labes.spm.service.dto.ActivitysDTO;
import br.ufpa.labes.spm.service.dto.DecomposedDTO;
import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Decomposed;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.domain.RequiredPeople;
import br.ufpa.labes.spm.domain.Process;
import br.ufpa.labes.spm.domain.ProcessModel;
import br.ufpa.labes.spm.domain.Task;
import br.ufpa.labes.spm.service.interfaces.CalendarServices;
import br.ufpa.labes.spm.service.interfaces.EasyModelingServices;
import br.ufpa.labes.spm.service.interfaces.GanttServices;
import br.ufpa.labes.spm.util.ServicesUtil;

public class GanttServicesImpl implements GanttServices {

	private static final String ACTIVITY_CLASSNAME = Activity.class.getSimpleName();

	IProcessDAO processDAO;

	IActivityDAO activityDAO;

	IProjectDAO projectDAO;
	EasyModelingServices easyModelingServices;

	CalendarServices calendarServices;

	final int MILLIS_PER_DAY = 1000 * 60 * 60 * 24;
	private long minCalendar = -1;
	private long maxCalendar = -1;
	private int duration;
	private Date plannedBegin;
	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

	private Query query;

	@Override
	public ActivitysDTO getGanttActivities(String processIdent) {
		Process process = (Process) processDAO
				.retrieveBySecondaryKey(processIdent);
		ProcessModel model = process.getTheProcessModel();

		ActivitysDTO gantt = new ActivitysDTO();
		List<ActivityDTO> lista = new ArrayList<ActivityDTO>();
		gantt = buildGantt(model, lista);

		return gantt;
	}

	@Override
	public String alo() {
	}

	private static GanttServices lookupService() throws NamingException {
		final Hashtable jndiProperties = new Hashtable();
		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		jndiProperties.put(Context.PROVIDER_URL, "remote://localhost:4447");
	    jndiProperties.put(Context.SECURITY_PRINCIPAL, "admin");
	    jndiProperties.put(Context.SECURITY_CREDENTIALS, "123");
	    jndiProperties.put("jboss.naming.client.ejb.context", true);

		final Context context = new InitialContext(jndiProperties);
		final String appName = "";
		final String moduleName = "jboss-as-ejb-remote-app";
		final String distinctName = "";
		final String beanName = GanttServicesImpl.class.getSimpleName();
		final String viewClassName = GanttServices.class.getSimpleName();

		String enderecoLookup = "ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "/" + viewClassName;
		System.out.println(enderecoLookup);
		return (GanttServices) context.lookup(enderecoLookup);
	}

	public static void main(String[] args) {
//		Class<GanttServices> classe = GanttServices.class;
//		try {
////			String enderecoLookup = "java:global/SPMEar/SPMServices/" + classe.getSimpleName() + "Impl!br.ufpa.labes.spm.service.interfaces." + classe.getSimpleName();
//			String enderecoLookup = "GanttServices";
//			GanttServices ejb = (GanttServices) new InitialContext().lookup(enderecoLookup);
//			System.out.println(ejb.alo());
//
//		} catch (NamingException e) {
//			System.out.println("Erro ao fazer o lookup do serviço " + classe.getSimpleName());
//			e.printStackTrace();
//		}
		try {
			GanttServices ejb = lookupService();
			System.out.println(ejb.alo());
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean updateGanttTask(ActivityDTO activityDTO) {

//		//mudar o gethoursworked para getDuration
//
//		SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
//
//		Date begin =  new Date();
//		try {
//			begin =  sdf1.parse(activityDTO.getPlannedBegin());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("caiu aqui no gant 2" + begin);
//
//		Date end =  new Date();
//
//		int duraction = activityDTO.getHoursWorked().intValue();
//		end = new Date(begin.getTime() +(duraction * MILLIS_PER_DAY));
//
//		long dateBegin = begin.getTime();
//		long dateEnd = end.getTime();
//		System.out.println("data inicial "+ dateBegin+"data final "+dateEnd);
//
//		String project_oid =  activityDTO.getIdent();
//		String ident[] = project_oid.split("\\.");
//
//		System.out.println("id projeto "+ ident[0]);
//		Project project =  projectDAO.retrieveBySecondaryKey(ident[0]);
//
//		CalendarDTO replain = calendarServices.getCalendarForProject(project.getId());
//
//
//		easyModelingServices.replanningDates(activityDTO.getIdent(),dateBegin,dateEnd,replain);

		String hql = "SELECT o FROM " + ACTIVITY_CLASSNAME + " o where o.ident = '" + activityDTO.getIdent() + "'";
		query = activityDAO.getPersistenceContext().createQuery(hql);

		List<Activity> result = query.getResultList();

		if(!result.isEmpty()) {
			Activity activity = result.get(0);
			activity.setName(activityDTO.getName());
			Date plannedBegin = new Date(activityDTO.getPlannedBegin());
			if(activity instanceof Normal) {
				Normal normal = (Normal)activity;
				normal.setPlannedBegin(plannedBegin);
				activityDAO.update(normal);
			}
		}

		return false;
	}

	private ActivitysDTO buildGantt(ProcessModel model,
			List<ActivityDTO> lista) {
		ActivitysDTO acts = null;
		try {
			lista.addAll(this.getActivitiesHierarchy(model));
			acts = new ActivitysDTO(lista);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acts;
	}

	public List<ActivityDTO> getActivitiesHierarchy(ProcessModel model) {
		Collection<Activity> activities = model.getTheActivity();
		List<ActivityDTO> activitiesDTO = new ArrayList<ActivityDTO>();

		Iterator<Activity> activityIterator = activities.iterator();
		while (activityIterator.hasNext()) {
			Object activity = activityIterator.next();
			ActivityDTO act = null;

			if(activity instanceof Decomposed) {
				Decomposed decomposed = (Decomposed) activity;
//				System.out.println("D ==> " + decomposed.getName() + " - " + this.getBeginDate(decomposed.getTheReferedProcessModel().getTheActivity()));
				act = this.convertDecomposedToDecomposedDTO(decomposed);
			}

			if(activity instanceof Normal) {
				Normal normal = (Normal) activity;
//				System.out.println("\tN ==> " + normal.getName() + " - " + normal.getPlannedBegin());
				if(normal.getPlannedBegin() != null) {
					act = this.convertNormalToActivityDTO(normal, "");
				}
			}

			if(act != null) {
				activitiesDTO.add(act);
			}
		}
		return activitiesDTO;
	}

	public List<ActivityDTO> getActivitiesHierarchy(ProcessModel model, String parent) {
		Collection<Activity> activities = model.getTheActivity();
		List<ActivityDTO> activitiesDTO = new ArrayList<ActivityDTO>();

		Iterator activityIterator = activities.iterator();
		while (activityIterator.hasNext()) {
			Object activity = activityIterator.next();
			ActivityDTO act = null;
			if(activity instanceof Decomposed) {
				Decomposed decomposed = (Decomposed) activity;
				act = this.convertDecomposedToDecomposedDTO(decomposed, parent);
			}

			if(activity instanceof Normal) {
				Normal normal = (Normal) activity;
				if(normal.getPlannedBegin() != null) {
					act = this.convertNormalToActivityDTO(normal, parent);
				}
			}

			if(act != null) {
				activitiesDTO.add(act);
			}
		}
		return activitiesDTO;
	}

	private DecomposedDTO convertDecomposedToDecomposedDTO(Decomposed decomposed, String parent) {
		ActivitysDTO dtos = new ActivitysDTO();
		ProcessModel model = decomposed.getTheReferedProcessModel();

		DecomposedDTO decomposedDTO = new DecomposedDTO();
		decomposedDTO.setId(decomposed.getId());
		decomposedDTO.setName(decomposed.getName());
		decomposedDTO.setIdent(decomposed.getIdent());

		List<ActivityDTO> childs = this.getActivitiesHierarchy(model, "");
		dtos.getActivitys().addAll(childs);

		Date plannedBeginDate = this.getBeginDate(model.getTheActivity());
		Date plannedEndDate = this.getEndDate(model.getTheActivity());

		decomposedDTO.setDuration(String.valueOf(duration));
		decomposedDTO.setPlannedBegin(format.format(plannedBeginDate));
		decomposedDTO.setPlannedEnd(format.format(plannedEndDate));
		decomposedDTO.setPercentCompleted("50");
		decomposedDTO.setNormals(dtos);

		return decomposedDTO;
	}

	private DecomposedDTO convertDecomposedToDecomposedDTO(Decomposed decomposed) {
		@SuppressWarnings("deprecation")
		Date plannedEnd = new Date(2008, 12, 01);
		duration = 20;
		ActivitysDTO dtos = new ActivitysDTO();
		ProcessModel model = decomposed.getTheReferedProcessModel();

		DecomposedDTO decomposedDTO = new DecomposedDTO();
		decomposedDTO.setId(decomposed.getId());
		decomposedDTO.setName(decomposed.getName());
		decomposedDTO.setIdent(decomposed.getIdent());

		List<ActivityDTO> childs = this.getActivitiesHierarchy(model, "");
		dtos.getActivitys().addAll(childs);

		Date plannedBeginDate = this.getBeginDate(model.getTheActivity());
		Date plannedEndDate = this.getEndDate(model.getTheActivity());

		decomposedDTO.setDuration(String.valueOf(duration));
		decomposedDTO.setPlannedBegin(format.format(plannedBeginDate));
		decomposedDTO.setPlannedEnd(format.format(plannedEndDate));
		decomposedDTO.setPercentCompleted("50");
		decomposedDTO.setNormals(dtos);
		duration = 0;
//		System.out.println("D ==> " + decomposed.getName() + " - " + plannedBeginDate);

		return decomposedDTO;
	}

	private ActivityDTO convertNormalToActivityDTO(Normal normal, String parent) {
		@SuppressWarnings("deprecation")
		Date plannedEnd = new Date(2008, 12, 01);
		duration = 20;

//		System.out.println("Begin: " + normal.getPlannedBegin() + " - End: " + normal.getPlannedEnd());

		ActivityDTO activityDTO = new ActivityDTO();
		activityDTO.setId(normal.getId());
		activityDTO.setName(normal.getName());
		activityDTO.setIdent(normal.getIdent());
		activityDTO.setBeginDate(normal.getPlannedBegin());
		activityDTO.setEndDate(normal.getPlannedEnd());

		activityDTO.setPlannedBegin(format.format(normal.getPlannedBegin()));
		activityDTO.setPlannedEnd(format.format(normal.getPlannedEnd()));

		int duracao = ServicesUtil.diasEntre(normal.getPlannedBegin(), normal.getPlannedEnd()) + 1;
		activityDTO.setDuration(new Integer(duracao).toString());

		String state = this.getNormalState(normal);
		activityDTO.setState(state);

		activityDTO.setParent(parent);
		List<String> reqAgents = new ArrayList<String>();

		Collection<RequiredPeople> r = normal.getTheRequiredPeople();
		for (RequiredPeople requiredPeople : r) {
			if(requiredPeople instanceof ReqAgent) {
				Agent theAgent = ((ReqAgent) requiredPeople).getTheAgent();
				if(theAgent != null) reqAgents.add(theAgent.getName());
			}
		}
		activityDTO.setReqAgents(reqAgents);

//		System.out.println("==> " + normal.getName() + " - " + normal.getPlannedBegin());
		if(normal.getPlannedBegin() != null
				&& normal.getPlannedEnd() != null) {
			int horas = ServicesUtil.horasEntre(normal.getPlannedBegin(), normal.getPlannedEnd());
			duration += horas;
			activityDTO.setDuration(String.valueOf(duration));
		}

		activityDTO.setPercentCompleted("50");
		return activityDTO;
	}

	private String getNormalState(Normal normal) {
		//Retorna o 1° LocalState das Tasks (Ver com o Ernani como funciona o relacionamento Normal->Tasks)
		if(!normal.getTheTasks().isEmpty()) {
			for (Task task : normal.getTheTasks()) {
				return task.getLocalState();
			}
		}

		return "";
	}

	public Date getBeginDate(Collection<Activity> activities) {
		Date begin = new Timestamp(new Date().getTime());

		for (Activity activity : activities) {
			if(activity instanceof Normal) {
				Normal normal = (Normal) activity;
//				System.out.println(normal.getName() + " --------------> " + normal.getPlannedBegin());
				if(begin == null) {
					begin = normal.getPlannedBegin();
				} else {
					boolean plannedNotNull = normal.getPlannedBegin() != null;
//					boolean beginAfterPlanned = begin.after(normal.getPlannedBegin());
					if(plannedNotNull && begin.after(normal.getPlannedBegin())) {
						begin = normal.getPlannedBegin();
					}
				}
			}

			if(activity instanceof Decomposed) {
				Decomposed decomposed = (Decomposed) activity;
				Collection<Activity> theActivity = decomposed.getTheReferedProcessModel().getTheActivity();
//				System.out.println("Size: " + theActivity.size());
//				if(!theActivity.isEmpty()) {
					Date d = this.getBeginDate(theActivity);
					if((d != null) && (!d.equals(new Date())))
						begin = d;
//				}
			}
		}

		return begin;
	}

	public Date getEndDate(Collection<Activity> activities) {
		Date end = new Timestamp(1, 1, 1, 1, 1, 1,0);

		for (Activity activity : activities) {
			if(activity instanceof Normal) {
				Normal normal = (Normal) activity;
//				System.out.println(normal.getName() + " --------------> " + normal.getPlannedBegin());
				if(end == null) {
					end = normal.getPlannedBegin();
				} else {
					boolean plannedNotNull = null != normal.getPlannedBegin();
					if(plannedNotNull && end.before(normal.getPlannedBegin())) {
						end = normal.getPlannedBegin();
					}
				}
			}

			if(activity instanceof Decomposed) {
				Decomposed decomposed = (Decomposed) activity;
				ProcessModel model = decomposed.getTheReferedProcessModel();
				Collection<Activity> theActivity = model.getTheActivity();
//				System.out.println("Size: " + theActivity.size());
//				if(!theActivity.isEmpty()) {
				Date d = this.getBeginDate(theActivity);
				if((d != null) && (!d.equals(new Date())))
					end = d;
//				}
			}
		}

		return end;
	}


}
