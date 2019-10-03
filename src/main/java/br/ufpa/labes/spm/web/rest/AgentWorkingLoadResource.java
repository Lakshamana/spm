package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AgentWorkingLoadService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AgentWorkingLoadDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.AgentWorkingLoad}.
 */
@RestController
@RequestMapping("/api")
public class AgentWorkingLoadResource {

    private final Logger log = LoggerFactory.getLogger(AgentWorkingLoadResource.class);

    private static final String ENTITY_NAME = "agentWorkingLoad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgentWorkingLoadService agentWorkingLoadService;

    public AgentWorkingLoadResource(AgentWorkingLoadService agentWorkingLoadService) {
        this.agentWorkingLoadService = agentWorkingLoadService;
    }

    /**
     * {@code POST  /agent-working-loads} : Create a new agentWorkingLoad.
     *
     * @param agentWorkingLoadDTO the agentWorkingLoadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentWorkingLoadDTO, or with status {@code 400 (Bad Request)} if the agentWorkingLoad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agent-working-loads")
    public ResponseEntity<AgentWorkingLoadDTO> createAgentWorkingLoad(@RequestBody AgentWorkingLoadDTO agentWorkingLoadDTO) throws URISyntaxException {
        log.debug("REST request to save AgentWorkingLoad : {}", agentWorkingLoadDTO);
        if (agentWorkingLoadDTO.getId() != null) {
            throw new BadRequestAlertException("A new agentWorkingLoad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgentWorkingLoadDTO result = agentWorkingLoadService.save(agentWorkingLoadDTO);
        return ResponseEntity.created(new URI("/api/agent-working-loads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agent-working-loads} : Updates an existing agentWorkingLoad.
     *
     * @param agentWorkingLoadDTO the agentWorkingLoadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentWorkingLoadDTO,
     * or with status {@code 400 (Bad Request)} if the agentWorkingLoadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentWorkingLoadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agent-working-loads")
    public ResponseEntity<AgentWorkingLoadDTO> updateAgentWorkingLoad(@RequestBody AgentWorkingLoadDTO agentWorkingLoadDTO) throws URISyntaxException {
        log.debug("REST request to update AgentWorkingLoad : {}", agentWorkingLoadDTO);
        if (agentWorkingLoadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AgentWorkingLoadDTO result = agentWorkingLoadService.save(agentWorkingLoadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentWorkingLoadDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /agent-working-loads} : get all the agentWorkingLoads.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentWorkingLoads in body.
     */
    @GetMapping("/agent-working-loads")
    public List<AgentWorkingLoadDTO> getAllAgentWorkingLoads() {
        log.debug("REST request to get all AgentWorkingLoads");
        return agentWorkingLoadService.findAll();
    }

    /**
     * {@code GET  /agent-working-loads/:id} : get the "id" agentWorkingLoad.
     *
     * @param id the id of the agentWorkingLoadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentWorkingLoadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agent-working-loads/{id}")
    public ResponseEntity<AgentWorkingLoadDTO> getAgentWorkingLoad(@PathVariable Long id) {
        log.debug("REST request to get AgentWorkingLoad : {}", id);
        Optional<AgentWorkingLoadDTO> agentWorkingLoadDTO = agentWorkingLoadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agentWorkingLoadDTO);
    }

    /**
     * {@code DELETE  /agent-working-loads/:id} : delete the "id" agentWorkingLoad.
     *
     * @param id the id of the agentWorkingLoadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agent-working-loads/{id}")
    public ResponseEntity<Void> deleteAgentWorkingLoad(@PathVariable Long id) {
        log.debug("REST request to delete AgentWorkingLoad : {}", id);
        agentWorkingLoadService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
