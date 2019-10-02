package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.AgentInstSuggestionToAgent;
import br.ufpa.labes.spm.repository.AgentInstSuggestionToAgentRepository;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;

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

    private final AgentInstSuggestionToAgentRepository agentInstSuggestionToAgentRepository;

    public AgentInstSuggestionToAgentResource(AgentInstSuggestionToAgentRepository agentInstSuggestionToAgentRepository) {
        this.agentInstSuggestionToAgentRepository = agentInstSuggestionToAgentRepository;
    }

    /**
     * {@code POST  /agent-inst-suggestion-to-agents} : Create a new agentInstSuggestionToAgent.
     *
     * @param agentInstSuggestionToAgent the agentInstSuggestionToAgent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentInstSuggestionToAgent, or with status {@code 400 (Bad Request)} if the agentInstSuggestionToAgent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agent-inst-suggestion-to-agents")
    public ResponseEntity<AgentInstSuggestionToAgent> createAgentInstSuggestionToAgent(@RequestBody AgentInstSuggestionToAgent agentInstSuggestionToAgent) throws URISyntaxException {
        log.debug("REST request to save AgentInstSuggestionToAgent : {}", agentInstSuggestionToAgent);
        if (agentInstSuggestionToAgent.getId() != null) {
            throw new BadRequestAlertException("A new agentInstSuggestionToAgent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgentInstSuggestionToAgent result = agentInstSuggestionToAgentRepository.save(agentInstSuggestionToAgent);
        return ResponseEntity.created(new URI("/api/agent-inst-suggestion-to-agents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agent-inst-suggestion-to-agents} : Updates an existing agentInstSuggestionToAgent.
     *
     * @param agentInstSuggestionToAgent the agentInstSuggestionToAgent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentInstSuggestionToAgent,
     * or with status {@code 400 (Bad Request)} if the agentInstSuggestionToAgent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentInstSuggestionToAgent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agent-inst-suggestion-to-agents")
    public ResponseEntity<AgentInstSuggestionToAgent> updateAgentInstSuggestionToAgent(@RequestBody AgentInstSuggestionToAgent agentInstSuggestionToAgent) throws URISyntaxException {
        log.debug("REST request to update AgentInstSuggestionToAgent : {}", agentInstSuggestionToAgent);
        if (agentInstSuggestionToAgent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AgentInstSuggestionToAgent result = agentInstSuggestionToAgentRepository.save(agentInstSuggestionToAgent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentInstSuggestionToAgent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /agent-inst-suggestion-to-agents} : get all the agentInstSuggestionToAgents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentInstSuggestionToAgents in body.
     */
    @GetMapping("/agent-inst-suggestion-to-agents")
    public List<AgentInstSuggestionToAgent> getAllAgentInstSuggestionToAgents() {
        log.debug("REST request to get all AgentInstSuggestionToAgents");
        return agentInstSuggestionToAgentRepository.findAll();
    }

    /**
     * {@code GET  /agent-inst-suggestion-to-agents/:id} : get the "id" agentInstSuggestionToAgent.
     *
     * @param id the id of the agentInstSuggestionToAgent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentInstSuggestionToAgent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agent-inst-suggestion-to-agents/{id}")
    public ResponseEntity<AgentInstSuggestionToAgent> getAgentInstSuggestionToAgent(@PathVariable Long id) {
        log.debug("REST request to get AgentInstSuggestionToAgent : {}", id);
        Optional<AgentInstSuggestionToAgent> agentInstSuggestionToAgent = agentInstSuggestionToAgentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(agentInstSuggestionToAgent);
    }

    /**
     * {@code DELETE  /agent-inst-suggestion-to-agents/:id} : delete the "id" agentInstSuggestionToAgent.
     *
     * @param id the id of the agentInstSuggestionToAgent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agent-inst-suggestion-to-agents/{id}")
    public ResponseEntity<Void> deleteAgentInstSuggestionToAgent(@PathVariable Long id) {
        log.debug("REST request to delete AgentInstSuggestionToAgent : {}", id);
        agentInstSuggestionToAgentRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
