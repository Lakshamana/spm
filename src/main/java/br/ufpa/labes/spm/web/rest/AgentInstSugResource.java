package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.AgentInstSug;
import br.ufpa.labes.spm.repository.AgentInstSugRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.AgentInstSug}.
 */
@RestController
@RequestMapping("/api")
public class AgentInstSugResource {

    private final Logger log = LoggerFactory.getLogger(AgentInstSugResource.class);

    private static final String ENTITY_NAME = "agentInstSug";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgentInstSugRepository agentInstSugRepository;

    public AgentInstSugResource(AgentInstSugRepository agentInstSugRepository) {
        this.agentInstSugRepository = agentInstSugRepository;
    }

    /**
     * {@code POST  /agent-inst-sugs} : Create a new agentInstSug.
     *
     * @param agentInstSug the agentInstSug to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentInstSug, or with status {@code 400 (Bad Request)} if the agentInstSug has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agent-inst-sugs")
    public ResponseEntity<AgentInstSug> createAgentInstSug(@RequestBody AgentInstSug agentInstSug) throws URISyntaxException {
        log.debug("REST request to save AgentInstSug : {}", agentInstSug);
        if (agentInstSug.getId() != null) {
            throw new BadRequestAlertException("A new agentInstSug cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgentInstSug result = agentInstSugRepository.save(agentInstSug);
        return ResponseEntity.created(new URI("/api/agent-inst-sugs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agent-inst-sugs} : Updates an existing agentInstSug.
     *
     * @param agentInstSug the agentInstSug to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentInstSug,
     * or with status {@code 400 (Bad Request)} if the agentInstSug is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentInstSug couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agent-inst-sugs")
    public ResponseEntity<AgentInstSug> updateAgentInstSug(@RequestBody AgentInstSug agentInstSug) throws URISyntaxException {
        log.debug("REST request to update AgentInstSug : {}", agentInstSug);
        if (agentInstSug.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AgentInstSug result = agentInstSugRepository.save(agentInstSug);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentInstSug.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /agent-inst-sugs} : get all the agentInstSugs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentInstSugs in body.
     */
    @GetMapping("/agent-inst-sugs")
    public List<AgentInstSug> getAllAgentInstSugs() {
        log.debug("REST request to get all AgentInstSugs");
        return agentInstSugRepository.findAll();
    }

    /**
     * {@code GET  /agent-inst-sugs/:id} : get the "id" agentInstSug.
     *
     * @param id the id of the agentInstSug to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentInstSug, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agent-inst-sugs/{id}")
    public ResponseEntity<AgentInstSug> getAgentInstSug(@PathVariable Long id) {
        log.debug("REST request to get AgentInstSug : {}", id);
        Optional<AgentInstSug> agentInstSug = agentInstSugRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(agentInstSug);
    }

    /**
     * {@code DELETE  /agent-inst-sugs/:id} : delete the "id" agentInstSug.
     *
     * @param id the id of the agentInstSug to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agent-inst-sugs/{id}")
    public ResponseEntity<Void> deleteAgentInstSug(@PathVariable Long id) {
        log.debug("REST request to delete AgentInstSug : {}", id);
        agentInstSugRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
