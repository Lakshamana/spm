package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Automatic;
import br.ufpa.labes.spm.repository.AutomaticRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Automatic}.
 */
@RestController
@RequestMapping("/api")
public class AutomaticResource {

    private final Logger log = LoggerFactory.getLogger(AutomaticResource.class);

    private static final String ENTITY_NAME = "automatic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutomaticRepository automaticRepository;

    public AutomaticResource(AutomaticRepository automaticRepository) {
        this.automaticRepository = automaticRepository;
    }

    /**
     * {@code POST  /automatics} : Create a new automatic.
     *
     * @param automatic the automatic to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new automatic, or with status {@code 400 (Bad Request)} if the automatic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/automatics")
    public ResponseEntity<Automatic> createAutomatic(@RequestBody Automatic automatic) throws URISyntaxException {
        log.debug("REST request to save Automatic : {}", automatic);
        if (automatic.getId() != null) {
            throw new BadRequestAlertException("A new automatic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Automatic result = automaticRepository.save(automatic);
        return ResponseEntity.created(new URI("/api/automatics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /automatics} : Updates an existing automatic.
     *
     * @param automatic the automatic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated automatic,
     * or with status {@code 400 (Bad Request)} if the automatic is not valid,
     * or with status {@code 500 (Internal Server Error)} if the automatic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/automatics")
    public ResponseEntity<Automatic> updateAutomatic(@RequestBody Automatic automatic) throws URISyntaxException {
        log.debug("REST request to update Automatic : {}", automatic);
        if (automatic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Automatic result = automaticRepository.save(automatic);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, automatic.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /automatics} : get all the automatics.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of automatics in body.
     */
    @GetMapping("/automatics")
    public List<Automatic> getAllAutomatics() {
        log.debug("REST request to get all Automatics");
        return automaticRepository.findAll();
    }

    /**
     * {@code GET  /automatics/:id} : get the "id" automatic.
     *
     * @param id the id of the automatic to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the automatic, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/automatics/{id}")
    public ResponseEntity<Automatic> getAutomatic(@PathVariable Long id) {
        log.debug("REST request to get Automatic : {}", id);
        Optional<Automatic> automatic = automaticRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(automatic);
    }

    /**
     * {@code DELETE  /automatics/:id} : delete the "id" automatic.
     *
     * @param id the id of the automatic to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/automatics/{id}")
    public ResponseEntity<Void> deleteAutomatic(@PathVariable Long id) {
        log.debug("REST request to delete Automatic : {}", id);
        automaticRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
