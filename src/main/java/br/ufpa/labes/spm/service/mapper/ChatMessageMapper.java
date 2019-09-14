package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ChatMessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatMessage} and its DTO {@link ChatMessageDTO}.
 */
@Mapper(componentModel = "spring", uses = {AgentMapper.class})
public interface ChatMessageMapper extends EntityMapper<ChatMessageDTO, ChatMessage> {

    @Mapping(source = "fromAgent.id", target = "fromAgentId")
    ChatMessageDTO toDto(ChatMessage chatMessage);

    @Mapping(source = "fromAgentId", target = "fromAgent")
    ChatMessage toEntity(ChatMessageDTO chatMessageDTO);

    default ChatMessage fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(id);
        return chatMessage;
    }
}
