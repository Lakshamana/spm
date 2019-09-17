package org.qrconsult.spm.services.impl;


import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.qrconsult.spm.exceptions.DAOException;
import org.qrconsult.spm.exceptions.ObjectLockedException;
import org.qrconsult.spm.exceptions.UserDeniedException;
import org.qrconsult.spm.exceptions.UserInvalidException;
import org.qrconsult.spm.exceptions.UserNotManagerException;
import org.qrconsult.spm.services.interfaces.NotificationServices;

@Stateless
public class SecurityServicesImpl implements NotificationServices {
	
	private static NotificationServices instance;
	
	public static NotificationServices getInstance(){
		if(instance != null){
    		return instance;
    	}
    	else{
    		try {
				instance = new SecurityServicesImpl();
			} catch (EJBException e) {
				e.printStackTrace();
				return null;
			}
			return instance; 
    	}
	}

	@Override
	public boolean isAgent(String name, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAgentInGroup(String agent_id, String group_id) throws DAOException, RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isActivityInTasks(String id_activity, String agent_id, String process_id) throws DAOException, RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOutOfWorkPeriod(String agent_id) throws DAOException, RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isManagerInProcess(String agent_id, String process_id) throws DAOException, RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<String> getUsersInSession() throws DAOException, RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSubordTo(String agent_subord_id, String agent_chef_id) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean login(String name, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasLogedIn(String name, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean logoff(String name, String clientId) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float lockObject(String userName, Class classe, Integer obj_oid, int ttl, int ttlbase) throws UserDeniedException,
			UserNotManagerException, UserInvalidException, DAOException, ObjectLockedException, RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean unlockObject(String UserName, Class obj_class, Integer object_oid, float key) throws UserDeniedException, UserNotManagerException,
			UserInvalidException, DAOException, ObjectLockedException, RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLocked(Integer oid, Class classe) throws UserDeniedException, UserNotManagerException, UserInvalidException, DAOException,
			ObjectLockedException, RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String isLockedTo(Integer oid, Class classe) throws UserDeniedException, UserNotManagerException, UserInvalidException, DAOException,
			ObjectLockedException, RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String isLockedTo_with_key(Integer oid, Class classe, float key) throws UserDeniedException, UserNotManagerException,
			UserInvalidException, DAOException, ObjectLockedException, RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAgentInProcess(String agent_id, String process_id) throws UserDeniedException, UserNotManagerException, UserInvalidException,
			DAOException, RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void sendMessage(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessageToGroup(String msg, Collection<String> names) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessageToUser(String msg, String userName) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerCallbackServiceListener(String rmihost, String rmiport, String rmiservicename) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
}
