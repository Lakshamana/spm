package org.qrconsult.spm.services.impl;

import java.util.Date;

import javax.ejb.Stateless;

import org.qrconsult.spm.model.activities.Activity;
import org.qrconsult.spm.model.activities.Decomposed;
import org.qrconsult.spm.model.activities.Plain;
import org.qrconsult.spm.model.connections.Branch;
import org.qrconsult.spm.model.connections.Join;
import org.qrconsult.spm.model.log.AgendaEvent;
import org.qrconsult.spm.model.log.GlobalActivityEvent;
import org.qrconsult.spm.model.log.Log;
import org.qrconsult.spm.model.log.ModelingActivityEvent;
import org.qrconsult.spm.model.log.ProcessEvent;
import org.qrconsult.spm.model.log.ProcessModelEvent;
import org.qrconsult.spm.model.log.ResourceEvent;
import org.qrconsult.spm.model.plainActivities.Normal;
import org.qrconsult.spm.model.processModels.ProcessModel;
import org.qrconsult.spm.model.resources.Resource;
import org.qrconsult.spm.model.taskagenda.Task;
import org.qrconsult.spm.model.processModels.Process;
import org.qrconsult.spm.model.types.EventType;
import org.qrconsult.spm.notifications.mail.EventNotification;
import org.qrconsult.spm.services.interfaces.Logging;

@Stateless
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
		event.setWhy (why);
		event.setWhen (new Date());
		event.setIsCreatedByApsee (new Boolean (true));
		event.getTheCatalogEvents().setDescription (what);
		event.setTheEventType(type);
		type.getTheEvent().add (event);
		Log log = this.getTheProcess(activity.getTheProcessModel()).getTheLog();
		event.setTheLog(log);
		log.getTheEvent().add(event);
		activity.getTheModelingActivityEvent().add (event);
		
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
		Log log = this.getTheProcess(activity.getTheProcessModel()).getTheLog();
		event.setTheLog(log);
		log.getTheEvent().add(event);
		event.setThePlain(activity);
		event.setWhy (why);
		event.setWhen (new Date());
		event.setIsCreatedByApsee (new Boolean (true));
		event.getTheCatalogEvents().setDescription (what);
		event.setTheEventType(type);
		type.getTheEvent().add (event);
		activity.getTheGlobalActivityEvent().add (event);

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
		process.getTheLog().getTheEvent().add(event);
		event.setWhy (why);
		event.setWhen (new Date());
		event.setIsCreatedByApsee (new Boolean (true));
		event.getTheCatalogEvents().setDescription (what);
		event.setTheEventType(type);
		type.getTheEvent().add (event);
		process.getTheProcessEvent().add (event);
		
		EventNotification.getInstance().notifyEvent( event );
	}
	
	/* (non-Javadoc)
	 * @see org.qrconsult.spm.services.impl.Logging#registerBranchEvent(org.qrconsult.spm.model.connections.Branch, java.lang.String)
	 */
	@Override
	public void registerBranchEvent(Branch branch, String why){
/*		EventType type = new EventType();
		type.setIdent("Enactment Event" + Math.random()*10+ System.currentTimeMillis());
		type.setDescription("This is an enactment event. Automatically generated.");

		ConnectionEvent event = new ConnectionEvent();
		Log log = this.getTheProcess(branch.getTheProcessModel()).getTheLog();
		event.setTheLog(log);
		log.getTheEvent().add(event);
		event.setWhy (why);
		event.setWhen (new Date());
		event.setIsCreatedByApsee (new Boolean (true));
		event.getTheCatalogEvents().setDescription ("Fired");
		event.setTheEventType(type);
		type.getTheEvent().add(event);
		branch.getTheProcessModel().getProcessModelEvent.add (event);
*/
	}
	
	/* (non-Javadoc)
	 * @see org.qrconsult.spm.services.impl.Logging#registerJoinEvent(org.qrconsult.spm.model.connections.Join, java.lang.String)
	 */
	@Override
	public void registerJoinEvent(Join join, String why){
/*		EventType type = new EventType();
		type.setIdent("Enactment Event" + Math.random()*10+ System.currentTimeMillis());
		type.setDescription("This is an enactment event. Automatically generated.");

		ConnectionEvent event = new ConnectionEvent();
		Log log = this.getTheProcess(join.getTheProcessModel()).getTheLog();
		event.setTheLog(log);
		log.getTheEvent().add(event);
		event.setWhy (why);
		event.setWhen (new Date());
		event.setIsCreatedByApsee (new Boolean (true));
		event.getTheCatalogEvents().setDescription ("Fired");
		event.setTheEventType(type);
		type.getTheEvent().add(event);
		join.theProcessModelEvent.add (event);
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
  		Log log = this.getTheProcess(model).getTheLog();
       	event.setTheLog(log);
       	log.getTheEvent().add(event);
       	event.setWhy (why);
       	event.setWhen (new Date());
       	event.setIsCreatedByApsee (new Boolean (true));
       	event.getTheCatalogEvents().setDescription (what);
       	event.setTheEventType(type);
       	type.getTheEvent().add(event);
       	model.getTheProcessModelEvent().add (event);
       	
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
			Log log = this.getTheProcess(actNorm.getTheProcessModel()).getTheLog();
			event.setTheLog(log);
			log.getTheEvent().add(event);

		}
		event.setWhy (why);
		event.setWhen (new Date());
		event.setIsCreatedByApsee (new Boolean (false));
		event.getTheCatalogEvents().setDescription (what);
		event.setTheEventType(type);
		type.getTheEvent().add (event);
		resource.getTheResourceEvent().add (event);
		
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
		Log log = this.getTheProcess(task.getTheNormal().getTheProcessModel()).getTheLog();
		agEvent.setTheLog(log);
		log.getTheEvent().add(agEvent);
		agEvent.setWhen(new Date());
		agEvent.setWhy(why);
		agEvent.getTheCatalogEvents().setDescription(what);
		agEvent.getTheCatalogEvents().setTheAgendaEvent(agEvent);
		agEvent.setTheTask(task);
		agEvent.setTheEventType(type);
		type.getTheEvent().add(agEvent);
		task.getTheAgendaEvent().add(agEvent);
		
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
