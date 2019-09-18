package br.ufpa.labes.spm.service.interfaces;

import javax.ejb.Local;

import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Plain;
import br.ufpa.labes.spm.domain.BranchCon;
import br.ufpa.labes.spm.domain.JoinCon;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.Process;
import br.ufpa.labes.spm.domain.ProcessModel;
import br.ufpa.labes.spm.domain.Resource;
import br.ufpa.labes.spm.domain.Task;

@Local
public interface Logging {

	public abstract void registerModelingActivityEvent(Activity activity,
			String what, String why);

	public abstract void registerGlobalActivityEvent(Plain activity,
			String what, String why);

	public abstract void registerProcessEvent(Process process, String what,
			String why);

	public abstract void registerBranchEvent(Branch branchCon, String why);

	public abstract void registerJoinEvent(Join joinCon, String why);

	public abstract void registerProcessModelEvent(ProcessModel model,
			String what, String why);

	public abstract void registerResourceEvent(Resource resource,
			Normal actNorm, String what, String why);

	public abstract void registerAgendaEvent(Task task, String what, String why);

}
