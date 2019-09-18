package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.BranchANDConService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.BranchANDConDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.BranchANDCon}.
 */
@RestController
@RequestMapping("/api")
public class BranchANDConResource {

    private final Logger log = LoggerFactory.getLogger(BranchANDConResource.class);

    private static final String ENTITY_NAME = "branchANDCon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BranchANDConService branchANDConService;

    public BranchANDConResource(BranchANDConService branchANDConService) {
        this.branchANDConService = branchANDConService;
    }

    /**
     * {@code POST  /branch-and-cons} : Create a new branchANDCon.
     *
     * @param branchANDConDTO the branchANDConDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchANDConDTO, or with status {@code 400 (Bad Request)} if the branchANDCon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/branch-and-cons")
    public ResponseEntity<BranchANDConDTO> createBranchANDCon(@RequestBody BranchANDConDTO branchANDConDTO) throws URISyntaxException {
        log.debug("REST request to save BranchANDCon : {}", branchANDConDTO);
        if (branchANDConDTO.getId() != null) {
            throw new BadRequestAlertException("A new branchANDCon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BranchANDConDTO result = branchANDConService.save(branchANDConDTO);
        return ResponseEntity.created(new URI("/api/branch-and-cons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /branch-and-cons} : Updates an existing branchANDCon.
     *
     * @param branchANDConDTO the branchANDConDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchANDConDTO,
     * or with status {@code 400 (Bad Request)} if the branchANDConDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchANDConDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/branch-and-cons")
    public ResponseEntity<BranchANDConDTO> updateBranchANDCon(@RequestBody BranchANDConDTO branchANDConDTO) throws URISyntaxException {
        log.debug("REST request to update BranchANDCon : {}", branchANDConDTO);
        if (branchANDConDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BranchANDConDTO result = branchANDConService.save(branchANDConDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, branchANDConDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /branch-and-cons} : get all the branchANDCons.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of branchANDCons in body.
     */
    @GetMapping("/branch-and-cons")
    public List<BranchANDConDTO> getAllBranchANDCons(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all BranchANDCons");
        return branchANDConService.findAll();
    }

    /**
     * {@code GET  /branch-and-cons/:id} : get the "id" branchANDCon.
     *
     * @param id the id of the branchANDConDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchANDConDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branch-and-cons/{id}")
    public ResponseEntity<BranchANDConDTO> getBranchANDCon(@PathVariable Long id) {
        log.debug("REST request to get BranchANDCon : {}", id);
        Optional<BranchANDConDTO> branchANDConDTO = branchANDConService.findOne(id);
        return ResponseUtil.wrapOrNotFound(branchANDConDTO);
    }

    /**
     * {@code DELETE  /branch-and-cons/:id} : delete the "id" branchANDCon.
     *
     * @param id the id of the branchANDConDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/branch-and-cons/{id}")
    public ResponseEntity<Void> deleteBranchANDCon(@PathVariable Long id) {
        log.debug("REST request to delete BranchANDCon : {}", id);
        branchANDConService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
