package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.InvolvedArtifact;
import br.ufpa.labes.spm.repository.InvolvedArtifactRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.InvolvedArtifact}.
 */
@RestController
@RequestMapping("/api")
public class InvolvedArtifactResource {

    private final Logger log = LoggerFactory.getLogger(InvolvedArtifactResource.class);

    private static final String ENTITY_NAME = "involvedArtifact";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvolvedArtifactRepository involvedArtifactRepository;

    public InvolvedArtifactResource(InvolvedArtifactRepository involvedArtifactRepository) {
        this.involvedArtifactRepository = involvedArtifactRepository;
    }

    /**
     * {@code POST  /involved-artifacts} : Create a new involvedArtifact.
     *
     * @param involvedArtifact the involvedArtifact to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new involvedArtifact, or with status {@code 400 (Bad Request)} if the involvedArtifact has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/involved-artifacts")
    public ResponseEntity<InvolvedArtifact> createInvolvedArtifact(@RequestBody InvolvedArtifact involvedArtifact) throws URISyntaxException {
        log.debug("REST request to save InvolvedArtifact : {}", involvedArtifact);
        if (involvedArtifact.getId() != null) {
            throw new BadRequestAlertException("A new involvedArtifact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvolvedArtifact result = involvedArtifactRepository.save(involvedArtifact);
        return ResponseEntity.created(new URI("/api/involved-artifacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /involved-artifacts} : Updates an existing involvedArtifact.
     *
     * @param involvedArtifact the involvedArtifact to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated involvedArtifact,
     * or with status {@code 400 (Bad Request)} if the involvedArtifact is not valid,
     * or with status {@code 500 (Internal Server Error)} if the involvedArtifact couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/involved-artifacts")
    public ResponseEntity<InvolvedArtifact> updateInvolvedArtifact(@RequestBody InvolvedArtifact involvedArtifact) throws URISyntaxException {
        log.debug("REST request to update InvolvedArtifact : {}", involvedArtifact);
        if (involvedArtifact.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvolvedArtifact result = involvedArtifactRepository.save(involvedArtifact);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, involvedArtifact.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /involved-artifacts} : get all the involvedArtifacts.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of involvedArtifacts in body.
     */
    @GetMapping("/involved-artifacts")
    public List<InvolvedArtifact> getAllInvolvedArtifacts() {
        log.debug("REST request to get all InvolvedArtifacts");
        return involvedArtifactRepository.findAll();
    }

    /**
     * {@code GET  /involved-artifacts/:id} : get the "id" involvedArtifact.
     *
     * @param id the id of the involvedArtifact to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the involvedArtifact, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/involved-artifacts/{id}")
    public ResponseEntity<InvolvedArtifact> getInvolvedArtifact(@PathVariable Long id) {
        log.debug("REST request to get InvolvedArtifact : {}", id);
        Optional<InvolvedArtifact> involvedArtifact = involvedArtifactRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(involvedArtifact);
    }

    /**
     * {@code DELETE  /involved-artifacts/:id} : delete the "id" involvedArtifact.
     *
     * @param id the id of the involvedArtifact to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/involved-artifacts/{id}")
    public ResponseEntity<Void> deleteInvolvedArtifact(@PathVariable Long id) {
        log.debug("REST request to delete InvolvedArtifact : {}", id);
        involvedArtifactRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
