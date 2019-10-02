package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.MessageDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Message} and its DTO {@link MessageDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {AuthorMapper.class, AssetMapper.class})
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {

  @Mapping(source = "sender.id", target = "senderId")
  @Mapping(source = "recipient.id", target = "recipientId")
  @Mapping(source = "theAsset.id", target = "theAssetId")
  MessageDTO toDto(Message message);

  @Mapping(source = "senderId", target = "sender")
  @Mapping(source = "recipientId", target = "recipient")
  @Mapping(source = "theAssetId", target = "theAsset")
  Message toEntity(MessageDTO messageDTO);

  default Message fromId(Long id) {
    if (id == null) {
      return null;
    }
    Message message = new Message();
    message.setId(id);
    return message;
  }
}
