package br.ufpa.labes.spm.service.impl;


import java.rmi.RemoteException;
import java.util.Collection;


import br.ufpa.labes.spm.exceptions.DAOException;
import br.ufpa.labes.spm.exceptions.ObjectLockedException;
import br.ufpa.labes.spm.exceptions.UserDeniedException;
import br.ufpa.labes.spm.exceptions.UserInvalidException;
import br.ufpa.labes.spm.exceptions.UserNotManagerException;
import br.ufpa.labes.spm.service.interfaces.NotificationServices;

public class SecurityServicesImpl implements NotificationServices {

	private static NotificationServices instance;

	public static NotificationServices getInstance(){
		if(instance != null){
    		return instance;
    	}
    	else{
    		try {
				instance = new SecurityServicesImpl();
				e.printStackTrace();
				return null;
			}
			return instance;
    	}
	}

	@Override
		// TODO Auto-generated method stub
		return false;
	}

	@Override
		// TODO Auto-generated method stub
		return false;
	}

	@Override
		// TODO Auto-generated method stub
		return false;
	}

	@Override
		// TODO Auto-generated method stub
		return false;
	}

	@Override
		// TODO Auto-generated method stub
		return false;
	}

	@Override
		// TODO Auto-generated method stub
		return null;
	}

	@Override
		// TODO Auto-generated method stub
		return false;
	}

	@Override
		// TODO Auto-generated method stub
		return false;
	}

	@Override
		// TODO Auto-generated method stub
		return false;
	}

	@Override
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float lockObject(String userName, Class classe, Integer obj_oid, int ttl, int ttlbase) throws UserDeniedException,
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean unlockObject(String UserName, Class obj_class, Integer object_oid, float key) throws UserDeniedException, UserNotManagerException,
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLocked(Long oid, Class classe) throws UserDeniedException, UserNotManagerException, UserInvalidException, DAOException,
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String isLockedTo(Long oid, Class classe) throws UserDeniedException, UserNotManagerException, UserInvalidException, DAOException,
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String isLockedTo_with_key(Long oid, Class classe, float key) throws UserDeniedException, UserNotManagerException,
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAgentInProcess(String agent_id, String process_id) throws UserDeniedException, UserNotManagerException, UserInvalidException,
		// TODO Auto-generated method stub
		return false;
	}

	@Override
		// TODO Auto-generated method stub

	}

	@Override
		// TODO Auto-generated method stub

	}

	@Override
		// TODO Auto-generated method stub

	}

	@Override
		// TODO Auto-generated method stub

	}

}
