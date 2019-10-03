package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AgentInstSuggestionToAgentService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AgentInstSuggestionToAgentDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.AgentInstSuggestionToAgent}.
 */
@RestController
@RequestMapping("/api")
public class AgentInstSuggestionToAgentResource {

    private final Logger log = LoggerFactory.getLogger(AgentInstSuggestionToAgentResource.class);

    private static final String ENTITY_NAME = "agentInstSuggestionToAgent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgentInstSuggestionToAgentService agentInstSuggestionToAgentService;

    public AgentInstSuggestionToAgentResource(AgentInstSuggestionToAgentService agentInstSuggestionToAgentService) {
        this.agentInstSuggestionToAgentService = agentInstSuggestionToAgentService;
    }

    /**
     * {@code POST  /agent-inst-suggestion-to-agents} : Create a new agentInstSuggestionToAgent.
     *
     * @param agentInstSuggestionToAgentDTO the agentInstSuggestionToAgentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentInstSuggestionToAgentDTO, or with status {@code 400 (Bad Request)} if the agentInstSuggestionToAgent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agent-inst-suggestion-to-agents")
    public ResponseEntity<AgentInstSuggestionToAgentDTO> createAgentInstSuggestionToAgent(@RequestBody AgentInstSuggestionToAgentDTO agentInstSuggestionToAgentDTO) throws URISyntaxException {
        log.debug("REST request to save AgentInstSuggestionToAgent : {}", agentInstSuggestionToAgentDTO);
        if (agentInstSuggestionToAgentDTO.getId() != null) {
            throw new BadRequestAlertException("A new agentInstSuggestionToAgent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgentInstSuggestionToAgentDTO result = agentInstSuggestionToAgentService.save(agentInstSuggestionToAgentDTO);
        return ResponseEntity.created(new URI("/api/agent-inst-suggestion-to-agents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agent-inst-suggestion-to-agents} : Updates an existing agentInstSuggestionToAgent.
     *
     * @param agentInstSuggestionToAgentDTO the agentInstSuggestionToAgentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentInstSuggestionToAgentDTO,
     * or with status {@code 400 (Bad Request)} if the agentInstSuggestionToAgentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentInstSuggestionToAgentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agent-inst-suggestion-to-agents")
    public ResponseEntity<AgentInstSuggestionToAgentDTO> updateAgentInstSuggestionToAgent(@RequestBody AgentInstSuggestionToAgentDTO agentInstSuggestionToAgentDTO) throws URISyntaxException {
        log.debug("REST request to update AgentInstSuggestionToAgent : {}", agentInstSuggestionToAgentDTO);
        if (agentInstSuggestionToAgentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AgentInstSuggestionToAgentDTO result = agentInstSuggestionToAgentService.save(agentInstSuggestionToAgentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentInstSuggestionToAgentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /agent-inst-suggestion-to-agents} : get all the agentInstSuggestionToAgents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentInstSuggestionToAgents in body.
     */
    @GetMapping("/agent-inst-suggestion-to-agents")
    public List<AgentInstSuggestionToAgentDTO> getAllAgentInstSuggestionToAgents() {
        log.debug("REST request to get all AgentInstSuggestionToAgents");
        return agentInstSuggestionToAgentService.findAll();
    }

    /**
     * {@code GET  /agent-inst-suggestion-to-agents/:id} : get the "id" agentInstSuggestionToAgent.
     *
     * @param id the id of the agentInstSuggestionToAgentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentInstSuggestionToAgentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agent-inst-suggestion-to-agents/{id}")
    public ResponseEntity<AgentInstSuggestionToAgentDTO> getAgentInstSuggestionToAgent(@PathVariable Long id) {
        log.debug("REST request to get AgentInstSuggestionToAgent : {}", id);
        Optional<AgentInstSuggestionToAgentDTO> agentInstSuggestionToAgentDTO = agentInstSuggestionToAgentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agentInstSuggestionToAgentDTO);
    }

    /**
     * {@code DELETE  /agent-inst-suggestion-to-agents/:id} : delete the "id" agentInstSuggestionToAgent.
     *
     * @param id the id of the agentInstSuggestionToAgentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agent-inst-suggestion-to-agents/{id}")
    public ResponseEntity<Void> deleteAgentInstSuggestionToAgent(@PathVariable Long id) {
        log.debug("REST request to delete AgentInstSuggestionToAgent : {}", id);
        agentInstSuggestionToAgentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
