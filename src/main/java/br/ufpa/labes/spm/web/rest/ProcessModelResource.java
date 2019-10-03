package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ProcessModelService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ProcessModelDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ProcessModel}.
 */
@RestController
@RequestMapping("/api")
public class ProcessModelResource {

    private final Logger log = LoggerFactory.getLogger(ProcessModelResource.class);

    private static final String ENTITY_NAME = "processModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessModelService processModelService;

    public ProcessModelResource(ProcessModelService processModelService) {
        this.processModelService = processModelService;
    }

    /**
     * {@code POST  /process-models} : Create a new processModel.
     *
     * @param processModelDTO the processModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processModelDTO, or with status {@code 400 (Bad Request)} if the processModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-models")
    public ResponseEntity<ProcessModelDTO> createProcessModel(@RequestBody ProcessModelDTO processModelDTO) throws URISyntaxException {
        log.debug("REST request to save ProcessModel : {}", processModelDTO);
        if (processModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new processModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessModelDTO result = processModelService.save(processModelDTO);
        return ResponseEntity.created(new URI("/api/process-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-models} : Updates an existing processModel.
     *
     * @param processModelDTO the processModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processModelDTO,
     * or with status {@code 400 (Bad Request)} if the processModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-models")
    public ResponseEntity<ProcessModelDTO> updateProcessModel(@RequestBody ProcessModelDTO processModelDTO) throws URISyntaxException {
        log.debug("REST request to update ProcessModel : {}", processModelDTO);
        if (processModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProcessModelDTO result = processModelService.save(processModelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processModelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /process-models} : get all the processModels.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processModels in body.
     */
    @GetMapping("/process-models")
    public List<ProcessModelDTO> getAllProcessModels(@RequestParam(required = false) String filter) {
        if ("thedecomposed-is-null".equals(filter)) {
            log.debug("REST request to get all ProcessModels where theDecomposed is null");
            return processModelService.findAllWhereTheDecomposedIsNull();
        }
        if ("theprocess-is-null".equals(filter)) {
            log.debug("REST request to get all ProcessModels where theProcess is null");
            return processModelService.findAllWhereTheProcessIsNull();
        }
        log.debug("REST request to get all ProcessModels");
        return processModelService.findAll();
    }

    /**
     * {@code GET  /process-models/:id} : get the "id" processModel.
     *
     * @param id the id of the processModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-models/{id}")
    public ResponseEntity<ProcessModelDTO> getProcessModel(@PathVariable Long id) {
        log.debug("REST request to get ProcessModel : {}", id);
        Optional<ProcessModelDTO> processModelDTO = processModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processModelDTO);
    }

    /**
     * {@code DELETE  /process-models/:id} : delete the "id" processModel.
     *
     * @param id the id of the processModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-models/{id}")
    public ResponseEntity<Void> deleteProcessModel(@PathVariable Long id) {
        log.debug("REST request to delete ProcessModel : {}", id);
        processModelService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
