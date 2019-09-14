package br.ufpa.labes.spm.repository.interfaces;

import java.util.Properties;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.ufpa.labes.spm.repository.interfaces.activities.IActivityDAO;
import br.ufpa.labes.spm.repository.interfaces.activities.IDecomposedDAO;

public class IDAOFactory {
	
	@EJB
	private IActivityDAO iActivityDAO;
	
	@SuppressWarnings("unused")
	@EJB
	private IDecomposedDAO iDecomposedDAO;
	
	public IDAOFactory(){
		try {
			Properties properties = new Properties();
			properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
			InitialContext context = new InitialContext(properties);
			Object obj = context.lookup("ActivityDAOLocal");
			iActivityDAO = (IActivityDAO) obj;
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public Object getIDAO(String daoName){
		//TO-DO
		return iActivityDAO;
	}

}
