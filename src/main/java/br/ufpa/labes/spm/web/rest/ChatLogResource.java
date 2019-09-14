package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ChatLogService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ChatLogDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ChatLog}.
 */
@RestController
@RequestMapping("/api")
public class ChatLogResource {

    private final Logger log = LoggerFactory.getLogger(ChatLogResource.class);

    private static final String ENTITY_NAME = "chatLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatLogService chatLogService;

    public ChatLogResource(ChatLogService chatLogService) {
        this.chatLogService = chatLogService;
    }

    /**
     * {@code POST  /chat-logs} : Create a new chatLog.
     *
     * @param chatLogDTO the chatLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatLogDTO, or with status {@code 400 (Bad Request)} if the chatLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chat-logs")
    public ResponseEntity<ChatLogDTO> createChatLog(@RequestBody ChatLogDTO chatLogDTO) throws URISyntaxException {
        log.debug("REST request to save ChatLog : {}", chatLogDTO);
        if (chatLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatLogDTO result = chatLogService.save(chatLogDTO);
        return ResponseEntity.created(new URI("/api/chat-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chat-logs} : Updates an existing chatLog.
     *
     * @param chatLogDTO the chatLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatLogDTO,
     * or with status {@code 400 (Bad Request)} if the chatLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chat-logs")
    public ResponseEntity<ChatLogDTO> updateChatLog(@RequestBody ChatLogDTO chatLogDTO) throws URISyntaxException {
        log.debug("REST request to update ChatLog : {}", chatLogDTO);
        if (chatLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChatLogDTO result = chatLogService.save(chatLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatLogDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chat-logs} : get all the chatLogs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatLogs in body.
     */
    @GetMapping("/chat-logs")
    public List<ChatLogDTO> getAllChatLogs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ChatLogs");
        return chatLogService.findAll();
    }

    /**
     * {@code GET  /chat-logs/:id} : get the "id" chatLog.
     *
     * @param id the id of the chatLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chat-logs/{id}")
    public ResponseEntity<ChatLogDTO> getChatLog(@PathVariable Long id) {
        log.debug("REST request to get ChatLog : {}", id);
        Optional<ChatLogDTO> chatLogDTO = chatLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatLogDTO);
    }

    /**
     * {@code DELETE  /chat-logs/:id} : delete the "id" chatLog.
     *
     * @param id the id of the chatLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chat-logs/{id}")
    public ResponseEntity<Void> deleteChatLog(@PathVariable Long id) {
        log.debug("REST request to delete ChatLog : {}", id);
        chatLogService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
