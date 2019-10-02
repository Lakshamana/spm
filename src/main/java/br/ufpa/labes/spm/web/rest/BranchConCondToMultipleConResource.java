package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.BranchConCondToMultipleCon;
import br.ufpa.labes.spm.repository.BranchConCondToMultipleConRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.BranchConCondToMultipleCon}.
 */
@RestController
@RequestMapping("/api")
public class BranchConCondToMultipleConResource {

    private final Logger log = LoggerFactory.getLogger(BranchConCondToMultipleConResource.class);

    private static final String ENTITY_NAME = "branchConCondToMultipleCon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchConCondToMultipleConRepository branchConCondToMultipleConRepository;

    public BranchConCondToMultipleConResource(BranchConCondToMultipleConRepository branchConCondToMultipleConRepository) {
        this.branchConCondToMultipleConRepository = branchConCondToMultipleConRepository;
    }

    /**
     * {@code POST  /branch-con-cond-to-multiple-cons} : Create a new branchConCondToMultipleCon.
     *
     * @param branchConCondToMultipleCon the branchConCondToMultipleCon to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchConCondToMultipleCon, or with status {@code 400 (Bad Request)} if the branchConCondToMultipleCon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/branch-con-cond-to-multiple-cons")
    public ResponseEntity<BranchConCondToMultipleCon> createBranchConCondToMultipleCon(@RequestBody BranchConCondToMultipleCon branchConCondToMultipleCon) throws URISyntaxException {
        log.debug("REST request to save BranchConCondToMultipleCon : {}", branchConCondToMultipleCon);
        if (branchConCondToMultipleCon.getId() != null) {
            throw new BadRequestAlertException("A new branchConCondToMultipleCon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BranchConCondToMultipleCon result = branchConCondToMultipleConRepository.save(branchConCondToMultipleCon);
        return ResponseEntity.created(new URI("/api/branch-con-cond-to-multiple-cons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branch-con-cond-to-multiple-cons} : Updates an existing branchConCondToMultipleCon.
     *
     * @param branchConCondToMultipleCon the branchConCondToMultipleCon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchConCondToMultipleCon,
     * or with status {@code 400 (Bad Request)} if the branchConCondToMultipleCon is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchConCondToMultipleCon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/branch-con-cond-to-multiple-cons")
    public ResponseEntity<BranchConCondToMultipleCon> updateBranchConCondToMultipleCon(@RequestBody BranchConCondToMultipleCon branchConCondToMultipleCon) throws URISyntaxException {
        log.debug("REST request to update BranchConCondToMultipleCon : {}", branchConCondToMultipleCon);
        if (branchConCondToMultipleCon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BranchConCondToMultipleCon result = branchConCondToMultipleConRepository.save(branchConCondToMultipleCon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, branchConCondToMultipleCon.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /branch-con-cond-to-multiple-cons} : get all the branchConCondToMultipleCons.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchConCondToMultipleCons in body.
     */
    @GetMapping("/branch-con-cond-to-multiple-cons")
    public List<BranchConCondToMultipleCon> getAllBranchConCondToMultipleCons() {
        log.debug("REST request to get all BranchConCondToMultipleCons");
        return branchConCondToMultipleConRepository.findAll();
    }

    /**
     * {@code GET  /branch-con-cond-to-multiple-cons/:id} : get the "id" branchConCondToMultipleCon.
     *
     * @param id the id of the branchConCondToMultipleCon to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchConCondToMultipleCon, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branch-con-cond-to-multiple-cons/{id}")
    public ResponseEntity<BranchConCondToMultipleCon> getBranchConCondToMultipleCon(@PathVariable Long id) {
        log.debug("REST request to get BranchConCondToMultipleCon : {}", id);
        Optional<BranchConCondToMultipleCon> branchConCondToMultipleCon = branchConCondToMultipleConRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(branchConCondToMultipleCon);
    }

    /**
     * {@code DELETE  /branch-con-cond-to-multiple-cons/:id} : delete the "id" branchConCondToMultipleCon.
     *
     * @param id the id of the branchConCondToMultipleCon to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/branch-con-cond-to-multiple-cons/{id}")
    public ResponseEntity<Void> deleteBranchConCondToMultipleCon(@PathVariable Long id) {
        log.debug("REST request to delete BranchConCondToMultipleCon : {}", id);
        branchConCondToMultipleConRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
