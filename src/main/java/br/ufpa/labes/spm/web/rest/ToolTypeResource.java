package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ToolType;
import br.ufpa.labes.spm.repository.ToolTypeRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ToolType}.
 */
@RestController
@RequestMapping("/api")
public class ToolTypeResource {

    private final Logger log = LoggerFactory.getLogger(ToolTypeResource.class);

    private static final String ENTITY_NAME = "toolType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToolTypeRepository toolTypeRepository;

    public ToolTypeResource(ToolTypeRepository toolTypeRepository) {
        this.toolTypeRepository = toolTypeRepository;
    }

    /**
     * {@code POST  /tool-types} : Create a new toolType.
     *
     * @param toolType the toolType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toolType, or with status {@code 400 (Bad Request)} if the toolType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tool-types")
    public ResponseEntity<ToolType> createToolType(@RequestBody ToolType toolType) throws URISyntaxException {
        log.debug("REST request to save ToolType : {}", toolType);
        if (toolType.getId() != null) {
            throw new BadRequestAlertException("A new toolType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToolType result = toolTypeRepository.save(toolType);
        return ResponseEntity.created(new URI("/api/tool-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tool-types} : Updates an existing toolType.
     *
     * @param toolType the toolType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toolType,
     * or with status {@code 400 (Bad Request)} if the toolType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toolType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tool-types")
    public ResponseEntity<ToolType> updateToolType(@RequestBody ToolType toolType) throws URISyntaxException {
        log.debug("REST request to update ToolType : {}", toolType);
        if (toolType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ToolType result = toolTypeRepository.save(toolType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toolType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tool-types} : get all the toolTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toolTypes in body.
     */
    @GetMapping("/tool-types")
    public List<ToolType> getAllToolTypes() {
        log.debug("REST request to get all ToolTypes");
        return toolTypeRepository.findAll();
    }

    /**
     * {@code GET  /tool-types/:id} : get the "id" toolType.
     *
     * @param id the id of the toolType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toolType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tool-types/{id}")
    public ResponseEntity<ToolType> getToolType(@PathVariable Long id) {
        log.debug("REST request to get ToolType : {}", id);
        Optional<ToolType> toolType = toolTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(toolType);
    }

    /**
     * {@code DELETE  /tool-types/:id} : delete the "id" toolType.
     *
     * @param id the id of the toolType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tool-types/{id}")
    public ResponseEntity<Void> deleteToolType(@PathVariable Long id) {
        log.debug("REST request to delete ToolType : {}", id);
        toolTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
