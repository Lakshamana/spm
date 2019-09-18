package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AgendaEventService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AgendaEventDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.AgendaEvent}.
 */
@RestController
@RequestMapping("/api")
public class AgendaEventResource {

    private final Logger log = LoggerFactory.getLogger(AgendaEventResource.class);

    private static final String ENTITY_NAME = "agendaEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgendaEventService agendaEventService;

    public AgendaEventResource(AgendaEventService agendaEventService) {
        this.agendaEventService = agendaEventService;
    }

    /**
     * {@code POST  /agenda-events} : Create a new agendaEvent.
     *
     * @param agendaEventDTO the agendaEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agendaEventDTO, or with status {@code 400 (Bad Request)} if the agendaEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agenda-events")
    public ResponseEntity<AgendaEventDTO> createAgendaEvent(@RequestBody AgendaEventDTO agendaEventDTO) throws URISyntaxException {
        log.debug("REST request to save AgendaEvent : {}", agendaEventDTO);
        if (agendaEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new agendaEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgendaEventDTO result = agendaEventService.save(agendaEventDTO);
        return ResponseEntity.created(new URI("/api/agenda-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agenda-events} : Updates an existing agendaEvent.
     *
     * @param agendaEventDTO the agendaEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agendaEventDTO,
     * or with status {@code 400 (Bad Request)} if the agendaEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agendaEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agenda-events")
    public ResponseEntity<AgendaEventDTO> updateAgendaEvent(@RequestBody AgendaEventDTO agendaEventDTO) throws URISyntaxException {
        log.debug("REST request to update AgendaEvent : {}", agendaEventDTO);
        if (agendaEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AgendaEventDTO result = agendaEventService.save(agendaEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agendaEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /agenda-events} : get all the agendaEvents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agendaEvents in body.
     */
    @GetMapping("/agenda-events")
    public List<AgendaEventDTO> getAllAgendaEvents() {
        log.debug("REST request to get all AgendaEvents");
        return agendaEventService.findAll();
    }

    /**
     * {@code GET  /agenda-events/:id} : get the "id" agendaEvent.
     *
     * @param id the id of the agendaEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agendaEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agenda-events/{id}")
    public ResponseEntity<AgendaEventDTO> getAgendaEvent(@PathVariable Long id) {
        log.debug("REST request to get AgendaEvent : {}", id);
        Optional<AgendaEventDTO> agendaEventDTO = agendaEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agendaEventDTO);
    }

    /**
     * {@code DELETE  /agenda-events/:id} : delete the "id" agendaEvent.
     *
     * @param id the id of the agendaEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agenda-events/{id}")
    public ResponseEntity<Void> deleteAgendaEvent(@PathVariable Long id) {
        log.debug("REST request to delete AgendaEvent : {}", id);
        agendaEventService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
