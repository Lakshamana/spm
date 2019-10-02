package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ToolParameter;
import br.ufpa.labes.spm.repository.ToolParameterRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ToolParameter}.
 */
@RestController
@RequestMapping("/api")
public class ToolParameterResource {

    private final Logger log = LoggerFactory.getLogger(ToolParameterResource.class);

    private static final String ENTITY_NAME = "toolParameter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToolParameterRepository toolParameterRepository;

    public ToolParameterResource(ToolParameterRepository toolParameterRepository) {
        this.toolParameterRepository = toolParameterRepository;
    }

    /**
     * {@code POST  /tool-parameters} : Create a new toolParameter.
     *
     * @param toolParameter the toolParameter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toolParameter, or with status {@code 400 (Bad Request)} if the toolParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tool-parameters")
    public ResponseEntity<ToolParameter> createToolParameter(@RequestBody ToolParameter toolParameter) throws URISyntaxException {
        log.debug("REST request to save ToolParameter : {}", toolParameter);
        if (toolParameter.getId() != null) {
            throw new BadRequestAlertException("A new toolParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToolParameter result = toolParameterRepository.save(toolParameter);
        return ResponseEntity.created(new URI("/api/tool-parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tool-parameters} : Updates an existing toolParameter.
     *
     * @param toolParameter the toolParameter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toolParameter,
     * or with status {@code 400 (Bad Request)} if the toolParameter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toolParameter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tool-parameters")
    public ResponseEntity<ToolParameter> updateToolParameter(@RequestBody ToolParameter toolParameter) throws URISyntaxException {
        log.debug("REST request to update ToolParameter : {}", toolParameter);
        if (toolParameter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ToolParameter result = toolParameterRepository.save(toolParameter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toolParameter.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tool-parameters} : get all the toolParameters.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toolParameters in body.
     */
    @GetMapping("/tool-parameters")
    public List<ToolParameter> getAllToolParameters() {
        log.debug("REST request to get all ToolParameters");
        return toolParameterRepository.findAll();
    }

    /**
     * {@code GET  /tool-parameters/:id} : get the "id" toolParameter.
     *
     * @param id the id of the toolParameter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toolParameter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tool-parameters/{id}")
    public ResponseEntity<ToolParameter> getToolParameter(@PathVariable Long id) {
        log.debug("REST request to get ToolParameter : {}", id);
        Optional<ToolParameter> toolParameter = toolParameterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(toolParameter);
    }

    /**
     * {@code DELETE  /tool-parameters/:id} : delete the "id" toolParameter.
     *
     * @param id the id of the toolParameter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tool-parameters/{id}")
    public ResponseEntity<Void> deleteToolParameter(@PathVariable Long id) {
        log.debug("REST request to delete ToolParameter : {}", id);
        toolParameterRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
