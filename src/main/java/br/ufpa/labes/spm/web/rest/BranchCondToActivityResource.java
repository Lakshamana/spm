package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.BranchCondToActivityService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.BranchCondToActivityDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.BranchCondToActivity}.
 */
@RestController
@RequestMapping("/api")
public class BranchCondToActivityResource {

    private final Logger log = LoggerFactory.getLogger(BranchCondToActivityResource.class);

    private static final String ENTITY_NAME = "branchCondToActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchCondToActivityService branchCondToActivityService;

    public BranchCondToActivityResource(BranchCondToActivityService branchCondToActivityService) {
        this.branchCondToActivityService = branchCondToActivityService;
    }

    /**
     * {@code POST  /branch-cond-to-activities} : Create a new branchCondToActivity.
     *
     * @param branchCondToActivityDTO the branchCondToActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchCondToActivityDTO, or with status {@code 400 (Bad Request)} if the branchCondToActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/branch-cond-to-activities")
    public ResponseEntity<BranchCondToActivityDTO> createBranchCondToActivity(@RequestBody BranchCondToActivityDTO branchCondToActivityDTO) throws URISyntaxException {
        log.debug("REST request to save BranchCondToActivity : {}", branchCondToActivityDTO);
        if (branchCondToActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new branchCondToActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BranchCondToActivityDTO result = branchCondToActivityService.save(branchCondToActivityDTO);
        return ResponseEntity.created(new URI("/api/branch-cond-to-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branch-cond-to-activities} : Updates an existing branchCondToActivity.
     *
     * @param branchCondToActivityDTO the branchCondToActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchCondToActivityDTO,
     * or with status {@code 400 (Bad Request)} if the branchCondToActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchCondToActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/branch-cond-to-activities")
    public ResponseEntity<BranchCondToActivityDTO> updateBranchCondToActivity(@RequestBody BranchCondToActivityDTO branchCondToActivityDTO) throws URISyntaxException {
        log.debug("REST request to update BranchCondToActivity : {}", branchCondToActivityDTO);
        if (branchCondToActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BranchCondToActivityDTO result = branchCondToActivityService.save(branchCondToActivityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, branchCondToActivityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /branch-cond-to-activities} : get all the branchCondToActivities.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchCondToActivities in body.
     */
    @GetMapping("/branch-cond-to-activities")
    public List<BranchCondToActivityDTO> getAllBranchCondToActivities() {
        log.debug("REST request to get all BranchCondToActivities");
        return branchCondToActivityService.findAll();
    }

    /**
     * {@code GET  /branch-cond-to-activities/:id} : get the "id" branchCondToActivity.
     *
     * @param id the id of the branchCondToActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchCondToActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branch-cond-to-activities/{id}")
    public ResponseEntity<BranchCondToActivityDTO> getBranchCondToActivity(@PathVariable Long id) {
        log.debug("REST request to get BranchCondToActivity : {}", id);
        Optional<BranchCondToActivityDTO> branchCondToActivityDTO = branchCondToActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(branchCondToActivityDTO);
    }

    /**
     * {@code DELETE  /branch-cond-to-activities/:id} : delete the "id" branchCondToActivity.
     *
     * @param id the id of the branchCondToActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/branch-cond-to-activities/{id}")
    public ResponseEntity<Void> deleteBranchCondToActivity(@PathVariable Long id) {
        log.debug("REST request to delete BranchCondToActivity : {}", id);
        branchCondToActivityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
