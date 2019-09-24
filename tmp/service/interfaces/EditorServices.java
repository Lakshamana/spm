package br.ufpa.labes.spm.service.interfaces;


@Remote
public interface EditorServices {

	public String getActivities( String sessionID );
	public String getEditorContent( String pmodelID );

}
