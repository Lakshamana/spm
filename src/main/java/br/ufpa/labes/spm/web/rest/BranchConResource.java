package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.BranchCon;
import br.ufpa.labes.spm.repository.BranchConRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.BranchCon}.
 */
@RestController
@RequestMapping("/api")
public class BranchConResource {

    private final Logger log = LoggerFactory.getLogger(BranchConResource.class);

    private static final String ENTITY_NAME = "branchCon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchConRepository branchConRepository;

    public BranchConResource(BranchConRepository branchConRepository) {
        this.branchConRepository = branchConRepository;
    }

    /**
     * {@code POST  /branch-cons} : Create a new branchCon.
     *
     * @param branchCon the branchCon to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchCon, or with status {@code 400 (Bad Request)} if the branchCon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/branch-cons")
    public ResponseEntity<BranchCon> createBranchCon(@RequestBody BranchCon branchCon) throws URISyntaxException {
        log.debug("REST request to save BranchCon : {}", branchCon);
        if (branchCon.getId() != null) {
            throw new BadRequestAlertException("A new branchCon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BranchCon result = branchConRepository.save(branchCon);
        return ResponseEntity.created(new URI("/api/branch-cons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branch-cons} : Updates an existing branchCon.
     *
     * @param branchCon the branchCon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchCon,
     * or with status {@code 400 (Bad Request)} if the branchCon is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchCon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/branch-cons")
    public ResponseEntity<BranchCon> updateBranchCon(@RequestBody BranchCon branchCon) throws URISyntaxException {
        log.debug("REST request to update BranchCon : {}", branchCon);
        if (branchCon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BranchCon result = branchConRepository.save(branchCon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, branchCon.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /branch-cons} : get all the branchCons.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchCons in body.
     */
    @GetMapping("/branch-cons")
    public List<BranchCon> getAllBranchCons() {
        log.debug("REST request to get all BranchCons");
        return branchConRepository.findAll();
    }

    /**
     * {@code GET  /branch-cons/:id} : get the "id" branchCon.
     *
     * @param id the id of the branchCon to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchCon, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branch-cons/{id}")
    public ResponseEntity<BranchCon> getBranchCon(@PathVariable Long id) {
        log.debug("REST request to get BranchCon : {}", id);
        Optional<BranchCon> branchCon = branchConRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(branchCon);
    }

    /**
     * {@code DELETE  /branch-cons/:id} : delete the "id" branchCon.
     *
     * @param id the id of the branchCon to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/branch-cons/{id}")
    public ResponseEntity<Void> deleteBranchCon(@PathVariable Long id) {
        log.debug("REST request to delete BranchCon : {}", id);
        branchConRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
