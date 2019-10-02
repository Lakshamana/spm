package br.ufpa.labes.spm.service.interfaces;

import br.ufpa.labes.spm.service.dto.ToolDTO;
import br.ufpa.labes.spm.service.dto.ToolsDTO;
import br.ufpa.labes.spm.service.dto.TypesDTO;

public interface ToolServices {
  public TypesDTO getToolTypes();

  public ToolDTO saveTool(ToolDTO toolDTO);

  public ToolDTO getTool(String toolName);

  public ToolsDTO getTools();

  public ToolsDTO getTools(String toolName, String toolType, String artifact, Boolean isActive);

  public Boolean removeArtifactFromTool(String artifactName, ToolDTO tool);

  public Boolean removeTool(String toolName);
}
