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

	public abstract boolean isAgent(String name, String password) throws RemoteException;

	public abstract boolean isAgentInGroup(String agent_id, String group_id)
			throws DAOException, RemoteException;

	public abstract boolean isActivityInTasks(String id_activity,
			String agent_id, String process_id) throws DAOException, RemoteException;

	public abstract boolean isOutOfWorkPeriod(String agent_id)
			throws DAOException, RemoteException;

	public abstract boolean isManagerInProcess(String agent_id,
			String process_id) throws DAOException, RemoteException;

	public abstract Collection<String> getUsersInSession() throws DAOException, RemoteException;

	public abstract boolean isSubordTo(String agent_subord_id,
			String agent_chef_id) throws RemoteException ;

	public abstract boolean login(String name, String password) throws RemoteException ;

	public abstract boolean hasLogedIn(String name, String password) throws RemoteException;

	public abstract boolean logoff(String name, String clientId) throws RemoteException;

	public abstract float lockObject(String userName, Class classe,
			Integer obj_oid, int ttl, int ttlbase) throws
			UserDeniedException, UserNotManagerException, UserInvalidException,
			DAOException, ObjectLockedException, RemoteException;

	public abstract boolean unlockObject(String UserName, Class obj_class,
			Integer object_oid, float key) throws
			UserDeniedException, UserNotManagerException, UserInvalidException,
			DAOException, ObjectLockedException, RemoteException;

	public abstract boolean isLocked(Integer oid, Class classe)
			throws UserDeniedException,
			UserNotManagerException, UserInvalidException, DAOException,
			ObjectLockedException, RemoteException;

	public abstract String isLockedTo(Integer oid, Class classe)
			throws UserDeniedException,
			UserNotManagerException, UserInvalidException, DAOException,
			ObjectLockedException, RemoteException;

	public abstract String isLockedTo_with_key(Integer oid, Class classe,
			float key) throws UserDeniedException,
			UserNotManagerException, UserInvalidException, DAOException,
			ObjectLockedException, RemoteException;

	public abstract boolean isAgentInProcess(String agent_id, String process_id)
			throws UserDeniedException,
			UserNotManagerException, UserInvalidException, DAOException, RemoteException;

	public abstract void sendMessage(String msg) throws RemoteException;

	public abstract void sendMessageToGroup(String msg, Collection<String> names) throws RemoteException;

	public abstract void sendMessageToUser(String msg, String userName) throws RemoteException;

	public abstract void registerCallbackServiceListener(final String rmihost,final String rmiport,final String rmiservicename) throws RemoteException;
}
