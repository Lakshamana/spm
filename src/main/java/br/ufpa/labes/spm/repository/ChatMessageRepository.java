package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.chat.IChatMessageDAO;


import br.ufpa.labes.spm.domain.ChatMessage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ChatMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatMessageRepository extends IChatMessageDAO, JpaRepository<ChatMessage, Long> {

}
