package org.qrconsult.spm.services.interfaces;

import javax.ejb.Remote;

@Remote
public interface EditorServices {
	
	public String getActivities( String sessionID );
	public String getEditorContent( String pmodelID );

}
