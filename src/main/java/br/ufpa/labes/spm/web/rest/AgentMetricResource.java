package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AgentMetricService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AgentMetricDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.AgentMetric}.
 */
@RestController
@RequestMapping("/api")
public class AgentMetricResource {

    private final Logger log = LoggerFactory.getLogger(AgentMetricResource.class);

    private static final String ENTITY_NAME = "agentMetric";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgentMetricService agentMetricService;

    public AgentMetricResource(AgentMetricService agentMetricService) {
        this.agentMetricService = agentMetricService;
    }

    /**
     * {@code POST  /agent-metrics} : Create a new agentMetric.
     *
     * @param agentMetricDTO the agentMetricDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentMetricDTO, or with status {@code 400 (Bad Request)} if the agentMetric has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agent-metrics")
    public ResponseEntity<AgentMetricDTO> createAgentMetric(@RequestBody AgentMetricDTO agentMetricDTO) throws URISyntaxException {
        log.debug("REST request to save AgentMetric : {}", agentMetricDTO);
        if (agentMetricDTO.getId() != null) {
            throw new BadRequestAlertException("A new agentMetric cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgentMetricDTO result = agentMetricService.save(agentMetricDTO);
        return ResponseEntity.created(new URI("/api/agent-metrics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agent-metrics} : Updates an existing agentMetric.
     *
     * @param agentMetricDTO the agentMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentMetricDTO,
     * or with status {@code 400 (Bad Request)} if the agentMetricDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agent-metrics")
    public ResponseEntity<AgentMetricDTO> updateAgentMetric(@RequestBody AgentMetricDTO agentMetricDTO) throws URISyntaxException {
        log.debug("REST request to update AgentMetric : {}", agentMetricDTO);
        if (agentMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AgentMetricDTO result = agentMetricService.save(agentMetricDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentMetricDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /agent-metrics} : get all the agentMetrics.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentMetrics in body.
     */
    @GetMapping("/agent-metrics")
    public List<AgentMetricDTO> getAllAgentMetrics(@RequestParam(required = false) String filter) {
        if ("themetricsuper-is-null".equals(filter)) {
            log.debug("REST request to get all AgentMetrics where theMetricSuper is null");
            return agentMetricService.findAllWhereTheMetricSuperIsNull();
        }
        log.debug("REST request to get all AgentMetrics");
        return agentMetricService.findAll();
    }

    /**
     * {@code GET  /agent-metrics/:id} : get the "id" agentMetric.
     *
     * @param id the id of the agentMetricDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentMetricDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agent-metrics/{id}")
    public ResponseEntity<AgentMetricDTO> getAgentMetric(@PathVariable Long id) {
        log.debug("REST request to get AgentMetric : {}", id);
        Optional<AgentMetricDTO> agentMetricDTO = agentMetricService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agentMetricDTO);
    }

    /**
     * {@code DELETE  /agent-metrics/:id} : delete the "id" agentMetric.
     *
     * @param id the id of the agentMetricDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agent-metrics/{id}")
    public ResponseEntity<Void> deleteAgentMetric(@PathVariable Long id) {
        log.debug("REST request to delete AgentMetric : {}", id);
        agentMetricService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
