package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.LogEntryDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link LogEntry} and its DTO {@link LogEntryDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class})
public interface LogEntryMapper extends EntityMapper<LogEntryDTO, LogEntry> {

  @Mapping(source = "user.id", target = "userId")
  LogEntryDTO toDto(LogEntry logEntry);

  @Mapping(source = "userId", target = "user")
  LogEntry toEntity(LogEntryDTO logEntryDTO);

  default LogEntry fromId(Long id) {
    if (id == null) {
      return null;
    }
    LogEntry logEntry = new LogEntry();
    logEntry.setId(id);
    return logEntry;
  }
}
