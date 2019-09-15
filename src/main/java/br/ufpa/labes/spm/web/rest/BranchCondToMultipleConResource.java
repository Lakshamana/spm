package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.BranchCondToMultipleConService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.BranchCondToMultipleConDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.BranchCondToMultipleCon}.
 */
@RestController
@RequestMapping("/api")
public class BranchCondToMultipleConResource {

    private final Logger log = LoggerFactory.getLogger(BranchCondToMultipleConResource.class);

    private static final String ENTITY_NAME = "branchCondToMultipleCon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchCondToMultipleConService branchCondToMultipleConService;

    public BranchCondToMultipleConResource(BranchCondToMultipleConService branchCondToMultipleConService) {
        this.branchCondToMultipleConService = branchCondToMultipleConService;
    }

    /**
     * {@code POST  /branch-cond-to-multiple-cons} : Create a new branchCondToMultipleCon.
     *
     * @param branchCondToMultipleConDTO the branchCondToMultipleConDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchCondToMultipleConDTO, or with status {@code 400 (Bad Request)} if the branchCondToMultipleCon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/branch-cond-to-multiple-cons")
    public ResponseEntity<BranchCondToMultipleConDTO> createBranchCondToMultipleCon(@RequestBody BranchCondToMultipleConDTO branchCondToMultipleConDTO) throws URISyntaxException {
        log.debug("REST request to save BranchCondToMultipleCon : {}", branchCondToMultipleConDTO);
        if (branchCondToMultipleConDTO.getId() != null) {
            throw new BadRequestAlertException("A new branchCondToMultipleCon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BranchCondToMultipleConDTO result = branchCondToMultipleConService.save(branchCondToMultipleConDTO);
        return ResponseEntity.created(new URI("/api/branch-cond-to-multiple-cons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branch-cond-to-multiple-cons} : Updates an existing branchCondToMultipleCon.
     *
     * @param branchCondToMultipleConDTO the branchCondToMultipleConDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchCondToMultipleConDTO,
     * or with status {@code 400 (Bad Request)} if the branchCondToMultipleConDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchCondToMultipleConDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/branch-cond-to-multiple-cons")
    public ResponseEntity<BranchCondToMultipleConDTO> updateBranchCondToMultipleCon(@RequestBody BranchCondToMultipleConDTO branchCondToMultipleConDTO) throws URISyntaxException {
        log.debug("REST request to update BranchCondToMultipleCon : {}", branchCondToMultipleConDTO);
        if (branchCondToMultipleConDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BranchCondToMultipleConDTO result = branchCondToMultipleConService.save(branchCondToMultipleConDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, branchCondToMultipleConDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /branch-cond-to-multiple-cons} : get all the branchCondToMultipleCons.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchCondToMultipleCons in body.
     */
    @GetMapping("/branch-cond-to-multiple-cons")
    public List<BranchCondToMultipleConDTO> getAllBranchCondToMultipleCons() {
        log.debug("REST request to get all BranchCondToMultipleCons");
        return branchCondToMultipleConService.findAll();
    }

    /**
     * {@code GET  /branch-cond-to-multiple-cons/:id} : get the "id" branchCondToMultipleCon.
     *
     * @param id the id of the branchCondToMultipleConDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchCondToMultipleConDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branch-cond-to-multiple-cons/{id}")
    public ResponseEntity<BranchCondToMultipleConDTO> getBranchCondToMultipleCon(@PathVariable Long id) {
        log.debug("REST request to get BranchCondToMultipleCon : {}", id);
        Optional<BranchCondToMultipleConDTO> branchCondToMultipleConDTO = branchCondToMultipleConService.findOne(id);
        return ResponseUtil.wrapOrNotFound(branchCondToMultipleConDTO);
    }

    /**
     * {@code DELETE  /branch-cond-to-multiple-cons/:id} : delete the "id" branchCondToMultipleCon.
     *
     * @param id the id of the branchCondToMultipleConDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/branch-cond-to-multiple-cons/{id}")
    public ResponseEntity<Void> deleteBranchCondToMultipleCon(@PathVariable Long id) {
        log.debug("REST request to delete BranchCondToMultipleCon : {}", id);
        branchCondToMultipleConService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
