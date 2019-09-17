package org.qrconsult.spm.services.interfaces;

import javax.ejb.Local;

import org.qrconsult.spm.model.activities.Activity;
import org.qrconsult.spm.model.activities.Plain;
import org.qrconsult.spm.model.connections.Branch;
import org.qrconsult.spm.model.connections.Join;
import org.qrconsult.spm.model.plainActivities.Normal;
import org.qrconsult.spm.model.processModels.Process;
import org.qrconsult.spm.model.processModels.ProcessModel;
import org.qrconsult.spm.model.resources.Resource;
import org.qrconsult.spm.model.taskagenda.Task;

@Local
public interface Logging {

	public abstract void registerModelingActivityEvent(Activity activity,
			String what, String why);

	public abstract void registerGlobalActivityEvent(Plain activity,
			String what, String why);

	public abstract void registerProcessEvent(Process process, String what,
			String why);

	public abstract void registerBranchEvent(Branch branch, String why);

	public abstract void registerJoinEvent(Join join, String why);

	public abstract void registerProcessModelEvent(ProcessModel model,
			String what, String why);

	public abstract void registerResourceEvent(Resource resource,
			Normal actNorm, String what, String why);

	public abstract void registerAgendaEvent(Task task, String what, String why);

}