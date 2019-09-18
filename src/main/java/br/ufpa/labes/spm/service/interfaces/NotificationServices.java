package br.ufpa.labes.spm.service.interfaces;

import java.rmi.RemoteException;
import java.util.Collection;


import br.ufpa.labes.spm.exceptions.DAOException;
import br.ufpa.labes.spm.exceptions.ObjectLockedException;
import br.ufpa.labes.spm.exceptions.UserDeniedException;
import br.ufpa.labes.spm.exceptions.UserInvalidException;
import br.ufpa.labes.spm.exceptions.UserNotManagerException;

@Remote
public interface NotificationServices {


	public abstract boolean isAgentInGroup(String agent_id, String group_id)

	public abstract boolean isActivityInTasks(String id_activity,

	public abstract boolean isOutOfWorkPeriod(String agent_id)

	public abstract boolean isManagerInProcess(String agent_id,


	public abstract boolean isSubordTo(String agent_subord_id,




	public abstract float lockObject(String userName, Class classe,
			Integer obj_oid, int ttl, int ttlbase) throws
			UserDeniedException, UserNotManagerException, UserInvalidException,

	public abstract boolean unlockObject(String UserName, Class obj_class,
			Integer object_oid, float key) throws
			UserDeniedException, UserNotManagerException, UserInvalidException,

	public abstract boolean isLocked(Integer oid, Class classe)
			throws UserDeniedException,
			UserNotManagerException, UserInvalidException, DAOException,

	public abstract String isLockedTo(Integer oid, Class classe)
			throws UserDeniedException,
			UserNotManagerException, UserInvalidException, DAOException,

	public abstract String isLockedTo_with_key(Integer oid, Class classe,
			float key) throws UserDeniedException,
			UserNotManagerException, UserInvalidException, DAOException,

	public abstract boolean isAgentInProcess(String agent_id, String process_id)
			throws UserDeniedException,







}
