package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ToolParameterService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ToolParameterDTO;

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

    private final ToolParameterService toolParameterService;

    public ToolParameterResource(ToolParameterService toolParameterService) {
        this.toolParameterService = toolParameterService;
    }

    /**
     * {@code POST  /tool-parameters} : Create a new toolParameter.
     *
     * @param toolParameterDTO the toolParameterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toolParameterDTO, or with status {@code 400 (Bad Request)} if the toolParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tool-parameters")
    public ResponseEntity<ToolParameterDTO> createToolParameter(@RequestBody ToolParameterDTO toolParameterDTO) throws URISyntaxException {
        log.debug("REST request to save ToolParameter : {}", toolParameterDTO);
        if (toolParameterDTO.getId() != null) {
            throw new BadRequestAlertException("A new toolParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToolParameterDTO result = toolParameterService.save(toolParameterDTO);
        return ResponseEntity.created(new URI("/api/tool-parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tool-parameters} : Updates an existing toolParameter.
     *
     * @param toolParameterDTO the toolParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toolParameterDTO,
     * or with status {@code 400 (Bad Request)} if the toolParameterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toolParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tool-parameters")
    public ResponseEntity<ToolParameterDTO> updateToolParameter(@RequestBody ToolParameterDTO toolParameterDTO) throws URISyntaxException {
        log.debug("REST request to update ToolParameter : {}", toolParameterDTO);
        if (toolParameterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ToolParameterDTO result = toolParameterService.save(toolParameterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toolParameterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tool-parameters} : get all the toolParameters.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toolParameters in body.
     */
    @GetMapping("/tool-parameters")
    public List<ToolParameterDTO> getAllToolParameters() {
        log.debug("REST request to get all ToolParameters");
        return toolParameterService.findAll();
    }

    /**
     * {@code GET  /tool-parameters/:id} : get the "id" toolParameter.
     *
     * @param id the id of the toolParameterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toolParameterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tool-parameters/{id}")
    public ResponseEntity<ToolParameterDTO> getToolParameter(@PathVariable Long id) {
        log.debug("REST request to get ToolParameter : {}", id);
        Optional<ToolParameterDTO> toolParameterDTO = toolParameterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toolParameterDTO);
    }

    /**
     * {@code DELETE  /tool-parameters/:id} : delete the "id" toolParameter.
     *
     * @param id the id of the toolParameterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tool-parameters/{id}")
    public ResponseEntity<Void> deleteToolParameter(@PathVariable Long id) {
        log.debug("REST request to delete ToolParameter : {}", id);
        toolParameterService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
