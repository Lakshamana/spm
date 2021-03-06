package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.BranchConCondToActivityService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.BranchConCondToActivityDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.BranchConCondToActivity}.
 */
@RestController
@RequestMapping("/api")
public class BranchConCondToActivityResource {

    private final Logger log = LoggerFactory.getLogger(BranchConCondToActivityResource.class);

    private static final String ENTITY_NAME = "branchConCondToActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchConCondToActivityService branchConCondToActivityService;

    public BranchConCondToActivityResource(BranchConCondToActivityService branchConCondToActivityService) {
        this.branchConCondToActivityService = branchConCondToActivityService;
    }

    /**
     * {@code POST  /branch-con-cond-to-activities} : Create a new branchConCondToActivity.
     *
     * @param branchConCondToActivityDTO the branchConCondToActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchConCondToActivityDTO, or with status {@code 400 (Bad Request)} if the branchConCondToActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/branch-con-cond-to-activities")
    public ResponseEntity<BranchConCondToActivityDTO> createBranchConCondToActivity(@RequestBody BranchConCondToActivityDTO branchConCondToActivityDTO) throws URISyntaxException {
        log.debug("REST request to save BranchConCondToActivity : {}", branchConCondToActivityDTO);
        if (branchConCondToActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new branchConCondToActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BranchConCondToActivityDTO result = branchConCondToActivityService.save(branchConCondToActivityDTO);
        return ResponseEntity.created(new URI("/api/branch-con-cond-to-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branch-con-cond-to-activities} : Updates an existing branchConCondToActivity.
     *
     * @param branchConCondToActivityDTO the branchConCondToActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchConCondToActivityDTO,
     * or with status {@code 400 (Bad Request)} if the branchConCondToActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchConCondToActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/branch-con-cond-to-activities")
    public ResponseEntity<BranchConCondToActivityDTO> updateBranchConCondToActivity(@RequestBody BranchConCondToActivityDTO branchConCondToActivityDTO) throws URISyntaxException {
        log.debug("REST request to update BranchConCondToActivity : {}", branchConCondToActivityDTO);
        if (branchConCondToActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BranchConCondToActivityDTO result = branchConCondToActivityService.save(branchConCondToActivityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, branchConCondToActivityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /branch-con-cond-to-activities} : get all the branchConCondToActivities.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchConCondToActivities in body.
     */
    @GetMapping("/branch-con-cond-to-activities")
    public List<BranchConCondToActivityDTO> getAllBranchConCondToActivities() {
        log.debug("REST request to get all BranchConCondToActivities");
        return branchConCondToActivityService.findAll();
    }

    /**
     * {@code GET  /branch-con-cond-to-activities/:id} : get the "id" branchConCondToActivity.
     *
     * @param id the id of the branchConCondToActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchConCondToActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branch-con-cond-to-activities/{id}")
    public ResponseEntity<BranchConCondToActivityDTO> getBranchConCondToActivity(@PathVariable Long id) {
        log.debug("REST request to get BranchConCondToActivity : {}", id);
        Optional<BranchConCondToActivityDTO> branchConCondToActivityDTO = branchConCondToActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(branchConCondToActivityDTO);
    }

    /**
     * {@code DELETE  /branch-con-cond-to-activities/:id} : delete the "id" branchConCondToActivity.
     *
     * @param id the id of the branchConCondToActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/branch-con-cond-to-activities/{id}")
    public ResponseEntity<Void> deleteBranchConCondToActivity(@PathVariable Long id) {
        log.debug("REST request to delete BranchConCondToActivity : {}", id);
        branchConCondToActivityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
