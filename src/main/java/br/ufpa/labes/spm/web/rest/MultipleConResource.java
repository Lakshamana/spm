package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.MultipleCon;
import br.ufpa.labes.spm.repository.MultipleConRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.MultipleCon}.
 */
@RestController
@RequestMapping("/api")
public class MultipleConResource {

    private final Logger log = LoggerFactory.getLogger(MultipleConResource.class);

    private static final String ENTITY_NAME = "multipleCon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MultipleConRepository multipleConRepository;

    public MultipleConResource(MultipleConRepository multipleConRepository) {
        this.multipleConRepository = multipleConRepository;
    }

    /**
     * {@code POST  /multiple-cons} : Create a new multipleCon.
     *
     * @param multipleCon the multipleCon to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new multipleCon, or with status {@code 400 (Bad Request)} if the multipleCon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/multiple-cons")
    public ResponseEntity<MultipleCon> createMultipleCon(@RequestBody MultipleCon multipleCon) throws URISyntaxException {
        log.debug("REST request to save MultipleCon : {}", multipleCon);
        if (multipleCon.getId() != null) {
            throw new BadRequestAlertException("A new multipleCon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MultipleCon result = multipleConRepository.save(multipleCon);
        return ResponseEntity.created(new URI("/api/multiple-cons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /multiple-cons} : Updates an existing multipleCon.
     *
     * @param multipleCon the multipleCon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated multipleCon,
     * or with status {@code 400 (Bad Request)} if the multipleCon is not valid,
     * or with status {@code 500 (Internal Server Error)} if the multipleCon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/multiple-cons")
    public ResponseEntity<MultipleCon> updateMultipleCon(@RequestBody MultipleCon multipleCon) throws URISyntaxException {
        log.debug("REST request to update MultipleCon : {}", multipleCon);
        if (multipleCon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MultipleCon result = multipleConRepository.save(multipleCon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, multipleCon.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /multiple-cons} : get all the multipleCons.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of multipleCons in body.
     */
    @GetMapping("/multiple-cons")
    public List<MultipleCon> getAllMultipleCons() {
        log.debug("REST request to get all MultipleCons");
        return multipleConRepository.findAll();
    }

    /**
     * {@code GET  /multiple-cons/:id} : get the "id" multipleCon.
     *
     * @param id the id of the multipleCon to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the multipleCon, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/multiple-cons/{id}")
    public ResponseEntity<MultipleCon> getMultipleCon(@PathVariable Long id) {
        log.debug("REST request to get MultipleCon : {}", id);
        Optional<MultipleCon> multipleCon = multipleConRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(multipleCon);
    }

    /**
     * {@code DELETE  /multiple-cons/:id} : delete the "id" multipleCon.
     *
     * @param id the id of the multipleCon to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/multiple-cons/{id}")
    public ResponseEntity<Void> deleteMultipleCon(@PathVariable Long id) {
        log.debug("REST request to delete MultipleCon : {}", id);
        multipleConRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
