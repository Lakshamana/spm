package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.MessageService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.MessageDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Message}.
 */
@RestController
@RequestMapping("/api")
public class MessageResource {

    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    private static final String ENTITY_NAME = "message";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MessageService messageService;

    public MessageResource(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * {@code POST  /messages} : Create a new message.
     *
     * @param messageDTO the messageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new messageDTO, or with status {@code 400 (Bad Request)} if the message has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/messages")
    public ResponseEntity<MessageDTO> createMessage(@RequestBody MessageDTO messageDTO) throws URISyntaxException {
        log.debug("REST request to save Message : {}", messageDTO);
        if (messageDTO.getId() != null) {
            throw new BadRequestAlertException("A new message cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MessageDTO result = messageService.save(messageDTO);
        return ResponseEntity.created(new URI("/api/messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /messages} : Updates an existing message.
     *
     * @param messageDTO the messageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageDTO,
     * or with status {@code 400 (Bad Request)} if the messageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the messageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/messages")
    public ResponseEntity<MessageDTO> updateMessage(@RequestBody MessageDTO messageDTO) throws URISyntaxException {
        log.debug("REST request to update Message : {}", messageDTO);
        if (messageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MessageDTO result = messageService.save(messageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, messageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /messages} : get all the messages.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of messages in body.
     */
    @GetMapping("/messages")
    public List<MessageDTO> getAllMessages() {
        log.debug("REST request to get all Messages");
        return messageService.findAll();
    }

    /**
     * {@code GET  /messages/:id} : get the "id" message.
     *
     * @param id the id of the messageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the messageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/messages/{id}")
    public ResponseEntity<MessageDTO> getMessage(@PathVariable Long id) {
        log.debug("REST request to get Message : {}", id);
        Optional<MessageDTO> messageDTO = messageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(messageDTO);
    }

    /**
     * {@code DELETE  /messages/:id} : delete the "id" message.
     *
     * @param id the id of the messageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        log.debug("REST request to delete Message : {}", id);
        messageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
