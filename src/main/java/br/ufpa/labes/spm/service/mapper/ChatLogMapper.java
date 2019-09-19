package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ChatLogDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatLog} and its DTO {@link ChatLogDTO}.
 */
@Mapper(componentModel = "spring", uses = {AgentMapper.class})
public interface ChatLogMapper extends EntityMapper<ChatLogDTO, ChatLog> {


    @Mapping(target = "removeInvolvedAgentsInChat", ignore = true)

    default ChatLog fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatLog chatLog = new ChatLog();
        chatLog.setId(id);
        return chatLog;
    }
}
