package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class AgendaEventsDTO implements Serializable {
  List<AgendaEventDTO> agendaEvents;

  public AgendaEventsDTO() {
    this.agendaEvents = new ArrayList<AgendaEventDTO>();
  }

  public AgendaEventsDTO(List<AgendaEventDTO> agendaEvents) {
    this.agendaEvents = agendaEvents;
  }

  public boolean addAgendaEvent(AgendaEventDTO agendaEventDTO) {
    return agendaEvents.add(agendaEventDTO);
  }

  public boolean addAll(List<AgendaEventDTO> agendaEventsDTO) {
    return agendaEvents.addAll(agendaEventsDTO);
  }

  public boolean addAll(AgendaEventsDTO agendaEventsDTO) {
    return agendaEvents.addAll(agendaEventsDTO.agendaEvents);
  }

  public boolean removeAgendaEventDTO(AgendaEventDTO agendaEventDTO) {
    return agendaEvents.remove(agendaEventDTO);
  }

  public AgendaEventDTO getAgendaEventDTO(int index) {
    return agendaEvents.get(index);
  }

  public List<String> getCatalogEvents(AgendaEventFormatter formatter) {
    List<String> catalogEvents = new ArrayList<String>();
    for (AgendaEventDTO event : agendaEvents) {
      catalogEvents.add(
          "[" + formatter.formatDate(event.getWhen()) + "] - " + event.getCatalogEvent());
    }

    return catalogEvents;
  }

  public boolean isEmpty() {
    return agendaEvents.isEmpty();
  }

  public int size() {
    return agendaEvents.size();
  }
}
