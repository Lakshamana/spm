package br.ufpa.labes.spm.repository.impl.driver;




import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.driver.IDriverDAO;
import br.ufpa.labes.spm.domain.Driver;



public class DriverDAO extends BaseDAO<Driver, String> implements IDriverDAO{



	protected DriverDAO(Class<Driver> businessClass) {
		super(businessClass);
	}

	public DriverDAO() {
		super(Driver.class);
	}







	}




