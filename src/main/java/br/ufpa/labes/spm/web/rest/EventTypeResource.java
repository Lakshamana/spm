package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.EventTypeService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.EventTypeDTO;

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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.EventType}.
 */
@RestController
@RequestMapping("/api")
public class EventTypeResource {

    private final Logger log = LoggerFactory.getLogger(EventTypeResource.class);

    private static final String ENTITY_NAME = "eventType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventTypeService eventTypeService;

    public EventTypeResource(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    /**
     * {@code POST  /event-types} : Create a new eventType.
     *
     * @param eventTypeDTO the eventTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventTypeDTO, or with status {@code 400 (Bad Request)} if the eventType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-types")
    public ResponseEntity<EventTypeDTO> createEventType(@RequestBody EventTypeDTO eventTypeDTO) throws URISyntaxException {
        log.debug("REST request to save EventType : {}", eventTypeDTO);
        if (eventTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventTypeDTO result = eventTypeService.save(eventTypeDTO);
        return ResponseEntity.created(new URI("/api/event-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-types} : Updates an existing eventType.
     *
     * @param eventTypeDTO the eventTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventTypeDTO,
     * or with status {@code 400 (Bad Request)} if the eventTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-types")
    public ResponseEntity<EventTypeDTO> updateEventType(@RequestBody EventTypeDTO eventTypeDTO) throws URISyntaxException {
        log.debug("REST request to update EventType : {}", eventTypeDTO);
        if (eventTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EventTypeDTO result = eventTypeService.save(eventTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /event-types} : get all the eventTypes.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventTypes in body.
     */
    @GetMapping("/event-types")
    public List<EventTypeDTO> getAllEventTypes(@RequestParam(required = false) String filter) {
        if ("thetypesuper-is-null".equals(filter)) {
            log.debug("REST request to get all EventTypes where theTypeSuper is null");
            return eventTypeService.findAllWhereTheTypeSuperIsNull();
        }
        log.debug("REST request to get all EventTypes");
        return eventTypeService.findAll();
    }

    /**
     * {@code GET  /event-types/:id} : get the "id" eventType.
     *
     * @param id the id of the eventTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-types/{id}")
    public ResponseEntity<EventTypeDTO> getEventType(@PathVariable Long id) {
        log.debug("REST request to get EventType : {}", id);
        Optional<EventTypeDTO> eventTypeDTO = eventTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventTypeDTO);
    }

    /**
     * {@code DELETE  /event-types/:id} : delete the "id" eventType.
     *
     * @param id the id of the eventTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-types/{id}")
    public ResponseEntity<Void> deleteEventType(@PathVariable Long id) {
        log.debug("REST request to delete EventType : {}", id);
        eventTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
