package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ProcessModel;
import br.ufpa.labes.spm.repository.ProcessModelRepository;
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
import java.util.stream.Collectors;
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

    private final ProcessModelRepository processModelRepository;

    public ProcessModelResource(ProcessModelRepository processModelRepository) {
        this.processModelRepository = processModelRepository;
    }

    /**
     * {@code POST  /process-models} : Create a new processModel.
     *
     * @param processModel the processModel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processModel, or with status {@code 400 (Bad Request)} if the processModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-models")
    public ResponseEntity<ProcessModel> createProcessModel(@RequestBody ProcessModel processModel) throws URISyntaxException {
        log.debug("REST request to save ProcessModel : {}", processModel);
        if (processModel.getId() != null) {
            throw new BadRequestAlertException("A new processModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessModel result = processModelRepository.save(processModel);
        return ResponseEntity.created(new URI("/api/process-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-models} : Updates an existing processModel.
     *
     * @param processModel the processModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processModel,
     * or with status {@code 400 (Bad Request)} if the processModel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-models")
    public ResponseEntity<ProcessModel> updateProcessModel(@RequestBody ProcessModel processModel) throws URISyntaxException {
        log.debug("REST request to update ProcessModel : {}", processModel);
        if (processModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProcessModel result = processModelRepository.save(processModel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processModel.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /process-models} : get all the processModels.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processModels in body.
     */
    @GetMapping("/process-models")
    public List<ProcessModel> getAllProcessModels(@RequestParam(required = false) String filter) {
        if ("thedecomposed-is-null".equals(filter)) {
            log.debug("REST request to get all ProcessModels where theDecomposed is null");
            return StreamSupport
                .stream(processModelRepository.findAll().spliterator(), false)
                .filter(processModel -> processModel.getTheDecomposed() == null)
                .collect(Collectors.toList());
        }
        if ("theprocess-is-null".equals(filter)) {
            log.debug("REST request to get all ProcessModels where theProcess is null");
            return StreamSupport
                .stream(processModelRepository.findAll().spliterator(), false)
                .filter(processModel -> processModel.getTheProcess() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all ProcessModels");
        return processModelRepository.findAll();
    }

    /**
     * {@code GET  /process-models/:id} : get the "id" processModel.
     *
     * @param id the id of the processModel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processModel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-models/{id}")
    public ResponseEntity<ProcessModel> getProcessModel(@PathVariable Long id) {
        log.debug("REST request to get ProcessModel : {}", id);
        Optional<ProcessModel> processModel = processModelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(processModel);
    }

    /**
     * {@code DELETE  /process-models/:id} : delete the "id" processModel.
     *
     * @param id the id of the processModel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-models/{id}")
    public ResponseEntity<Void> deleteProcessModel(@PathVariable Long id) {
        log.debug("REST request to delete ProcessModel : {}", id);
        processModelRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
