package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.service.dto.ChatMessageDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link br.ufpa.labes.spm.domain.ChatMessage}.
 */
public interface ChatMessageService {

    /**
     * Save a chatMessage.
     *
     * @param chatMessageDTO the entity to save.
     * @return the persisted entity.
     */
    ChatMessageDTO save(ChatMessageDTO chatMessageDTO);

    /**
     * Get all the chatMessages.
     *
     * @return the list of entities.
     */
    List<ChatMessageDTO> findAll();


    /**
     * Get the "id" chatMessage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChatMessageDTO> findOne(Long id);

    /**
     * Delete the "id" chatMessage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
