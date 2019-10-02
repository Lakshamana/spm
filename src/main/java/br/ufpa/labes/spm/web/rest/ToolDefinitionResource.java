package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ToolDefinition;
import br.ufpa.labes.spm.repository.ToolDefinitionRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ToolDefinition}.
 */
@RestController
@RequestMapping("/api")
public class ToolDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(ToolDefinitionResource.class);

    private static final String ENTITY_NAME = "toolDefinition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToolDefinitionRepository toolDefinitionRepository;

    public ToolDefinitionResource(ToolDefinitionRepository toolDefinitionRepository) {
        this.toolDefinitionRepository = toolDefinitionRepository;
    }

    /**
     * {@code POST  /tool-definitions} : Create a new toolDefinition.
     *
     * @param toolDefinition the toolDefinition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toolDefinition, or with status {@code 400 (Bad Request)} if the toolDefinition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tool-definitions")
    public ResponseEntity<ToolDefinition> createToolDefinition(@RequestBody ToolDefinition toolDefinition) throws URISyntaxException {
        log.debug("REST request to save ToolDefinition : {}", toolDefinition);
        if (toolDefinition.getId() != null) {
            throw new BadRequestAlertException("A new toolDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToolDefinition result = toolDefinitionRepository.save(toolDefinition);
        return ResponseEntity.created(new URI("/api/tool-definitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tool-definitions} : Updates an existing toolDefinition.
     *
     * @param toolDefinition the toolDefinition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toolDefinition,
     * or with status {@code 400 (Bad Request)} if the toolDefinition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toolDefinition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tool-definitions")
    public ResponseEntity<ToolDefinition> updateToolDefinition(@RequestBody ToolDefinition toolDefinition) throws URISyntaxException {
        log.debug("REST request to update ToolDefinition : {}", toolDefinition);
        if (toolDefinition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ToolDefinition result = toolDefinitionRepository.save(toolDefinition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toolDefinition.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tool-definitions} : get all the toolDefinitions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toolDefinitions in body.
     */
    @GetMapping("/tool-definitions")
    public List<ToolDefinition> getAllToolDefinitions(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ToolDefinitions");
        return toolDefinitionRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /tool-definitions/:id} : get the "id" toolDefinition.
     *
     * @param id the id of the toolDefinition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toolDefinition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tool-definitions/{id}")
    public ResponseEntity<ToolDefinition> getToolDefinition(@PathVariable Long id) {
        log.debug("REST request to get ToolDefinition : {}", id);
        Optional<ToolDefinition> toolDefinition = toolDefinitionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(toolDefinition);
    }

    /**
     * {@code DELETE  /tool-definitions/:id} : delete the "id" toolDefinition.
     *
     * @param id the id of the toolDefinition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tool-definitions/{id}")
    public ResponseEntity<Void> deleteToolDefinition(@PathVariable Long id) {
        log.debug("REST request to delete ToolDefinition : {}", id);
        toolDefinitionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
