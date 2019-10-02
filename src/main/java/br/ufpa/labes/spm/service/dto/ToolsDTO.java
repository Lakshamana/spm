package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ToolsDTO implements Serializable {
  List<ToolDTO> tools;

  public ToolsDTO() {
    tools = new ArrayList<ToolDTO>();
  }

  public ToolsDTO(List<ToolDTO> tools) {
    this.tools = tools;
  }

  public boolean addTool(ToolDTO tool) {
    return tools.add(tool);
  }

  public void addTool(int index, ToolDTO tool) {
    tools.add(index, tool);
  }

  public boolean addTools(List<ToolDTO> tools) {
    return tools.addAll(tools);
  }

  public boolean addTools(ToolsDTO toolsDTO) {
    return tools.addAll(toolsDTO.getTools());
  }

  public boolean removeTool(ToolDTO tool) {
    return tools.remove(tool);
  }

  public boolean removeTools(List<ToolDTO> tools) {
    return tools.removeAll(tools);
  }

  public boolean isEmpty() {
    return tools.isEmpty();
  }

  public boolean contains(ToolDTO tool) {
    return tools.contains(tool);
  }

  public int size() {
    return tools.size();
  }

  public ToolDTO getTool(int index) {
    return tools.get(index);
  }

  public int indexOf(ToolDTO tool) {
    return tools.indexOf(tool);
  }

  public void clearToolsDTO() {
    tools.clear();
  }

  public List<String> getToolNames() {
    List<String> names = new ArrayList<String>();
    for (ToolDTO tool : tools) {
      names.add(tool.getName());
    }
    return names;
  }

  public List<String> getToolIdents() {
    List<String> idents = new ArrayList<String>();
    for (ToolDTO tool : tools) {
      idents.add(tool.getIdent());
    }
    return idents;
  }

  protected List<ToolDTO> getTools() {
    return this.tools;
  }
}
