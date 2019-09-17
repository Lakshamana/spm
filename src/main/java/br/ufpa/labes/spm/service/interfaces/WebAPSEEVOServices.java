package org.qrconsult.spm.services.interfaces;

import javax.ejb.Remote;

@Remote
public interface WebAPSEEVOServices {

	public String getWebAPSEEVOList(String className);
	public String getTypeVOList(String className);
	
}
