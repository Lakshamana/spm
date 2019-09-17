package org.qrconsult.spm.services.interfaces;

import javax.ejb.Remote;
import org.qrconsult.spm.dtos.formTool.ToolDTO;
import org.qrconsult.spm.dtos.formTool.ToolsDTO;
import org.qrconsult.spm.dtos.formTypes.TypesDTO;

@Remote
public interface ToolServices {
	public TypesDTO getToolTypes();
	
	public ToolDTO saveTool(ToolDTO toolDTO);
	
	public ToolDTO getTool(String toolName);
	
	public ToolsDTO getTools();
	
	public ToolsDTO getTools(String toolName, String toolType, String artifact, Boolean isActive);
	
	public Boolean removeArtifactFromTool(String artifactName, ToolDTO tool);
	
	public Boolean removeTool(String toolName);
}
