package br.ufpa.labes.spm.service.impl;

import java.time.LocalDate;
import java.util.Date;


import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Decomposed;
import br.ufpa.labes.spm.domain.Plain;
import br.ufpa.labes.spm.domain.BranchCon;
import br.ufpa.labes.spm.domain.JoinCon;
import br.ufpa.labes.spm.domain.AgendaEvent;
import br.ufpa.labes.spm.domain.GlobalActivityEvent;
import br.ufpa.labes.spm.domain.SpmLog;
import br.ufpa.labes.spm.domain.ModelingActivityEvent;
import br.ufpa.labes.spm.domain.ProcessEvent;
import br.ufpa.labes.spm.domain.ProcessModelEvent;
import br.ufpa.labes.spm.domain.ResourceEvent;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.ProcessModel;
import br.ufpa.labes.spm.domain.Resource;
import br.ufpa.labes.spm.domain.Task;
import br.ufpa.labes.spm.domain.Process;
import br.ufpa.labes.spm.domain.EventType;
import org.qrconsult.spm.notifications.mail.EventNotification;
import br.ufpa.labes.spm.service.interfaces.Logging;

public class LoggingImpl implements Logging {

	public LoggingImpl() {

	}

	/* (non-Javadoc)
	 * @see org.qrconsult.spm.services.impl.Logging#registerModelingActivityEvent(org.qrconsult.spm.model.activities.Activity, java.lang.String, java.lang.String)
	 */
	@Override
	public void registerModelingActivityEvent(Activity activity, String what, String why){
		EventType type = new EventType();
		type.setIdent("Modeling Event" + Math.random()*10 + System.currentTimeMillis());
		type.setDescription("This is an modeling event. Automatically generated.");

		ModelingActivityEvent event = new ModelingActivityEvent();
		event.setTheActivity (activity);
		event.setWhy(why);
		event.setWhen(LocalDate.now());
		event.setIsCreatedByApsee (new Boolean (true));
		event.getTheCatalogEvents().setDescription (what);
		event.setTheEventType(type);
		type.getTheEvents().add(event);
		SpmLog log = this.getTheProcess(activity.getTheProcessModel()).getTheLog();
		event.setTheLog(log);
		log.getTheEvents().add(event);
		activity.getTheModelingActivityEvents().add(event);

		EventNotification.getInstance().notifyEvent( event );
	}

	/* (non-Javadoc)
	 * @see org.qrconsult.spm.services.impl.Logging#registerGlobalActivityEvent(org.qrconsult.spm.model.activities.Plain, java.lang.String, java.lang.String)
	 */
	@Override
	public void registerGlobalActivityEvent(Plain activity, String what, String why){
		EventType type = new EventType();
		type.setIdent("Enactment Event" + Math.random()*10+ System.currentTimeMillis());
		type.setDescription("This is an enactment event. Automatically generated.");

		GlobalActivityEvent event = new GlobalActivityEvent();
		SpmLog log = this.getTheProcess(activity.getTheProcessModel()).getTheLog();
		event.setTheLog(log);
		log.getTheEvents().add(event);
		event.setThePlain(activity);
		event.setWhy (why);
		event.setWhen (LocalDate.now());
		event.setIsCreatedByApsee (new Boolean (true));
		event.getTheCatalogEvents().setDescription (what);
		event.setTheEventType(type);
		type.getTheEvents().add (event);
		activity.getTheGlobalActivityEvents().add (event);

		EventNotification.getInstance().notifyEvent( event );
	}

	/* (non-Javadoc)
	 * @see org.qrconsult.spm.services.impl.Logging#registerProcessEvent(org.qrconsult.spm.model.processModels.Process, java.lang.String, java.lang.String)
	 */
	@Override
	public void registerProcessEvent(Process process, String what, String why){
		EventType type = new EventType();
		type.setIdent("Enactment Event" + Math.random()*10+ System.currentTimeMillis());
		type.setDescription("This is an enactment event. Automatically generated.");

		ProcessEvent event = new ProcessEvent();
		event.setTheProcess(process);
		event.setTheLog(process.getTheLog());
		process.getTheLog().getTheEvents().add(event);
		event.setWhy (why);
		event.setWhen (LocalDate.now());
		event.setIsCreatedByApsee (new Boolean (true));
		event.getTheCatalogEvents().setDescription (what);
		event.setTheEventType(type);
		type.getTheEvents().add (event);
		process.getTheProcessEvents().add (event);

		EventNotification.getInstance().notifyEvent( event );
	}

	/* (non-Javadoc)
	 * @see org.qrconsult.spm.services.impl.Logging#registerBranchEvent(org.qrconsult.spm.model.connections.BranchCon, java.lang.String)
	 */
	@Override
	public void registerBranchEvent(BranchCon branchCon, String why){
/*		EventType type = new EventType();
		type.setIdent("Enactment Event" + Math.random()*10+ System.currentTimeMillis());
		type.setDescription("This is an enactment event. Automatically generated.");

		ConnectionEvent event = new ConnectionEvent();
		SpmLog log = this.getTheProcess(branchCon.getTheProcessModel()).getTheLog();
		event.setTheLog(log);
		log.getTheEvents().add(event);
		event.setWhy (why);
		event.setWhen (LocalDate.now());
		event.setIsCreatedByApsee (new Boolean (true));
		event.getTheCatalogEvents().setDescription ("Fired");
		event.setTheEventType(type);
		type.getTheEvents().add(event);
		branchCon.getTheProcessModel().getProcessModelEvent.add (event);
*/
	}

	/* (non-Javadoc)
	 * @see org.qrconsult.spm.services.impl.Logging#registerJoinEvent(org.qrconsult.spm.model.connections.JoinCon, java.lang.String)
	 */
	@Override
	public void registerJoinEvent(JoinCon joinCon, String why){
/*		EventType type = new EventType();
		type.setIdent("Enactment Event" + Math.random()*10+ System.currentTimeMillis());
		type.setDescription("This is an enactment event. Automatically generated.");

		ConnectionEvent event = new ConnectionEvent();
		SpmLog log = this.getTheProcess(joinCon.getTheProcessModel()).getTheLog();
		event.setTheLog(log);
		log.getTheEvents().add(event);
		event.setWhy (why);
		event.setWhen (LocalDate.now());
		event.setIsCreatedByApsee (new Boolean (true));
		event.getTheCatalogEvents().setDescription ("Fired");
		event.setTheEventType(type);
		type.getTheEvents().add(event);
		joinCon.theProcessModelEvent.add (event);
*/
	}

	/* (non-Javadoc)
	 * @see org.qrconsult.spm.services.impl.Logging#registerProcessModelEvent(org.qrconsult.spm.model.processModels.ProcessModel, java.lang.String, java.lang.String)
	 */
	@Override
	public void registerProcessModelEvent(ProcessModel model, String what, String why){
		EventType type = new EventType();
		type.setIdent("Enactment Event" + Math.random()*10+ System.currentTimeMillis());
		type.setDescription("This is an enactment event. Automatically generated.");

		ProcessModelEvent event = new ProcessModelEvent();
		event.setTheProcessModel(model);
  		SpmLog log = this.getTheProcess(model).getTheLog();
       	event.setTheLog(log);
       	log.getTheEvents().add(event);
       	event.setWhy (why);
       	event.setWhen (LocalDate.now());
       	event.setIsCreatedByApsee (new Boolean (true));
       	event.getTheCatalogEvents().setDescription (what);
       	event.setTheEventType(type);
       	type.getTheEvents().add(event);
       	model.getTheProcessModelEvents().add (event);

       	EventNotification.getInstance().notifyEvent( event );
	}

	/* (non-Javadoc)
	 * @see org.qrconsult.spm.services.impl.Logging#registerResourceEvent(org.qrconsult.spm.model.resources.Resource, org.qrconsult.spm.model.plainActivities.Normal, java.lang.String, java.lang.String)
	 */
	@Override
	public void registerResourceEvent(Resource resource, Normal actNorm, String what, String why){
		EventType type = new EventType();
		type.setIdent("Enactment Event" + Math.random()*10+ System.currentTimeMillis());
		type.setDescription("This is an enactment event. Automatically generated.");

		ResourceEvent event = new ResourceEvent();
		event.setTheResource(resource);
		if(actNorm != null){
		    event.setTheNormal(actNorm);
			SpmLog log = this.getTheProcess(actNorm.getTheProcessModel()).getTheLog();
			event.setTheLog(log);
			log.getTheEvents().add(event);

		}
		event.setWhy (why);
		event.setWhen (LocalDate.now());
		event.setIsCreatedByApsee (new Boolean (false));
		event.getTheCatalogEvents().setDescription (what);
		event.setTheEventType(type);
		type.getTheEvents().add (event);
		resource.getTheResourceEvents().add (event);

		EventNotification.getInstance().notifyEvent( event );
	}

	/* (non-Javadoc)
	 * @see org.qrconsult.spm.services.impl.Logging#registerAgendaEvent(org.qrconsult.spm.model.taskagenda.Task, java.lang.String, java.lang.String)
	 */
	@Override
	public void registerAgendaEvent(Task task, String what, String why){
		EventType type = new EventType();
		type.setIdent("Enactment Event" + Math.random()*10+ System.currentTimeMillis());
		type.setDescription("This is an enactment event. Automatically generated.");

		AgendaEvent agEvent = new AgendaEvent();
		agEvent.setIsCreatedByApsee(new Boolean(false));
		SpmLog log = this.getTheProcess(task.getTheNormal().getTheProcessModel()).getTheLog();
		agEvent.setTheLog(log);
		log.getTheEvents().add(agEvent);
		agEvent.setWhen(new Date());
		agEvent.setWhy(why);
		agEvent.getTheCatalogEvents().setDescription(what);
		agEvent.getTheCatalogEvents().setTheAgendaEvent(agEvent);
		agEvent.setTheTask(task);
		agEvent.setTheEventType(type);
		type.getTheEvents().add(agEvent);
		task.getTheAgendaEvents().add(agEvent);

		EventNotification.getInstance().notifyEvent( agEvent );
	}

	// Utility

	private Process getTheProcess(ProcessModel pmodel){

	    Process process = null;
		Decomposed decAct = pmodel.getTheDecomposed();

		if(decAct == null)
			process = pmodel.getTheProcess();
		else
		    process = this.getTheProcess(decAct.getTheProcessModel());
	    return process;
	}
}
