package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.PeopleInstSug;
import br.ufpa.labes.spm.repository.PeopleInstSugRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.PeopleInstSug}.
 */
@RestController
@RequestMapping("/api")
public class PeopleInstSugResource {

    private final Logger log = LoggerFactory.getLogger(PeopleInstSugResource.class);

    private static final String ENTITY_NAME = "peopleInstSug";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeopleInstSugRepository peopleInstSugRepository;

    public PeopleInstSugResource(PeopleInstSugRepository peopleInstSugRepository) {
        this.peopleInstSugRepository = peopleInstSugRepository;
    }

    /**
     * {@code POST  /people-inst-sugs} : Create a new peopleInstSug.
     *
     * @param peopleInstSug the peopleInstSug to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new peopleInstSug, or with status {@code 400 (Bad Request)} if the peopleInstSug has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/people-inst-sugs")
    public ResponseEntity<PeopleInstSug> createPeopleInstSug(@RequestBody PeopleInstSug peopleInstSug) throws URISyntaxException {
        log.debug("REST request to save PeopleInstSug : {}", peopleInstSug);
        if (peopleInstSug.getId() != null) {
            throw new BadRequestAlertException("A new peopleInstSug cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeopleInstSug result = peopleInstSugRepository.save(peopleInstSug);
        return ResponseEntity.created(new URI("/api/people-inst-sugs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /people-inst-sugs} : Updates an existing peopleInstSug.
     *
     * @param peopleInstSug the peopleInstSug to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peopleInstSug,
     * or with status {@code 400 (Bad Request)} if the peopleInstSug is not valid,
     * or with status {@code 500 (Internal Server Error)} if the peopleInstSug couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/people-inst-sugs")
    public ResponseEntity<PeopleInstSug> updatePeopleInstSug(@RequestBody PeopleInstSug peopleInstSug) throws URISyntaxException {
        log.debug("REST request to update PeopleInstSug : {}", peopleInstSug);
        if (peopleInstSug.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PeopleInstSug result = peopleInstSugRepository.save(peopleInstSug);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, peopleInstSug.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /people-inst-sugs} : get all the peopleInstSugs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of peopleInstSugs in body.
     */
    @GetMapping("/people-inst-sugs")
    public List<PeopleInstSug> getAllPeopleInstSugs() {
        log.debug("REST request to get all PeopleInstSugs");
        return peopleInstSugRepository.findAll();
    }

    /**
     * {@code GET  /people-inst-sugs/:id} : get the "id" peopleInstSug.
     *
     * @param id the id of the peopleInstSug to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the peopleInstSug, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/people-inst-sugs/{id}")
    public ResponseEntity<PeopleInstSug> getPeopleInstSug(@PathVariable Long id) {
        log.debug("REST request to get PeopleInstSug : {}", id);
        Optional<PeopleInstSug> peopleInstSug = peopleInstSugRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(peopleInstSug);
    }

    /**
     * {@code DELETE  /people-inst-sugs/:id} : delete the "id" peopleInstSug.
     *
     * @param id the id of the peopleInstSug to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/people-inst-sugs/{id}")
    public ResponseEntity<Void> deletePeopleInstSug(@PathVariable Long id) {
        log.debug("REST request to delete PeopleInstSug : {}", id);
        peopleInstSugRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
