package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Ocurrence;
import br.ufpa.labes.spm.repository.OcurrenceRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Ocurrence}.
 */
@RestController
@RequestMapping("/api")
public class OcurrenceResource {

    private final Logger log = LoggerFactory.getLogger(OcurrenceResource.class);

    private static final String ENTITY_NAME = "ocurrence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OcurrenceRepository ocurrenceRepository;

    public OcurrenceResource(OcurrenceRepository ocurrenceRepository) {
        this.ocurrenceRepository = ocurrenceRepository;
    }

    /**
     * {@code POST  /ocurrences} : Create a new ocurrence.
     *
     * @param ocurrence the ocurrence to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ocurrence, or with status {@code 400 (Bad Request)} if the ocurrence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ocurrences")
    public ResponseEntity<Ocurrence> createOcurrence(@RequestBody Ocurrence ocurrence) throws URISyntaxException {
        log.debug("REST request to save Ocurrence : {}", ocurrence);
        if (ocurrence.getId() != null) {
            throw new BadRequestAlertException("A new ocurrence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ocurrence result = ocurrenceRepository.save(ocurrence);
        return ResponseEntity.created(new URI("/api/ocurrences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ocurrences} : Updates an existing ocurrence.
     *
     * @param ocurrence the ocurrence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ocurrence,
     * or with status {@code 400 (Bad Request)} if the ocurrence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ocurrence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ocurrences")
    public ResponseEntity<Ocurrence> updateOcurrence(@RequestBody Ocurrence ocurrence) throws URISyntaxException {
        log.debug("REST request to update Ocurrence : {}", ocurrence);
        if (ocurrence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ocurrence result = ocurrenceRepository.save(ocurrence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ocurrence.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ocurrences} : get all the ocurrences.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ocurrences in body.
     */
    @GetMapping("/ocurrences")
    public List<Ocurrence> getAllOcurrences() {
        log.debug("REST request to get all Ocurrences");
        return ocurrenceRepository.findAll();
    }

    /**
     * {@code GET  /ocurrences/:id} : get the "id" ocurrence.
     *
     * @param id the id of the ocurrence to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ocurrence, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ocurrences/{id}")
    public ResponseEntity<Ocurrence> getOcurrence(@PathVariable Long id) {
        log.debug("REST request to get Ocurrence : {}", id);
        Optional<Ocurrence> ocurrence = ocurrenceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ocurrence);
    }

    /**
     * {@code DELETE  /ocurrences/:id} : delete the "id" ocurrence.
     *
     * @param id the id of the ocurrence to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ocurrences/{id}")
    public ResponseEntity<Void> deleteOcurrence(@PathVariable Long id) {
        log.debug("REST request to delete Ocurrence : {}", id);
        ocurrenceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
