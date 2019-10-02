package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class GroupsDTO implements Serializable {

  private List<GroupDTO> groups;

  public GroupsDTO() {}

  public GroupsDTO(List<GroupDTO> groups) {
    this.groups = groups;
  }

  public boolean addGroupDTO(GroupDTO groupDTO) {
    return this.groups.add(groupDTO);
  }

  public boolean addGroupsDTO(List<GroupDTO> groupsDTO) {
    return this.groups.addAll(groupsDTO);
  }

  public boolean addGroupsDTO(GroupsDTO groupsDTO) {
    List<GroupDTO> groupsList = new ArrayList<GroupDTO>();
    for (int i = 0; i < groupsDTO.size(); i++) {
      groupsList.add(groupsDTO.getGroupDTO(i));
    }
    return this.groups.addAll(groupsList);
  }

  public GroupDTO getGroupDTO(int index) {
    return this.groups.get(index);
  }

  public List<String> getGroupNames() {
    List<String> names = new ArrayList<String>();
    for (GroupDTO groupDTO : this.groups) {
      names.add(groupDTO.getName());
    }
    return names;
  }

  public List<String> getGroupIdents() {
    List<String> idents = new ArrayList<String>();
    for (GroupDTO groupDTO : this.groups) {
      idents.add(groupDTO.getIdent());
    }
    return idents;
  }

  public GroupsDTO groupsWithoutSuperGroup() {
    List<GroupDTO> groupsWithoutSuperGroup = new ArrayList<GroupDTO>();
    for (GroupDTO groupDTO : this.groups) {
      if (groupDTO.getSuperGroup().equals("")) {
        groupsWithoutSuperGroup.add(groupDTO);
      }
    }

    return new GroupsDTO(groupsWithoutSuperGroup);
  }

  public GroupsDTO groupsWithSuperGroup(String superGroupName) {
    List<GroupDTO> groupsWithSuperGroup = new ArrayList<GroupDTO>();
    for (GroupDTO groupDTO : this.groups) {
      if (groupDTO.getSuperGroup().equals(superGroupName)) {
        groupsWithSuperGroup.add(groupDTO);
      }
    }

    return new GroupsDTO(groupsWithSuperGroup);
  }

  public int indexOf(GroupDTO groupDTO) {
    return this.indexOf(groupDTO);
  }

  public int size() {
    return this.groups.size();
  }

  public boolean isEmpty() {
    return this.groups.isEmpty();
  }

  public boolean containsGroupDTO(GroupDTO groupDTO) {
    return this.groups.contains(groupDTO);
  }
}
